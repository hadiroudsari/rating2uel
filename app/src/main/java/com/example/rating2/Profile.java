package com.example.rating2;

import android.os.Parcelable;

import java.io.Serializable;

public class Profile implements Serializable {
    private String name;
    private String SerialNumber;
    private String opponent;
    private String duelTime;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        SerialNumber = serialNumber;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public String getDuelTime() {
        return duelTime;
    }

    public void setDuelTime(String duelTime) {
        this.duelTime = duelTime;
    }
}
