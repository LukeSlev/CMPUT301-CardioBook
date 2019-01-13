package com.lslevins.lslevins_cardiobook;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by Luke Slevinsky on 2019-01-10.
 */

public class CardioStats {
    private LocalDateTime dateTime;
    private int systolicPressure;
    private int diastolicPressure;
    private int bpm;
    private String comment;

    // Constructors

    public CardioStats() { ;}

    public CardioStats(LocalDateTime dateTime, int sysPres, int diaPres, int bpm, String comment ) {
        setBpm(bpm);
        setComment(comment);
        setDateTime(dateTime);
        setDiastolicPressure(diaPres);
        setSystolicPressure(sysPres);
    }

    // Getters and Setters

    public LocalDateTime getDateTime() {
        return dateTime;
    }
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public int getSystolicPressure() {
        return systolicPressure;
    }
    public void setSystolicPressure(int systolicPressure) {
        this.systolicPressure = systolicPressure;
    }

    public int getDiastolicPressure() {
        return diastolicPressure;
    }
    public void setDiastolicPressure(int diastolicPressure) {
        this.diastolicPressure = diastolicPressure;
    }

    public int getBpm() {
        return bpm;
    }
    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Date " + getDateTime().toString() + " SP " + Integer.toString(getSystolicPressure())
                + " DP "  + Integer.toString(getDiastolicPressure()) + " BPM " + Integer.toString(getBpm());
    }

}
