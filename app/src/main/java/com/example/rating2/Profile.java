package com.example.rating2;

import android.os.Parcelable;

import java.io.Serializable;

public class Profile implements Serializable {
    private String name;
    private String SerialNumber;
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
}
