package com.example.converter;

public class Valute {
    int numCode;
    String charCode;
    int nominal;
    String name;
    double value;
    double vunitRate;
    public Valute(int numCode, String charCode, int nominal, String name, double value, double vunitRate){
        this.numCode = numCode;
        this.charCode = charCode;
        this.nominal = nominal;
        this.name = name;
        this.value = value;
        this.vunitRate = vunitRate;
    }
}
