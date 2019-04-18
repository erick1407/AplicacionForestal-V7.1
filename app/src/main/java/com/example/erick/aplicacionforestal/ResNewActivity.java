package com.example.erick.aplicacionforestal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ResNewActivity extends AppCompatActivity {
    private RecyclerView recyclerViewN;
    private NewtonAdaptador newtonAdaptador;
    DBController controller = new DBController(this);
    ProgressDialog prgDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_new);
        comprobarConexion();
        //llenado();

        recyclerViewN = (RecyclerView) findViewById(R.id.recyclerNewton);
        recyclerViewN.setLayoutManager(new LinearLayoutManager(this));

        llenar();
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Sincronizando Registros de SQLite con Servidor MariaDB. Espere Por Favor...!!!");
        prgDialog.setCancelable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_updatene, menu);
        return true;
    }

    public void llenar(){
        DBController sqLiteHelper = new DBController(this);
        newtonAdaptador = new NewtonAdaptador(sqLiteHelper.mostrarRecordsNe());
        recyclerViewN.setAdapter(newtonAdaptador);
    }

    public void syncNe(MenuItem menuItem){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Desea Enviar los Registros al Servidor?");
        builder.setTitle("Confirmar");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                consultaU();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void deleteNe(MenuItem menuItem){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Desea Eliminar los Registros Guardados?");
        builder.setTitle("Eliminar Registros");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                limpiarNewton();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void limpiarNewton() {
        DBController dbController = new DBController(this);
        SQLiteDatabase sqLiteDatabase = dbController.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM Newton");
        sqLiteDatabase.close();
        //llenado();
        llenar();
        Dialog.show(this, "Cálculos Eliminados","Los Cálculos han sido eliminados", R.drawable.sucess);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, NewtonActivity.class));
        finish();
    }

    public void comprobarConexion(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info_wifi = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo info_datos = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (String.valueOf(info_wifi.getState()).equals("CONNECTED")){
            Snackbar snackbar = Snackbar.make(findViewById(R.id.layoutNe),"Estas Conectado al WIFI, UD Puede Enviar el Registro al Servidor", Snackbar.LENGTH_LONG).setAction("Action", null);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.parseColor("#008744"));
            snackbar.show();
        }else{
            if(String.valueOf(info_datos.getState()).equals("CONNECTED")) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.layoutNe),"Estas Conectado a los Datos Moviles del Telefono, Le Recomiendo usar WIFI", Snackbar.LENGTH_LONG).setAction("Action", null);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(Color.parseColor("#FFA700"));
                snackbar.show();
            }else{

                Snackbar snackbar = Snackbar.make(findViewById(R.id.layoutNe),"Ups!, No estas Conectado a Internet", Snackbar.LENGTH_LONG).setAction("Action", null);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(Color.parseColor("#D62D20"));
                snackbar.show();
            }
        }
    }

    public void consultaU(){
        String numer = "", passw = "";
        DBController dbController = new DBController(this);
        SQLiteDatabase sqLiteDatabase = dbController.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Persona", null);
        if (cursor.moveToFirst()){
            numer = cursor.getString(2);
            passw = cursor.getString(3);
        }

        final String numerousuario = numer;
        final String password = passw;
        StringRequest request = new StringRequest(Request.Method.POST, Conexion.URL_WEB_SERVICES+"login.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("1")){
                            syncSQLiteMySQLDB();
                        }else {
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.layoutNe),"Las Credenciales No Coinciden", Snackbar.LENGTH_LONG).setAction("Action", null);
                            View sbView = snackbar.getView();
                            sbView.setBackgroundColor(Color.RED);
                            snackbar.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.layoutNe),"No estas Conectado a Internet o el Servidor no Responde, Intente mas tarde.", Snackbar.LENGTH_LONG).setAction("Action", null);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(Color.RED);
                snackbar.show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("numerousuario", numerousuario);
                params.put("password", password);
                return  params;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }

    public void syncSQLiteMySQLDB(){
        //Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        ArrayList<HashMap<String, String>> userList =  controller.getAllNewton();
        if(userList.size()!=0){
            if(controller.dbSyncCountNe() != 0){
                prgDialog.show();
                params.put("usersJSON", controller.composeJSONfromSQLiteNe());
                client.post(Conexion.URL_WEB_SERVICES+"insertNewton.php", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        System.out.println(response);
                        prgDialog.hide();
                        try {
                            JSONArray arr = new JSONArray(response);
                            System.out.println(arr.length());
                            for(int i=0; i<arr.length();i++){
                                JSONObject obj = (JSONObject)arr.get(i);
                                System.out.println(obj.get("id"));
                                System.out.println(obj.get("status"));
                                controller.updateSyncStatusNe(obj.get("id").toString(),obj.get("status").toString());
                            }
                            Dialog.show(ResNewActivity.this,"Sincronización Completada!", "Los Datos ya estan en el Servidor", R.drawable.sucess);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Dialog.show(ResNewActivity.this,"Error", "Ocurrió un error [¡La Estructura del servidor JSON podría no ser válida]!", R.drawable.fail);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable error, String content) {
                        // TODO Auto-generated method stub
                        prgDialog.hide();
                        if(statusCode == 404){
                            Dialog.show(ResNewActivity.this,"Error 404", "Recurso Solicitado No Encontrado", R.drawable.errorcu);
                        }else if(statusCode == 500){
                            Dialog.show(ResNewActivity.this,"Error en el Servidor", "Algo anda mal en el Servidpr", R.drawable.servererror);
                        }else{
                            Dialog.show(ResNewActivity.this, "Error", "¡Ocurrió un error Inesperado!, Tal vez el dispositivo podría no estar conectado a Internet o se perdio la Conexion a Internet", R.drawable.nowifi);
                        }
                    }
                });
            }else{
                Dialog.show(ResNewActivity.this,"Nada para Enviar", "No es Necesario SQLite y MariaDB ya estan Sincronizados!", R.drawable.databasesy);
            }
        }else{
            Dialog.show(ResNewActivity.this,"No hay Registros en SQLite", "Guarde un Registro para poder Sincronizar", R.drawable.databaseem);
        }
    }
}
