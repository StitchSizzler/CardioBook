package com.example.eisha.eisha_cardiobooklatest;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a single Entry
 * Used by adapter
 */

public class importantEntry implements Serializable {

    private String date;
    private String time;
    private Integer systolicPressure;
    private Integer diastolicPressure;
    private Integer heartRate;
    private String comment;


    public importantEntry(String date, String time, Integer systolicPressure, Integer diastolicPressure, Integer heartRate, String comment) {

        this.date = date;
        this.time = time;
        this.systolicPressure = systolicPressure;
        this.diastolicPressure = diastolicPressure;
        this.heartRate = heartRate;
        this.comment = comment;
    }

    // getter methods
    public String getDate() {return this.date;}
    public String getTime() {return this.time;}
    public Integer getSystolicPressure() {return this.systolicPressure;}
    public Integer getDiastolicPressure() {return this.diastolicPressure;}
    public Integer getHeartRate() {return this.heartRate;}
    public String getComment() {return this.comment;}


    //Reference from: https://stackoverflow.com/questions/15776661/android-spinner-showing-object-reference-instead-of-string
    @Override
    public String toString() {
        String sysEnd = "";
        String diaEnd = "";

        // check for abnormal pressures
        if(systolicPressure<90 || systolicPressure>140){
            sysEnd = "!";
        }
        if(diastolicPressure<60 || diastolicPressure>90){
            diaEnd = "!";
        }

        // return what to display in listview
        return date+" | Systolic: "+Integer.toString(systolicPressure)+ " mm Hg"+sysEnd+
                " | Diastolic: "+Integer.toString(diastolicPressure)+ " mm Hg" +diaEnd+ " | HeartRate: "+Integer.toString(heartRate) +" per minute";
    }

}
