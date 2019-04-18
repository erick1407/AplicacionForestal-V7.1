package com.example.erick.aplicacionforestal;

public class HuberModel {
    private String dia1, dia2, lon, vol, uni;

    public HuberModel() {
    }

    public HuberModel(String dia1, String dia2, String lon, String vol, String uni) {
        this.dia1 = dia1;
        this.dia2 = dia2;
        this.lon = lon;
        this.vol = vol;
        this.uni = uni;
    }

    public String getDia1() {
        return dia1;
    }

    public void setDia1(String dia1) {
        this.dia1 = dia1;
    }

    public String getDia2() {
        return dia2;
    }

    public void setDia2(String dia2) {
        this.dia2 = dia2;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getVol() {
        return vol;
    }

    public void setVol(String vol) {
        this.vol = vol;
    }

    public String getUni() {
        return uni;
    }

    public void setUni(String uni) {
        this.uni = uni;
    }
}
