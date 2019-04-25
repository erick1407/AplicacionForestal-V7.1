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

public class NewtonActivity extends AppCompatActivity {
    //dialogo material
    Button btnok;
    android.app.Dialog dialogoError;
    ImageView imgClose;
    TextView mensajeTV, ttituloTV;
    TextView mensajeTV_AD, ttituloTV_AD;

    EditText diam1,diam2,lon;
    String fecha, hora;
    String diame1, diame2, longi, volu;
    String unidad;
    String nombreInforme;
    MaterialSpinner spinnerNe;
    private TemplatePDFNewton templatePDF;
    private String[] header ={"Diámetro 1", "Diámetro 2", "Longitud de la Toza", "Volumen"};

    //Variables Simpson Dialog
    EditText editTextNumN, editTextContraN;
    String matriN, passdN;
    String numN, contraN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newton);

        // dialog material
        dialogoError= new android.app.Dialog(this);
        btnok = (Button) findViewById(R.id.btn_botonOk);
        imgClose = (ImageView) findViewById(R.id.img_close);

        diam1 = (EditText) findViewById(R.id.editTextD1N);
        diam2 = (EditText) findViewById(R.id.editTextD2N);
        lon = (EditText) findViewById(R.id.editTextLonN);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        fecha = simpleDateFormat.format(date);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String dataTime = sdf.format(new Date());
        hora = dataTime;

        spinnerNe = (MaterialSpinner) findViewById(R.id.spinnerunidaesNew);

        List<String> unidades = new ArrayList<String>();
        unidades.add("Centímetros");
        unidades.add("Decímetros");
        unidades.add("Metros");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unidades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNe.setAdapter(adapter);

        readName();

        DBController dbController = new DBController(this);
        SQLiteDatabase sqLiteDatabase = dbController.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Persona", null);
        if (cursor.moveToFirst()){
            matriN = cursor.getString(2);
            passdN = cursor.getString(3);
        }
    }

    private void readName() {
        DBController dbController = new DBController(this);
        SQLiteDatabase sqLiteDatabase = dbController.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Persona", null);
        if (cursor.moveToFirst()){
            nombreInforme = cursor.getString(1);
        }
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

    public void calcularNe(View view){
        if (spinnerNe.getSelectedItem().toString().trim().equals("Unidades de Medida")){
            //Dialog.show(this,"Error", "Debe Seleccionar una Unidad de Medida");
            showDialogErrorUM();
        }else{
            if (diam1.getText().toString().isEmpty()||diam2.getText().toString().isEmpty()||lon.getText().toString().isEmpty()){
                //Dialog.show(this,"Ups","Hay Campos Vacios");
                showDialogErrorCamos();
            }else {
                unidad = spinnerNe.getSelectedItem().toString();
                String unidadF = null;
                if (unidad.equals("Centímetros")){
                    unidadF = "cm";
                }else
                if (unidad.equals("Decímetros")){
                    unidadF = "dm";
                }else
                if (unidad.equals("Metros")){
                    unidadF = "m";
                }
                diame1 = diam1.getText().toString();
                diame2 = diam2.getText().toString();
                longi = lon.getText().toString();

                double diamet1=Double.parseDouble(diame1);
                double diamet2=Double.parseDouble(diame2);
                double longit=Double.parseDouble(longi);

                double sm=(Math.PI*Math.pow(diamet1+diamet2,2)*longit)/16;
                double s0=(Math.PI*Math.pow(diamet1,2)*longit)/4;
                double s1=(Math.PI*Math.pow(diamet2,2)*longit)/4;
                double resultado=(longit/6)*(s0+4*sm+s1);

                volu = String.format("%.4f", resultado);

                Dialog.show(this, "Resultado", "Volumen: "+ String.format("%.4f", resultado) +" "+ unidadF +"3", R.drawable.sucess);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

                            AlertDialog.Builder builder2 = new AlertDialog.Builder(NewtonActivity.this);
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
                        limpiar();
                        addNew();
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    public void visualizarPDF(){
        templatePDF = new TemplatePDFNewton(getApplicationContext());
        templatePDF.openDocument();
        templatePDF.addMetaData("Resultados","Cubicacion");
        templatePDF.addImage();
        templatePDF.addParagraph("\n");
        templatePDF.addParagraph("\n");
        templatePDF.addParagraph("\n");
        templatePDF.addTitles("Método de Newton","Cálculo del volumen de un fuste por la fórmula de Newton.",fecha, hora);
        templatePDF.addParagraph("Cálculos realizados por: " + nombreInforme);
        templatePDF.createTable(header,getClients());
        templatePDF.addParagraphResu("ATENTAMENTE");
        templatePDF.addParagraphCenter("___________________________________________________________");
        templatePDF.addParagraphCenter(nombreInforme);
        templatePDF.closeDocument();
        templatePDF.viewPDF();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_newton, menu);
        return true;
    }

    public void resNew(MenuItem menuItem){
        startActivity(new Intent(this, ResNewActivity.class));
        finish();
    }

    private ArrayList<String[]> getClients(){
        unidad = spinnerNe.getSelectedItem().toString();
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
        diame1 = diam1.getText().toString();
        diame2 = diam2.getText().toString();
        longi = lon.getText().toString();
        ArrayList<String[]>rows = new ArrayList<>();
        rows.add(new String[]{diame1 + " " +unidadF,diame2 +" "+unidadF,longi+" "+unidadF,volu+" "+unidadF+"3"});
        return rows;
    }

    public void addNew(){
        DBController dbController = new DBController(this);
        HashMap<String, String> queryValues = new HashMap<String, String>();
        queryValues.put("diametro1", diame1);
        queryValues.put("diametro2", diame2);
        queryValues.put("longitud", longi);
        queryValues.put("volumen", volu);
        queryValues.put("unidad", unidad);
        queryValues.put("usuario", nombreInforme);
        queryValues.put("fecha", fecha);
        queryValues.put("hora", hora);
        dbController.insertNewton(queryValues);
    }

    public void limpiar(){
        diam1.getText().clear();
        diam2.getText().clear();
        lon.getText().clear();
        spinnerNe.setSelection(0,true);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.warning);
        builder.setTitle("Regresar al Menu");
        builder.setMessage("¿Desea Regresar al menu principal y no realizar el cálculo?");
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
        LayoutInflater inflater = LayoutInflater.from(NewtonActivity.this);
        View subView = inflater.inflate(R.layout.dialog_newton, null);
        editTextNumN = (EditText) subView.findViewById(R.id.editTextCNew);
        editTextContraN = (EditText)subView.findViewById(R.id.editTextCPNew);
        final ImageView subImageView = (ImageView)subView.findViewById(R.id.image);

        Drawable drawable = getResources().getDrawable(R.drawable.password);
        subImageView.setImageDrawable(drawable);
        editTextNumN.setText(matriN);
        numN = editTextNumN.getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar Contraseña");
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
                //Toast.makeText(NewtonActivity.this, "Cancel", Toast.LENGTH_LONG).show();
            }
        });

        builder.show();
    }

    public void consulta(){
        contraN = editTextContraN.getText().toString();
        if (numN.equals(matriN) && contraN.equals(passdN)){
            visualizarPDF();
            addNew();
            Toast.makeText(getApplicationContext(), "PDF Generado Correctamente", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
        }
    }
}
