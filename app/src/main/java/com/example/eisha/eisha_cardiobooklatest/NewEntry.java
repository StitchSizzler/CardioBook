package com.example.eisha.eisha_cardiobooklatest;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NewEntry extends AppCompatActivity {
    private EditText date;
    private EditText time;
    private EditText systolicPressure;
    private EditText diastolicPressure;
    private EditText heartRate;
    private EditText comment;

    private String dateText;
    private String timeText;
    private int systolicText;
    private int diastolicText;
    private int heartText;
    private String commentText;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_entry);
        date = (EditText) findViewById(R.id.date);
        time  = (EditText) findViewById(R.id.time);
        systolicPressure = (EditText) findViewById(R.id.systolicPressure);
        diastolicPressure = (EditText) findViewById(R.id.diastolicPressure);
        heartRate = (EditText) findViewById(R.id.heartRate);
        comment = (EditText) findViewById(R.id.comment);

        Button saveButton = (Button) findViewById(R.id.save);
        Button cancelButton = (Button) findViewById(R.id.cancel);

        saveButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                dateText = date.getText().toString();
                if(TextUtils.isEmpty(dateText)) {date.setError("Please enter the date");}
                timeText = time.getText().toString();
                if(TextUtils.isEmpty(timeText)) {time.setError("Please enter the time");}

                //heartText = Integer.parseInt(heartRate.getText().toString());

                if(systolicPressure.getText().length() == 0) {
                    systolicPressure.setError("Please enter systolic pressure");
                } else {
                    systolicText = Integer.parseInt(systolicPressure.getText().toString());
                }
                if(diastolicPressure.getText().length() == 0) {
                    diastolicPressure.setError("Please enter diastolic pressure");
                } else {
                    diastolicText = Integer.parseInt(diastolicPressure.getText().toString());

                }
                if(heartRate.getText().length() == 0) {
                    heartRate.setError("Please enter heart rate");
                    return;
                } else {
                    heartText = Integer.parseInt(heartRate.getText().toString());

                }
                commentText = comment.getText().toString();



                importantEntry anEntry = new importantEntry(dateText, timeText, systolicText,
                        diastolicText, heartText, commentText);

                Bundle result = new Bundle();
                Intent returnIntent = new Intent(NewEntry.this, MainBook.class);
                result.putSerializable("putresult", anEntry);
                returnIntent.putExtra("result", result);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewEntry.this, MainBook.class);
                startActivity(intent);
            }
        });
    }
}
