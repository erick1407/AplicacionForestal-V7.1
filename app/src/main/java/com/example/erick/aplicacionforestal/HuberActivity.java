package com.example.erick.aplicacionforestal;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class HuberActivity extends AppCompatActivity {
    //dialogo material
    Button btnok;
    android.app.Dialog dialogoError;
    ImageView imgClose;
    TextView mensajeTV, ttituloTV;

    EditText dia1, dia2, lon;
    TextView vol;
    String fecha, hora;
    String unidad;
    String diame1, diame2, longi, volu;
    String nombreInforme;
    String d1, d2, lo;

    EditText editTextNumHu, editTextContraHu;
    String matriHu, passdHu;
    String numHu, contraHu;

    private TemplatePDFHuber templatePDF;
    MaterialSpinner spinnerUnidadesHu;
    private String[] header ={"Diámetro 1", "Diámetro 2", "Longitud de la Troza", "Volumen"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_huber);


        //material dialog
        dialogoError= new android.app.Dialog(this);
        btnok = (Button) findViewById(R.id.btn_botonOk);
        imgClose = (ImageView) findViewById(R.id.img_close);

        //Casting Variables de EditText
        dia1 = (EditText) findViewById(R.id.editTextD1);
        dia2 = (EditText) findViewById(R.id.editTextD2);
        lon = (EditText) findViewById(R.id.editTextLN);

        //Casting Variables de TextView
        vol = (TextView) findViewById(R.id.textViewLon);

        //Datos de Fecha y Hora tomadas del Sistema del Telefono
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        fecha = simpleDateFormat.format(date);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String dataTime = sdf.format(new Date());
        hora = dataTime;

        spinnerUnidadesHu = (MaterialSpinner) findViewById(R.id.spinnerunidaesHuber);
        List<String> unidades = new ArrayList<String>();
        unidades.add("Centímetros");
        unidades.add("Decímetros");
        unidades.add("Metros");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unidades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnidadesHu.setAdapter(adapter);

        DBController dbController = new DBController(this);
        SQLiteDatabase sqLiteDatabase = dbController.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Persona", null);
        if (cursor.moveToFirst()){
            matriHu = cursor.getString(2);
            passdHu = cursor.getString(3);
        }

        readName();
    }


    private void showDialogErrorCamos() {
        dialogoError.setContentView(R.layout.error_validacion);
        imgClose=(ImageView) dialogoError.findViewById(R.id.img_close);
        btnok=(Button) dialogoError.findViewById(R.id.btn_botonOk);
        ttituloTV=(TextView)dialogoError.findViewById(R.id.tituloTv);
        mensajeTV=(TextView)dialogoError.findViewById(R.id.mensajeTv);

        dialogoError.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogoError.show();

    }

    private void showDialogErrorUM() {
        dialogoError.setContentView(R.layout.error_unidades_medida);
        imgClose=(ImageView) dialogoError.findViewById(R.id.img_close);
        btnok=(Button) dialogoError.findViewById(R.id.btn_botonOk);
        ttituloTV=(TextView)dialogoError.findViewById(R.id.tituloTv);
        mensajeTV=(TextView)dialogoError.findViewById(R.id.mensajeTv);

        dialogoError.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogoError.show();

    }

    public void cerrardialogo(View view){
        dialogoError.cancel();
    }

    //Metodo para Realizar Calculo
    public void calcularHu(View view){
        if (spinnerUnidadesHu.getSelectedItem().toString().trim().equals("Unidades de Medida")){
            //Dialog.show(this,"Error", "Debe Seleccionar una Unidad de Medida");
            showDialogErrorUM();
        }else{
            if (dia1.getText().toString().isEmpty()||dia2.getText().toString().isEmpty()||lon.getText().toString().isEmpty()){
                //Dialog.show(this,"Ups","Hay Campos Vacios");
                showDialogErrorCamos();
            }else{
                unidad = spinnerUnidadesHu.getSelectedItem().toString();
                String unidadF = null;
                if (unidad.equals("Centímetros")){
                    unidadF = "cm";
                }else
                if (unidad.equals("Decímetros")){
                    unidadF = "dm";
                }
                else
                if (unidad.equals("Metros")){
                    unidadF = "m";
                }
                d1 = dia1.getText().toString();
                d2 = dia2.getText().toString();
                lo = lon.getText().toString();
                double diam1 = Double.parseDouble(d1);
                double diam2 = Double.parseDouble(d2);
                double longi = Double.parseDouble(lo);

                double resultado = (Math.PI*Math.pow(diam1 + diam2, 2) * longi)/16;

                volu = String.format("%.4f", resultado);

                Dialog.show(this,"Resultado", "Volumen: "+ String.format("%.4f", resultado) +" "+ unidadF +"3", R.drawable.sucess);

                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Generar PDF");
                builder.setIcon(R.drawable.pdf);
                builder.setMessage("¿Desea generar archivo PDF de los resultados?");
                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        // SI LOS PERMISOS NO ESTAN ACTIVOS

                        if ((checkSelfPermission(WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                            //Toast.makeText(getApplicationContext(), "Permisos desactivados, debe activar los permisos para poder generar el documento PDF.", Toast.LENGTH_LONG).show();
                            // Dialog.show(HuberActivity.this, "Permisos desactivados", "Debe activar los permisos para poder generar el documento PDF.");

                            AlertDialog.Builder builder2 = new AlertDialog.Builder(HuberActivity.this);
                            // builder.setTitle("Regresar");
                            builder2.setIcon(R.drawable.fail);
                            builder2.setMessage("Permisos desactivados, debe activar los permisos para poder generar el documento PDF." + "\n" + "¿Desea configurar los permisos de almacenamiento?");
                            builder2.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                }
                            });
                            builder2.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            AlertDialog dialog2 = builder2.create();
                            dialog2.show();


                            //SI LOS PERMISOS SI ESTAN ACTIVOS
                        } else {
                            confPass();
                        }
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clear();
                        dialog.cancel();
                        addHuber();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }

    }

    private void readName(){
        DBController dbController = new DBController(this);
        SQLiteDatabase sqLiteDatabase = dbController.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Persona", null);
        if (cursor.moveToFirst()){
            nombreInforme = cursor.getString(1);
        }
    }

    public void addHuber(){
        DBController dbController = new DBController(this);
        HashMap<String, String> queryValues = new HashMap<String, String>();
        queryValues.put("diametro1", d1);
        queryValues.put("diametro2", d2);
        queryValues.put("longitud", lo);
        queryValues.put("volumen", volu);
        queryValues.put("unidad", unidad);
        queryValues.put("usuario", nombreInforme);
        queryValues.put("fecha", fecha);
        queryValues.put("hora", hora);
        dbController.insertHuber(queryValues);
    }

    public void visualizarPDF(){
        templatePDF = new TemplatePDFHuber(getApplicationContext());
        templatePDF.openDocument();
        templatePDF.addMetaData("Resultados","Cubicacion");
        templatePDF.addImage();
        templatePDF.addParagraph("\n");
        templatePDF.addParagraph("\n");
        templatePDF.addParagraph("\n");
        templatePDF.addTitles("Método de Huber","Cálculo del volumen de un fuste por la fórmula de Huber modificada.",fecha, hora);
        templatePDF.addParagraph("Cálculos realizados por: " + nombreInforme);
        templatePDF.createTable(header,getClients());
        templatePDF.addParagraphResu("ATENTAMENTE");
        templatePDF.addParagraphCenter("___________________________________________________________");
        templatePDF.addParagraphCenter(nombreInforme);
        templatePDF.closeDocument();
        templatePDF.viewPDF();
        finish();
    }

    private ArrayList<String[]> getClients(){
        unidad = spinnerUnidadesHu.getSelectedItem().toString();
        String unidadF = null;
        if (unidad.equals("Centímetros")){
            unidadF = "cm";
        }else
        if (unidad.equals("Decímetros")){
            unidadF = "dm";
        }
        else
        if (unidad.equals("Metros")){
            unidadF = "m";
        }
        diame1 = dia1.getText().toString();
        diame2 = dia2.getText().toString();
        longi = lon.getText().toString();
        ArrayList<String[]>rows = new ArrayList<>();
        rows.add(new String[]{diame1 + " " +unidadF,diame2 +" "+unidadF,longi+" "+unidadF,volu+" "+unidadF+"3"});
        return rows;
    }
    public void clear(){
        dia1.getText().clear();
        dia2.getText().clear();
        lon.getText().clear();
        spinnerUnidadesHu.setSelection(0,true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_huber, menu);
        return true;
    }

    public void regiHuber(MenuItem menuItem){
        startActivity(new Intent(this, ResHubActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.warning);
        builder.setTitle("Regresar al Menu");
        builder.setMessage("¿Desea regresar al menu principal y no realizar el cálculo?");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void confPass(){
        LayoutInflater inflater = LayoutInflater.from(HuberActivity.this);
        View subView = inflater.inflate(R.layout.dialog_huber, null);
        editTextNumHu = (EditText) subView.findViewById(R.id.editTextCNHuber);
        editTextContraHu = (EditText)subView.findViewById(R.id.editTextCPHuber);
        final ImageView subImageView = (ImageView)subView.findViewById(R.id.image);

        Drawable drawable = getResources().getDrawable(R.drawable.password);
        subImageView.setImageDrawable(drawable);
        editTextNumHu.setText(matriHu);

        numHu = editTextNumHu.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar contraseña");
        //builder.setMessage("Debe confirmar contraseña para poder generar el PDF");
        builder.setIcon(R.drawable.login);
        builder.setView(subView);
        AlertDialog alertDialog = builder.create();

        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                consulta();
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                //Toast.makeText(HuberActivity.this, "Cancel", Toast.LENGTH_LONG).show();
            }
        });

        builder.show();
    }

    public void consulta(){
        contraHu = editTextContraHu.getText().toString();
        if (numHu.equals(matriHu) && contraHu.equals(passdHu)){
            visualizarPDF();
            addHuber();
            Toast.makeText(getApplicationContext(), "PDF Generado Correctamente", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
        }
    }
}

