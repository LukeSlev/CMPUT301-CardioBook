package com.lslevins.lslevins_cardiobook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddCardioStatActivity extends AppCompatActivity {
    public static final String TAG = AddCardioStatActivity.class.getSimpleName();
    public static final String SYSTOLIC = "SYSTOLIC";
    public static final String DIASTOLIC = "DIASTOLIC";
    public static final String DATETIME = "DATETIME";
    public static final String COMMENT = "COMMENT";
    public static final String BPM = "BPM";

    private Context mContext;
    private int requestType;
    private EditText systolic;
    private EditText diastolic;
    private EditText date;
    private EditText time;
    private EditText comment;
    private EditText bpm;
    private Button submit;
    private CheckBox sysCheckBox;
    private CheckBox diaCheckBox;
    private CheckBox bpmCheckBox;
    private CheckBox dateCheckBox;
    private CheckBox timeCheckBox;

    private CardioStats cardioStats = new CardioStats();
    public DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cardio_stat);
        mContext = getApplicationContext();

        Intent intent = getIntent();
        requestType = intent.getIntExtra(MainActivity.REQUEST_MESSAGE,-1);
        systolic = findViewById(R.id.editSysPres);
        diastolic = findViewById(R.id.editDiaPres);
        bpm = findViewById(R.id.editBPM);
        date = findViewById(R.id.editDate);
        time = findViewById(R.id.editTime);
        comment = findViewById(R.id.editComment);
        submit = findViewById(R.id.submitButton);

        sysCheckBox = findViewById(R.id.sysCheckBox);
        diaCheckBox = findViewById(R.id.diaCheckBox);
        bpmCheckBox = findViewById(R.id.bpmCheckBox);
        dateCheckBox = findViewById(R.id.dateCheckBox);
        timeCheckBox = findViewById(R.id.timeCheckBox);

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

        // VALIDATION

        systolic.addTextChangedListener(new TextValidator(systolic) {
            @Override public void validate(TextView textView, String text) {
                /* Validation code here */
                boolean b = Pattern.matches("\\d+", text);

                if (b) {
                    sysCheckBox.setChecked(true);
                } else {
                    systolic.setError("Please enter a non-negative integer");
                    sysCheckBox.setChecked(false);
                }
            }
        });
        diastolic.addTextChangedListener(new TextValidator(diastolic) {
            @Override public void validate(TextView textView, String text) {
                /* Validation code here */
                boolean b = Pattern.matches("\\d+", text);

                if (b) {
                    diaCheckBox.setChecked(true);
                } else {
                    diastolic.setError("Please enter a non-negative integer");
                    diaCheckBox.setChecked(false);
                }
            }
        });
        bpm.addTextChangedListener(new TextValidator(bpm) {
            @Override public void validate(TextView textView, String text) {
                /* Validation code here */
                boolean b = Pattern.matches("\\d+", text);

                if (b) {
                    bpmCheckBox.setChecked(true);
                } else {
                    bpm.setError("Please enter a non-negative integer");
                    bpmCheckBox.setChecked(false);
                }
            }
        });
        date.addTextChangedListener(new TextValidator(date) {
            @Override public void validate(TextView textView, String text) {
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                /* Validation code here */
                try {
                    LocalDate.parse(text, dtf);
                    dateCheckBox.setChecked(true);
                } catch (DateTimeParseException e) {
                    date.setError("Please a valid time in the format hh:mm");
                    dateCheckBox.setChecked(false);
                }
            }
        });
        time.addTextChangedListener(new TextValidator(time) {
            @Override public void validate(TextView textView, String text) {
                /* Validation code here */
                if (validTime(text)) {
                    timeCheckBox.setChecked(true);
                } else {
                    time.setError("Please a valid time in the format hh:mm");
                    timeCheckBox.setChecked(false);
                }
            }
        });


    }

    private Boolean validTime(String string) {
        Matcher m = Pattern.compile("(\\d\\d):(\\d\\d)").matcher(string);

        if (m.matches()) {
            m.reset();
            if (m.find()) {
                int hour =Integer.parseInt(m.group(1));
                int minute = Integer.parseInt(m.group(2));

                if ((hour < 0 || hour > 24) || (minute < 0 || minute > 60)) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
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


    private Boolean errorOrEmpty(EditText et) {
        return (et.getError() != null) || (et.getText().length() == 0);
    }
    private Boolean checkValidations() {

        if(errorOrEmpty(systolic) || errorOrEmpty(diastolic) || errorOrEmpty(bpm) ||
            errorOrEmpty(date) || errorOrEmpty(time)){
            Toast.makeText(mContext,"Fix errors in the fields" ,Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    /** Called when the user taps the Send button */
    public void returnResult() {
        if (!checkValidations()) return;

        setStats();
        Log.d(TAG, "returnResult: class:"+cardioStats.toString());
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        updateIntentStats(intent);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
