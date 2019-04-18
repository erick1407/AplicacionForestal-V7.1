package com.example.erick.aplicacionforestal;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText numerousuario, password, nombreInforme;
    private CheckBox checkBoxSP;
    private Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        numerousuario = (EditText) findViewById(R.id.editTextEmailLog);
        password = (EditText) findViewById(R.id.editTextPassLog);
        nombreInforme = (EditText) findViewById(R.id.editTextPerson);

        checkBoxSP = (CheckBox) findViewById(R.id.checkBoxShowP);

        session = new Session(this);
        if (session.loggedin()){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    public void login(final View view){
        if (numerousuario.getText().toString().isEmpty()){
            Snackbar snackbar = Snackbar.make(view,"Ingrese Número de Usuario", Snackbar.LENGTH_LONG).setAction("Action", null);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.RED);
            snackbar.show();
        }else{
            if (password.getText().toString().isEmpty()){
                Snackbar snackbar = Snackbar.make(view,"Ingrese su Contraseña", Snackbar.LENGTH_LONG).setAction("Action", null);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(Color.RED);
                snackbar.show();
            }else {
                if (nombreInforme.getText().toString().isEmpty()){
                    Snackbar snackbar = Snackbar.make(view,"Ingrese Nombre de quien Realizara los calculos", Snackbar.LENGTH_LONG).setAction("Action", null);
                    View sbView = snackbar.getView();
                    sbView.setBackgroundColor(Color.RED);
                    snackbar.show();
                }else {
                    String nombre = nombreInforme.getText().toString();
                    String nombrePattern = "[a-zA-Z]+\\.+[a-z A-Zá-ñÑ]+";
                    if (nombre.matches(nombrePattern)){
                        StringRequest request = new StringRequest(Request.Method.POST, Conexion.URL_WEB_SERVICES+"login.php",
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.contains("1")){
                                            addNameInfor();
                                            session.setLoggedin(true);
                                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                            finish();
                                        }else {
                                            Snackbar snackbar = Snackbar.make(view,"Usuario o Contraseña Incorrectos", Snackbar.LENGTH_LONG).setAction("Action", null);
                                            View sbView = snackbar.getView();
                                            sbView.setBackgroundColor(Color.RED);
                                            snackbar.show();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Snackbar snackbar = Snackbar.make(view,"No estas Conectado a Internet o el Servidor no Responde, Intente mas tarde.", Snackbar.LENGTH_LONG).setAction("Action", null);
                                View sbView = snackbar.getView();
                                sbView.setBackgroundColor(Color.RED);
                                snackbar.show();
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();
                                params.put("numerousuario", numerousuario.getText().toString());
                                params.put("password", password.getText().toString());
                                return  params;
                            }
                        };

                        Volley.newRequestQueue(this).add(request);
                    }else {
                        Dialog.show(this,"Ups...!", "El Nombre o la abreviatura del Grado de Estudios son Incorrectos \n\nEjemplo:\nIng. Juan Salas Solo\nó tambien si solo es un obrero usted:\nC. Juan Solo Salas", R.drawable.fail);
                    }
                }
            }
        }
    }

    private void addNameInfor(){
        DBController dbController = new DBController(this);
        SQLiteDatabase sqLiteDatabase = dbController.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Nombre", nombreInforme.getText().toString());
        contentValues.put("Numero", numerousuario.getText().toString());
        contentValues.put("Password", password.getText().toString());
        sqLiteDatabase.insert("Persona", null, contentValues);
        sqLiteDatabase.close();
    }

    public void showPass(View view){
        password = (EditText) findViewById(R.id.editTextPassLog);
        int cursor = password.getSelectionEnd();
        if (checkBoxSP.isChecked()){
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }else{
            password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        password.setSelection(cursor);
    }
}
