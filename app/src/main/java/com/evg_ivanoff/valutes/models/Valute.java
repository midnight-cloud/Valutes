package com.evg_ivanoff.valutes.models;

import com.google.gson.annotations.SerializedName;

public class Valute {
    @SerializedName("ID")
    public String id;
    @SerializedName("NumCode")
    public String numCode;
    @SerializedName("CharCode")
    public String charCode;
    @SerializedName("Nominal")
    public int nominal;
    @SerializedName("Name")
    public String name;
    @SerializedName("Value")
    public double value;
    @SerializedName("Previous")
    public double previous;


    public Valute(String id, String numCode, String charCode, int nominal, String name, double value, double previous) {
        this.id = id;
        this.numCode = numCode;
        this.charCode = charCode;
        this.nominal = nominal;
        this.name = name;
        this.value = value;
        this.previous = previous;
    }


    public String getCharCode() {
        return charCode;
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "ID - "+id+"; name - "+name+"; value - "+value;
    }
}
