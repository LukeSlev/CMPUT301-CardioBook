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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Activity to add/edit CardioBook Stats
 *
 * This activity is used to let add or update their cadio stats in the CardioBook. Comment is the
 *      only field that is allowed to be blank, and is at most 20 characters. The other fields
 *      must be inputted correctly or you will not be allowed to submit an entry. Heart rate is
 *      referred to as Beats per minute. As fields are entered correctly, the checkboxes beside
 *      them will get checked, indicating they are ready to be added.
 *
 * @author Luke Slevinsky
 */
public class AddCardioStatActivity extends AppCompatActivity {
    public static final String TAG = AddCardioStatActivity.class.getSimpleName();
    public static final String SYSTOLIC = "SYSTOLIC";
    public static final String DIASTOLIC = "DIASTOLIC";
    public static final String DATETIME = "DATETIME";
    public static final String COMMENT = "COMMENT";
    public static final String BPM = "BPM";

    private Context mContext;
    private int requestType;
    private int position;
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


    /**
     * onCreate method for AddCardioStatActivity. In this method, the initial setup for the activity
     *      is done. The view elements are assigned to variables. Based on if the Activity is
     *      started in ADD mode or EDIT mode, different values are populated; if it is an EDIT mode
     *      then the view is prepoulated with the values of the cardio stat that is being editted.
     *      As well, the textEdit validators are set up.
     */
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
                position=0;
                submit.setText(R.string.submit);
                break;
            case MainActivity.EDIT:
                position = intent.getIntExtra(CardioListAdapter.POSITION,0);
                cardioStats = MainActivity.setStats(intent);
                Log.d(TAG, "onCreate: "+cardioStats.toString());

                // For EDITs set up the view with the current cardioStat values
                systolic.setText(String.format("%d", cardioStats.getSystolicPressure()));
                diastolic.setText(String.format("%d", cardioStats.getDiastolicPressure()));
                bpm.setText(String.format("%d", cardioStats.getBpm()));
                date.setText(cardioStats.getDateTime().format(dateFormat));
                time.setText(cardioStats.getDateTime().format(timeFormat));
                comment.setText(cardioStats.getComment());

                submit.setText(R.string.update);
                break;
            default:
                returnResult();
        }

        // Setup text VALIDATION

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

    /**
     * Method to validate Time entries in the form of hh:mm
     *
     * @param string The value to validate
     * @return The result if string is valid or not
     */
    private Boolean validTime(String string) {
        Matcher m = Pattern.compile("(\\d\\d):(\\d\\d)").matcher(string);

        if (m.matches()) {
            m.reset();
            if (m.find()) {
                int hour =Integer.parseInt(m.group(1));
                int minute = Integer.parseInt(m.group(2));

                if ((hour < 0 || hour > 23) || (minute < 0 || minute > 59)) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method to update Activities cardioStat dataset; this is the cardioStat being created or edited
     */
    protected void updateStats() {
        LocalDateTime dateTime = LocalDateTime.parse(date.getText().toString() + " " + time.getText().toString(), CardioListAdapter.dateFormatter);
        debugStats(dateTime);
        cardioStats.setSystolicPressure(Integer.parseInt(systolic.getText().toString()));
        cardioStats.setDiastolicPressure(Integer.parseInt(diastolic.getText().toString()));
        cardioStats.setBpm(Integer.parseInt(bpm.getText().toString()));
        cardioStats.setDateTime(dateTime);
        cardioStats.setComment(comment.getText().toString());

    }

    /**
     * Method to update the passed in intent from the cardioStats variable
     *
     * @param intent The intent to update with data
     * @param cs The CardioStats instance to pull data off of
     */
    public static void updateIntentStats(Intent intent, CardioStats cs) {
        Log.d(TAG, "updateIntentStats: "+cs.toString());
        intent.putExtra(SYSTOLIC, cs.getSystolicPressure());
        intent.putExtra(DIASTOLIC, cs.getDiastolicPressure());
        intent.putExtra(BPM, cs.getBpm());
        intent.putExtra(DATETIME, cs.getDateTime().toString());
        intent.putExtra(COMMENT, cs.getComment());
    }

    /**
     * Debugging method to help see what is currently inputted in the view.
     */
    private void debugStats(LocalDateTime dateTime) {
        Log.d(TAG, "setStats: sys:" + Integer.parseInt(systolic.getText().toString()));
        Log.d(TAG, "setStats: dia:" + diastolic.getText().toString());
        Log.d(TAG, "setStats: bpm:" + bpm.getText().toString());
        Log.d(TAG, "setStats: datetime:" + dateTime.toString());
        Log.d(TAG, "setStats: comment:" + comment.getText().toString());
    }


    /**
     * Helper method to determine whether an EditText field is either empty of invalid
     *
     * @param et the EditText field to validate
     * @return The result of whether the field is either empty or invalid
     */
    private Boolean errorOrEmpty(EditText et) {
        return (et.getError() != null) || (et.getText().length() == 0);
    }

    /**
     * Method that is used to validate user input in the view
     *
     * @return The result of whether the user inputs are valid
     */
    private Boolean checkValidations() {

        if(errorOrEmpty(systolic) || errorOrEmpty(diastolic) || errorOrEmpty(bpm) ||
            errorOrEmpty(date) || errorOrEmpty(time)){
            Toast.makeText(mContext,"Fix errors in the fields" ,Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    /**
     * Method that finishes the activity and returns to MainActivity with the new information
     */
    public void returnResult() {
        // Guard statement to ensure the user input is valid
        if (!checkValidations()) return;

        updateStats();
        Log.d(TAG, "returnResult: class:"+cardioStats.toString());
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        updateIntentStats(intent, cardioStats);
        intent.putExtra(CardioListAdapter.POSITION,position);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
