package com.lslevins.lslevins_cardiobook;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.time.format.DateTimeFormatter;

public class AddCardioStatActivity extends AppCompatActivity {
    private int requestType;
    private CardioStats cardioStats;
    private EditText systolic;
    private EditText diastolic;
    private EditText date;
    private EditText time;
    private EditText comment;
    public DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cardio_stat);
        Intent intent = getIntent();
        requestType = intent.getIntExtra(MainActivity.REQUEST_MESSAGE,-1);
        switch (requestType) {
            case MainActivity.NEW:
                systolic = findViewById(R.id.editSysPres);
                diastolic = findViewById(R.id.editDiaPres);
                date = findViewById(R.id.editDate);
                time = findViewById(R.id.editTime);
                comment = findViewById(R.id.editComment);
                break;
            case MainActivity.EDIT:
                systolic = findViewById(R.id.editSysPres);
                diastolic = findViewById(R.id.editDiaPres);
                date = findViewById(R.id.editDate);
                time = findViewById(R.id.editTime);
                comment = findViewById(R.id.editComment);

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


    }

    protected void returnResult() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra(CardioStats.class.getSimpleName(), cardioStats);
        finish();
    }
}
