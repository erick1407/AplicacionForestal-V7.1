package com.example.erick.aplicacionforestal;

import android.support.v7.app.AlertDialog;
import android.content.Context;

public class Dialog {
    public static void show(Context context, String title, String menssage, int icon){
        AlertDialog.Builder dialogo = new AlertDialog.Builder(context);
        dialogo.setTitle(title);
        dialogo.setIcon(icon);
        dialogo.setMessage(menssage);
        dialogo.setPositiveButton("Aceptar",null);
        AlertDialog msn = dialogo.create();
        msn.show();
    }
}
