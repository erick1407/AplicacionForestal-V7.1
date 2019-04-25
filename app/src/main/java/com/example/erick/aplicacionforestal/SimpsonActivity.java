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

public class SimpsonActivity extends AppCompatActivity {

    //dialogo material
    Button btnok;
    android.app.Dialog dialogoError;
    ImageView imgClose;
    TextView mensajeTV, ttituloTV;

    EditText d,l;
    String str1, l1;
    String fecha, hora;
    String nombreInforme;
    String diame, longi, volu;
    String unidad;
    MaterialSpinner spinnerUnidadSimp;
    private TemplatePDFSimpson templatePDF;
    private String[] header ={"Diámetros de C/Sección", "Longitud de la Toza", "Volumen"};
    //Variables Simpson Dialog
    EditText editTextNumSi, editTextContraSi;
    String matriSi, passdSi;
    String numSi, contraSi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simpson);

        // dialog material
        dialogoError= new android.app.Dialog(this);
        btnok = (Button) findViewById(R.id.btn_botonOk);
        imgClose = (ImageView) findViewById(R.id.img_close);

        d=(EditText)findViewById(R.id.editTextDiam);
        l=(EditText)findViewById(R.id.editTextLongi);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        fecha = simpleDateFormat.format(date);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String dataTime = sdf.format(new Date());
        hora = dataTime;

        spinnerUnidadSimp = (MaterialSpinner) findViewById(R.id.spinnerunidaesSim);

        List<String> unidades = new ArrayList<>();
        unidades.add("Centímetros");
        unidades.add("Decímetros");
        unidades.add("Metros");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unidades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnidadSimp.setAdapter(adapter);

        readName();

        DBController dbController = new DBController(this);
        SQLiteDatabase sqLiteDatabase = dbController.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Persona", null);
        if (cursor.moveToFirst()){
            matriSi = cursor.getString(2);
            passdSi = cursor.getString(3);
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

    private void showDialogErrorCamposSimp() {
        dialogoError.setContentView(R.layout.error_validacionsimpson);
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

    public void calcularSi(View view){
        if (spinnerUnidadSimp.getSelectedItem().toString().trim().equals("Unidades de Medida")){
            //Dialog.show(this,"Error", "Debe Seleccionar una Unidad de Medida");
            showDialogErrorUM();
        }else{
            if (d.getText().toString().isEmpty()||l.getText().toString().isEmpty()){
                showDialogErrorCamos();
                //Dialog.show(this,"Ups","Hay Campos Vacios");
            }else{
                String comasDia = d.getText().toString();
                int countLDia = 0;
                for(int x=0;x<comasDia.length();x++){
                    if (comasDia.charAt(x)==','){
                        countLDia++;
                    }
                }
                if (countLDia%2==0) {
                    unidad = spinnerUnidadSimp.getSelectedItem().toString();
                    String unidadF = null;
                    if (unidad.equals("Centímetros")) {
                        unidadF = "cm";
                    } else if (unidad.equals("Decímetros")) {
                        unidadF = "dm";
                    } else if (unidad.equals("Metros")) {
                        unidadF = "m";
                    }

                    str1 = d.getText().toString();
                    String[] d1 = str1.split(",");
                    double[] d2 = new double[d1.length];
                    for (int i = 0; i <= d2.length - 1; i++) {
                        d2[i] = Double.parseDouble(d1[i]);
                    }
                    l1 = l.getText().toString();
                    double l2 = Double.parseDouble(l1);
                    double a0 = Math.PI * (Math.pow(d2[0], 2) / 4);
                    double a1 = Math.PI * (Math.pow(d2[d2.length - 1], 2) / 4);
                    double suma1 = 0;
                    double suma2 = 0;
                    for (int k = 1; k <= d2.length - 2; k++) {
                        if (k % 2 == 1) {
                            suma1 = suma1 + Math.PI * (Math.pow(d2[k], 2) / 4);
                        } else {
                            suma2 = suma2 + Math.PI * (Math.pow(d2[k], 2) / 4);
                        }
                    }
                    double vol = (l2 / 3) * (a0 + a1 + 4 * suma1 + 2 * suma2);

                    volu = String.format("%.4f", vol);


                    Dialog.show(this, "Resultado", "Volumen: " + String.format("%.4f", vol) + " " + unidadF + "3", R.drawable.sucess);
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

                                AlertDialog.Builder builder2 = new AlertDialog.Builder(SimpsonActivity.this);
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
                            addSim();
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else{
                    d.getText().clear();
                    showDialogErrorCamposSimp();
                    //Dialog.show(this,"Diametros Incorrectos","El numero de Diametros debe ser Impar");
                }
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

    public void visualizarPDF(){
        templatePDF = new TemplatePDFSimpson(getApplicationContext());
        templatePDF.openDocument();
        templatePDF.addMetaData("Resultados","Cubicacion");
        templatePDF.addImage();
        templatePDF.addParagraph("\n");
        templatePDF.addParagraph("\n");
        templatePDF.addParagraph("\n");
        templatePDF.addTitles("Método de Simpson","Cubicación de madera por medio de la regla de Simpson. El número diámetros debe ser impar.",fecha, hora);
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
        unidad = spinnerUnidadSimp.getSelectedItem().toString();
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
        diame = d.getText().toString();
        longi = l.getText().toString();
        ArrayList<String[]>rows = new ArrayList<>();
        rows.add(new String[]{diame+" "+unidadF,longi+" "+unidadF,volu+" "+unidadF+"3"});
        return rows;
    }

    public void addSim(){
        DBController dbController = new DBController(this);
        HashMap<String, String> queryValues = new HashMap<String, String>();
        queryValues.put("diametroSe", str1);
        queryValues.put("longitud", l1);
        queryValues.put("volumen", volu);
        queryValues.put("unidad", unidad);
        queryValues.put("usuario", nombreInforme);
        queryValues.put("fecha", fecha);
        queryValues.put("hora", hora);
        dbController.insertSimpson(queryValues);
    }

    public void limpiar(){
        d.getText().clear();
        l.getText().clear();
        spinnerUnidadSimp.setSelection(0,true);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_simp, menu);
        return true;
    }

    public void resSi(MenuItem menuItem){
        startActivity(new Intent(this, ResSimActivity.class));
        finish();
    }

    public void confPass(){
        LayoutInflater inflater = LayoutInflater.from(SimpsonActivity.this);
        View subView = inflater.inflate(R.layout.dialog_simpson, null);
        editTextNumSi = (EditText) subView.findViewById(R.id.editTextCNSimp);
        editTextContraSi = (EditText)subView.findViewById(R.id.editTextCPSimp);
        final ImageView subImageView = (ImageView)subView.findViewById(R.id.image);

        Drawable drawable = getResources().getDrawable(R.drawable.password);
        subImageView.setImageDrawable(drawable);
        editTextNumSi.setText(matriSi);

        numSi = editTextNumSi.getText().toString();

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
            }
        });

        builder.show();
    }

    public void consulta(){
        contraSi = editTextContraSi.getText().toString();
        if (numSi.equals(matriSi) && contraSi.equals(passdSi)){
            visualizarPDF();
            addSim();
            Toast.makeText(getApplicationContext(), "PDF Generado Correctamente", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
        }
    }
}
