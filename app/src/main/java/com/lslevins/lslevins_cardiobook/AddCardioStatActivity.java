package com.lslevins.lslevins_cardiobook;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddCardioStatActivity extends AppCompatActivity {
    public static final String TAG = AddCardioStatActivity.class.getSimpleName();
    public static final String SYSTOLIC = "SYSTOLIC";
    public static final String DIASTOLIC = "DIASTOLIC";
    public static final String DATETIME = "DATETIME";
    public static final String COMMENT = "COMMENT";
    public static final String BPM = "BPM";

    private int requestType;
    private EditText systolic;
    private EditText diastolic;
    private EditText date;
    private EditText time;
    private EditText comment;
    private EditText bpm;
    private Button submit;
    private CardioStats cardioStats = new CardioStats();
    public DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cardio_stat);
        Intent intent = getIntent();
        requestType = intent.getIntExtra(MainActivity.REQUEST_MESSAGE,-1);
        systolic = findViewById(R.id.editSysPres);
        diastolic = findViewById(R.id.editDiaPres);
        bpm = findViewById(R.id.editBPM);
        date = findViewById(R.id.editDate);
        time = findViewById(R.id.editTime);
        comment = findViewById(R.id.editComment);

        submit = findViewById(R.id.submitButton);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnResult();
            }
        });

        switch (requestType) {
            case MainActivity.NEW:
                break;
            case MainActivity.EDIT:
                cardioStats = (CardioStats) intent.getSerializableExtra(CardioStats.class.getSimpleName());

                systolic.setText(String.format("%d", cardioStats.getSystolicPressure()));
                diastolic.setText(String.format("%d", cardioStats.getDiastolicPressure()));
                date.setText(cardioStats.getDateTime().format(dateFormat));
                date.setText(cardioStats.getDateTime().format(timeFormat));
                comment.setText(cardioStats.getComment());
                break;
            default:
                returnResult();
        }
//        systolic.addTextChangedListener(this);
//        diastolic.addTextChangedListener(this);
//        date.addTextChangedListener(this);
//        time.addTextChangedListener(this);


    }

    protected void setStats() {
        LocalDateTime dateTime = LocalDateTime.parse(date.getText().toString() + " " + time.getText().toString(), CardioListAdapter.dateFormatter);
        debugStats(dateTime);
        cardioStats.setSystolicPressure(Integer.parseInt(systolic.getText().toString()));
        cardioStats.setDiastolicPressure(Integer.parseInt(diastolic.getText().toString()));
        cardioStats.setBpm(Integer.parseInt(bpm.getText().toString()));
        cardioStats.setDateTime(dateTime);
        cardioStats.setComment(comment.getText().toString());

    }

    protected void updateIntentStats(Intent intent) {
        intent.putExtra(SYSTOLIC, cardioStats.getSystolicPressure());
        intent.putExtra(DIASTOLIC, cardioStats.getDiastolicPressure());
        intent.putExtra(BPM, cardioStats.getBpm());
        intent.putExtra(DATETIME, cardioStats.getDateTime().toString());
        intent.putExtra(COMMENT, cardioStats.getComment());
    }

    private void debugStats(LocalDateTime dateTime) {
        Log.d(TAG, "setStats: sys:" + Integer.parseInt(systolic.getText().toString()));
        Log.d(TAG, "setStats: dia:" + diastolic.getText().toString());
        Log.d(TAG, "setStats: bpm:" + bpm.getText().toString());
        Log.d(TAG, "setStats: datetime:" + dateTime.toString());
        Log.d(TAG, "setStats: comment:" + comment.getText().toString());
    }

    /** Called when the user taps the Send button */
    public void returnResult() {
        setStats();
        Log.d(TAG, "returnResult: class:"+cardioStats.toString());
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        updateIntentStats(intent);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
