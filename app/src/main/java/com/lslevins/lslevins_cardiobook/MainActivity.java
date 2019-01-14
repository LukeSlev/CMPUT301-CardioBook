package com.lslevins.lslevins_cardiobook;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CardioListAdapter.ItemClickListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String REQUEST_MESSAGE = "com.lslevins.lslevins.REQUEST";

    public static final int NEW = 1;
    public static final int EDIT = 2;

    private Context                     mContext;
    private DataStorage                 dataStorage;
    private TextView                    emptyMessage;
    private RecyclerView                cardioRecyclerView;
    private CardioListAdapter           cardioStatsAdapter;
    private RecyclerView.LayoutManager  mLayoutManager;
    private List<CardioStats> cardioStats = new ArrayList<CardioStats>();

    public void setDemoData() {
        cardioStats.add(new CardioStats(LocalDateTime.now(),1,2,3,"asd"));
        cardioStats.add(new CardioStats(LocalDateTime.now(),22,222,222,"Oh my goodness"));
        cardioStats.add(new CardioStats(LocalDateTime.now(),333,3333,33333,"sfasfasfasf"));
        cardioStats.add(new CardioStats(LocalDateTime.now(),4444,4444444,44444444,"hhashrshahr"));
        cardioStats.add(new CardioStats(LocalDateTime.now(),555,55555,55555,"Oh goodness"));
    }

    public void loadStatsData() {
        dataStorage = new DataStorage(mContext);
        cardioStats = dataStorage.loadStats();
        Log.d(TAG, "loadStatsData: SIZE"+cardioStats.size());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

        cardioRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        cardioRecyclerView.setLayoutManager(mLayoutManager);
        cardioRecyclerView.setItemAnimator(new DefaultItemAnimator());

        loadStatsData();
        // specify an adapter (see also next example)
        cardioStatsAdapter = new CardioListAdapter(cardioStats);
        cardioStatsAdapter.setClickListener(this);
        cardioRecyclerView.setAdapter(cardioStatsAdapter);
        emptyMessage = (TextView) findViewById(R.id.emptyMessage);

        emptyMessage();

        // Button to be used to add entries
        FloatingActionButton addCardioButton = (FloatingActionButton) findViewById(R.id.fab);
        addCardioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AddCardioStatActivity.class);
                intent.putExtra(REQUEST_MESSAGE, NEW);
                startActivityForResult(intent, NEW);
            }
        });
    }

    private void emptyMessage(){
        if ( cardioStats.size() == 0 ) {
            Log.d(TAG, "emptyMessage: NO STATS");
            emptyMessage.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "emptyMessage: STATS");
            emptyMessage.setVisibility(View.INVISIBLE);
        }
    }

    public static CardioStats setStats(Intent intent) {
        int systolic = intent.getIntExtra(AddCardioStatActivity.SYSTOLIC,-1);
        int diastolic = intent.getIntExtra(AddCardioStatActivity.DIASTOLIC,-1);
        int bpm = intent.getIntExtra(AddCardioStatActivity.BPM,-1);
        LocalDateTime dateTime = LocalDateTime.parse(intent.getStringExtra(AddCardioStatActivity.DATETIME));
        String comment = intent.getStringExtra(AddCardioStatActivity.COMMENT);

        return new CardioStats(dateTime,systolic,diastolic,bpm,comment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        CardioStats cs;
        // Check which request it is that we're responding to
        if (requestCode == NEW) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                cs = setStats(resultIntent);
                if (cs == null) {
                    Log.d(TAG, "setStats: Error");
                } else {
                    Log.d(TAG, "onActivityResult: new:"+cs.toString());
                    cardioStats.add(cs);
                    dataStorage.writeToFile(cardioStats);
                }
                emptyMessage();
                Log.d(TAG, "onActivityResult: Added new stat:"+cardioStats.size());
                Toast.makeText(mContext,"Added new stat",Toast.LENGTH_SHORT).show();
                cardioStatsAdapter.notifyItemInserted(cardioStats.size()-1);
                cardioRecyclerView.scrollToPosition(cardioStats.size()-1);

            }
        } else if (requestCode == EDIT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                int position = resultIntent.getIntExtra(CardioListAdapter.POSITION,0);
                cs = setStats(resultIntent);
                if (cs == null) {
                    Log.d(TAG, "setStats: Error");
                } else {
                    Log.d(TAG, "onActivityResult: edit:"+cs.toString());
                    cardioStats.set(position,cs);
                    dataStorage.writeToFile(cardioStats);
                }
                emptyMessage();
                Log.d(TAG, "onActivityResult: Added new stat");
                Toast.makeText(mContext,"Added new stat",Toast.LENGTH_SHORT).show();
                cardioStatsAdapter.notifyItemChanged(position);
                cardioRecyclerView.scrollToPosition(position);
            }
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + Integer.toString(cardioStatsAdapter.getItem(position)) + " on row number " + position, Toast.LENGTH_SHORT).show();
//        String friend = friendAdapter.getItem(position);
//        if (selectedFriendList.contains(friend)) {
//            view.setBackgroundColor(getResources().getColor(R.color.white));
//            Log.d(TAG, "already selected " + friend);
//        } else {
//            view.setBackgroundColor(getResources().getColor(R.color.grey));
//            selectedFriendList.add(friendAdapter.getItem(position));
//            Log.d("selectedFriends:" , selectedFriendList.toString());
//        }
    }
}
