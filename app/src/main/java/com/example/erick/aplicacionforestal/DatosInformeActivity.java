package com.example.erick.aplicacionforestal;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DatosInformeActivity extends AppCompatActivity {
    private EditText editTextNombre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_informe);
        editTextNombre = (EditText) findViewById(R.id.editTextNombre);
        readName();
    }

    private void readName(){
        DBController dbController = new DBController(this);
        SQLiteDatabase sqLiteDatabase = dbController.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Persona", null);
        if (cursor.moveToFirst()){
            String name = cursor.getString(1);
            editTextNombre.setText(name);
        }
    }

    public void updateNombre(View view){
        if (editTextNombre.getText().toString().isEmpty()){
            Snackbar snackbar = Snackbar.make(view,"Ingrese nombre de quien realizara los cálculos", Snackbar.LENGTH_LONG).setAction("Action", null);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.RED);
            snackbar.show();
        }else {
            String nombre = editTextNombre.getText().toString();
            String nombrePattern = "[a-zA-Z]+\\.+[a-z A-Zá-ñÑ]+";
            if (nombre.matches(nombrePattern) && nombre.length()>0){
                update();
                onBackPressed();
                Toast.makeText(this, "Los datos del informe se han modificado", Toast.LENGTH_SHORT).show();
            }else {
                Dialog.show(this,"Ups...!", "El nombre o la abreviatura del grado de estudios son incorrectos\n\nEjemplo:\nIng. Juan Salas Solo", R.drawable.fail);
            }
        }
    }

    private void update(){
        DBController dbController = new DBController(this);
        SQLiteDatabase sqLiteDatabase = dbController.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Nombre", editTextNombre.getText().toString());
        sqLiteDatabase.update("Persona", contentValues, "IDUser = 1", null);
        sqLiteDatabase.close();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(DatosInformeActivity.this, MainActivity.class));
        finish();
    }
}
