package com.example.erick.aplicacionforestal;

public class HeyerModel {
    private String dias, longs, vol, unidad;

    public HeyerModel() {
    }

    public HeyerModel(String dias, String longs, String vol, String unidad) {
        this.dias = dias;
        this.longs = longs;
        this.vol = vol;
        this.unidad = unidad;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public String getLongs() {
        return longs;
    }

    public void setLongs(String longs) {
        this.longs = longs;
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
