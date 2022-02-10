package com.evg_ivanoff.valutes.models;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class DailyJSON {
    @SerializedName("Date")
    public String date;
    @SerializedName("PreviousDate")
    public String previousDate;
    @SerializedName("PreviousURL")
    public String previousURL;
    @SerializedName("Timestamp")
    public String timestamp;
    @SerializedName("Valute")
    public Map<String, Valute> valutes;
//    public Valutes valutes;
}
