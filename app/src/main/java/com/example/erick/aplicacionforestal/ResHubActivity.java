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

public class ResHubActivity extends AppCompatActivity {
    private RecyclerView recyclerViewHu;
    private HuberAdaptador huberAdaptador;
    DBController controller = new DBController(this);
    ProgressDialog prgDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_hub);
        comprobarConexion();

        recyclerViewHu = (RecyclerView) findViewById(R.id.recyclerHuber);
        recyclerViewHu.setLayoutManager(new LinearLayoutManager(this));
        llenar();
        //llenado();
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Sincronizando registros de SQLite con Servidor MariaDB. Espere por favor...!!!");
        prgDialog.setCancelable(false);
    }

    private void llenar() {
        huberAdaptador = new HuberAdaptador(controller.mostrarRecordsHu());
        recyclerViewHu.setAdapter(huberAdaptador);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_upload, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //When Sync action button is clicked
        if (id == R.id.Upload) {
            //Sync SQLite DB data to remote MySQL DB
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Desea enviar los registros al Servidor?");
            builder.setTitle("Confirmar");
            builder.setIcon(R.drawable.sync);
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
            return true;
        }
        if (id == R.id.Delete){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("¿Desea eliminar los registros guardados?");
            builder.setIcon(R.drawable.question);
            builder.setTitle("Eliminar registros");
            builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    limpiarHuber();
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void limpiarHuber() {
        DBController dbController = new DBController(this);
        SQLiteDatabase sqLiteDatabase = dbController.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM Huber");
        sqLiteDatabase.close();
        //llenado();
        llenar();
        Dialog.show(this, "Cálculos eliminados","Los cálculos han sido eliminados", R.drawable.sucess);
    }

    public void comprobarConexion(){
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info_wifi = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo info_datos = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (String.valueOf(info_wifi.getState()).equals("CONNECTED")){
            Snackbar snackbar = Snackbar.make(findViewById(R.id.layoutHu),"Estas conectado al WIFI, UD puede enviar el registro al Servidor", Snackbar.LENGTH_LONG).setAction("Action", null);
            View sbView = snackbar.getView();
            sbView.setBackgroundColor(Color.parseColor("#008744"));
            snackbar.show();
        }else{
            if(String.valueOf(info_datos.getState()).equals("CONNECTED")) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.layoutHu),"Estas conectado a los Datos Moviles del telefono, le recomiendo usar WIFI", Snackbar.LENGTH_LONG).setAction("Action", null);
                View sbView = snackbar.getView();
                sbView.setBackgroundColor(Color.parseColor("#FFA700"));
                snackbar.show();
            }else{

                Snackbar snackbar = Snackbar.make(findViewById(R.id.layoutHu),"Ups!, No estas conectado a Internet", Snackbar.LENGTH_LONG).setAction("Action", null);
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
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.layoutHu),"Las credenciales no coinciden", Snackbar.LENGTH_LONG).setAction("Action", null);
                            View sbView = snackbar.getView();
                            sbView.setBackgroundColor(Color.RED);
                            snackbar.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.layoutHu),"No estas conectado a Internet o el Servidor no responde, intente mas tarde.", Snackbar.LENGTH_LONG).setAction("Action", null);
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
        ArrayList<HashMap<String, String>> userList =  controller.getAllUsers();
        if(userList.size()!=0){
            if(controller.dbSyncCount() != 0){
                prgDialog.show();
                params.put("usersJSON", controller.composeJSONfromSQLite());
                client.post(Conexion.URL_WEB_SERVICES+"insertHuber.php", params, new AsyncHttpResponseHandler() {
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
                                controller.updateSyncStatus(obj.get("id").toString(),obj.get("status").toString());
                            }
                            Dialog.show(ResHubActivity.this,"Sincronización completada!", "Los datos ya estan en el Servidor", R.drawable.sucess);
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Dialog.show(ResHubActivity.this,"Error", "Ocurrió un error [¡La estructura del servidor JSON podría no ser válida]!", R.drawable.fail);
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Throwable error, String content) {
                        // TODO Auto-generated method stub
                        prgDialog.hide();
                        if(statusCode == 404){
                            Dialog.show(ResHubActivity.this,"Error 404", "Recurso solicitado no encontrado", R.drawable.errorcu);
                        }else if(statusCode == 500){
                            Dialog.show(ResHubActivity.this,"Error en el Servidor", "Algo anda mal en el Servidor", R.drawable.servererror);
                        }else{
                            Dialog.show(ResHubActivity.this, "Error", "¡Ocurrió un error Inesperado!, Tal vez el dispositivo podría no estar conectado a Internet o se perdio la Conexion a Internet", R.drawable.nowifi);
                        }
                    }
                });
            }else{
                Dialog.show(ResHubActivity.this,"Nada para enviar", "No es necesario SQLite y MariaDB ya estan sincronizados!", R.drawable.databasesy);
            }
        }else{
            Dialog.show(ResHubActivity.this,"No hay registros en SQLite", "Guarde un registro para poder sincronizar", R.drawable.databaseem);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HuberActivity.class));
        finish();
    }
}