package com.example.erick.aplicacionforestal;

public class SimpsonModel {
    private String dias, lon, vol, unidad;

    public SimpsonModel() {
    }

    public SimpsonModel(String dias, String lon, String vol, String unidad) {
        this.dias = dias;
        this.lon = lon;
        this.vol = vol;
        this.unidad = unidad;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
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

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }
}
