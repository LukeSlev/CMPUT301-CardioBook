package com.lslevins.lslevins_cardiobook;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CardioListAdapter.ItemClickListener{
    private static final String TAG = "MainActivity";
    private RecyclerView                cardioRecyclerView;
    private CardioListAdapter       cardioStatsAdapter;
    private RecyclerView.LayoutManager  mLayoutManager;
    private List<CardioStats> cardioStats = new ArrayList<CardioStats>();

    public void setDemoData() {
        cardioStats.add(new CardioStats(LocalDateTime.now(),1,2,3,"asd"));
        cardioStats.add(new CardioStats(LocalDateTime.now(),111,322222,3333,"Oh my goodness"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardioRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        cardioRecyclerView.setLayoutManager(mLayoutManager);
        cardioRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // specify an adapter (see also next example)
        cardioStatsAdapter = new CardioListAdapter(cardioStats);
        cardioStatsAdapter.setClickListener(this);
        cardioRecyclerView.setAdapter(cardioStatsAdapter);
        setDemoData();

        // Button to be used to add entries
        FloatingActionButton addCardioButton = (FloatingActionButton) findViewById(R.id.fab);
        addCardioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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
