package com.example.eisha.eisha_cardiobooklatest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import static android.os.FileObserver.DELETE;

public class Entry extends AppCompatActivity {
    private EditText date;
    private EditText time;
    private EditText systolicPressure;
    private EditText diastolicPressure;
    private EditText heartRate;
    private EditText comment;

    private int systolicText;
    private int diastolicText;
    private String dateText;
    private String timeText;
    private int heartText;
    private String commentText;


    //boolean changed = false;  // decide if canges need to be saved

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        Button doneButton = (Button) findViewById(R.id.done);
        Button cancelButton = (Button) findViewById(R.id.cancel);
        Button deleteButton = (Button) findViewById(R.id.delete);

        date = (EditText) findViewById(R.id.date);
        time  = (EditText) findViewById(R.id.time);
        systolicPressure = (EditText) findViewById(R.id.systolicPressure);
        diastolicPressure = (EditText) findViewById(R.id.diastolicPressure);
        heartRate = (EditText) findViewById(R.id.heartRate);
        comment = (EditText) findViewById(R.id.comment);

        final importantEntry anEntry = (importantEntry) getIntent().getBundleExtra("putedit").getSerializable("edit");
        date.setText(anEntry.getDate());
        time.setText(anEntry.getTime());
        systolicPressure.setText(Integer.toString(anEntry.getSystolicPressure()));
        diastolicPressure.setText(Integer.toString(anEntry.getDiastolicPressure()));
        heartRate.setText(Integer.toString(anEntry.getHeartRate()));
        comment.setText(anEntry.getComment());


        systolicText = Integer.parseInt(systolicPressure.getText().toString());
        diastolicText = Integer.parseInt(diastolicPressure.getText().toString());


        if(systolicText<90 || systolicText>140){
            systolicPressure.setTextColor(Color.parseColor("#ffd81b60"));
        }
        if(diastolicText<60 || diastolicText>90){
            diastolicPressure.setTextColor(Color.parseColor("#ffd81b60"));
        }

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deleteIntent = new Intent(Entry.this, MainBook.class);
                setResult(DELETE, deleteIntent);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Entry.this, MainBook.class);
                startActivity(intent);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent doneIntent = new Intent(Entry.this, MainBook.class);
                /*String dateText = date.getText().toString();
                String timeText = time.getText().toString();
                int systolicText = Integer.parseInt(systolicPressure.getText().toString());
                int diastolicText = Integer.parseInt(diastolicPressure.getText().toString());
                int heartText = Integer.parseInt(heartRate.getText().toString());
                String commentText = comment.getText().toString();*/
                int save = 1;

                // format reference: https://stackoverflow.com/questions/17416595/date-validation-in-android
                dateText = date.getText().toString();
                if(TextUtils.isEmpty(dateText)) {
                    date.setError("Please enter the date");
                    save = 0;
                }
                if (dateText== null || !dateText.matches("^([0-9]{4})-(0[1-9]|1[0-2])-(1[0-9]|0[1-9]|3[0-1]|2[1-9])$")) {
                    date.setError("Invalid date format");
                    save = 0;
                }
                SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
                try {
                    format.parse(dateText);
                }catch (ParseException e){
                    date.setError("Invalid date format");
                    save = 0;
                }
                timeText = time.getText().toString();
                if(TextUtils.isEmpty(timeText)) {
                    time.setError("Please enter the time");
                    save = 0;
                }
                // Learned the pattern matching stuff by trial and error. Kind of proud :)
                if (timeText== null || (!timeText.matches("^([0-1]+[0-9]):([0-5]+[0-9])$")) && !timeText.matches("^([0-2]+[0-3]):([0-5]+[0-9])$")) {
                    time.setError("Invalid time format");
                    save = 0;
                }
                SimpleDateFormat format1=new SimpleDateFormat("hh:mm");
                try {
                    format1.parse(timeText);
                }catch (ParseException e){
                    time.setError("Invalid time format");
                    save = 0;
                }
                if(systolicPressure.getText().length() == 0) {
                    systolicPressure.setError("Please enter systolic pressure");
                    save = 0;
                } else {
                    systolicText = Integer.parseInt(systolicPressure.getText().toString());
                }
                if(diastolicPressure.getText().length() == 0) {
                    diastolicPressure.setError("Please enter diastolic pressure");
                    save = 0;
                } else {
                    diastolicText = Integer.parseInt(diastolicPressure.getText().toString());
                }
                if(heartRate.getText().length() == 0) {
                    heartRate.setError("Please enter heart rate");
                    save = 0;
                    return;
                } else {
                    heartText = Integer.parseInt(heartRate.getText().toString());

                }
                commentText = comment.getText().toString();


                if(save == 1) {
                    importantEntry anEntry = new importantEntry(dateText, timeText, systolicText,
                            diastolicText, heartText, commentText);
                    Bundle result = new Bundle();
                    result.putSerializable("putresult", anEntry);
                    doneIntent.putExtra("result", result);
                    setResult(0, doneIntent);
                    finish();
                }

            }

        });
    }
}
