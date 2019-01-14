package com.lslevins.lslevins_cardiobook;

import android.util.Log;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by Luke Slevinsky on 2019-01-10.
 */

public class CardioStats implements Serializable{
    private static final String TAG = MainActivity.class.getSimpleName();

    private LocalDateTime dateTime;
    private int systolicPressure;
    private int diastolicPressure;
    private int bpm;
    private String comment;

    private Boolean systolicFlag= false;
    private Boolean diastolicFlag= false;

    private int systolicLow= 90;
    private int systolicHigh= 140;
    private int diastolicLow= 60;
    private int diastolicHigh= 90;



    // Constructors

    public CardioStats() {}

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
        if (systolicPressure > systolicHigh || systolicPressure < systolicLow) {
            systolicFlag = true;
        } else {
            systolicFlag = false;
        }
    }

    public int getDiastolicPressure() {
        return diastolicPressure;
    }
    public void setDiastolicPressure(int diastolicPressure) {
        this.diastolicPressure = diastolicPressure;
        if (diastolicPressure > diastolicHigh || diastolicPressure < diastolicLow) {
            diastolicFlag = true;
        } else {
            diastolicFlag = false;
        }
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
                + " DP "  + Integer.toString(getDiastolicPressure()) + " BPM "
                + Integer.toString(getBpm()) + " Comment " + getComment();
    }

    public Boolean getSystolicFlag() {
        return systolicFlag;
    }

    public Boolean getDiastolicFlag() {
        return diastolicFlag;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!CardioStats.class.isAssignableFrom(obj.getClass())) {
            return false;
        }

        final CardioStats other = (CardioStats) obj;
        if (getSystolicPressure() != other.getSystolicPressure()) {
            return false;
        }
        if (getDiastolicPressure() != other.getDiastolicPressure()) {
            return false;
        }
        if (getBpm() != other.getBpm()) {
            return false;
        }
        if (!getDateTime().equals(other.getDateTime())) {
            return false;
        }
        if ((getComment() == null) ? (other.getComment() != null) : !this.getComment().equals(other.getComment())) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + getDiastolicPressure();
        hash = 53 * hash + getSystolicPressure();
        hash = 53 * hash + getBpm();
        hash = 53 * hash + (getComment() != null ? getComment().hashCode() : 0);
        hash = 53 * hash + (getDateTime().hashCode());

        return hash;
    }
}
