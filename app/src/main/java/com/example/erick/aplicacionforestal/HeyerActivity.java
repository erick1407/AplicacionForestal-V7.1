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
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import fr.ganfra.materialspinner.MaterialSpinner;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class HeyerActivity extends AppCompatActivity {


    //dialogo material
    Button btnok;
    android.app.Dialog dialogoError;
    ImageView imgClose;
    TextView mensajeTV, ttituloTV;
    TextView mensajeTV_AD, ttituloTV_AD;


    TextView tvnum;
    Button numerosboton;

    int  DiametrosCorrectos;
    double numero;
    int comas2CHECKBOX;
    String unidadF;


    //variables de metodo heyer diametros
    String str1;
    String[] d1;
    double[] d2;
    //variables de metodo heyer longitudes
    String str2;
    String[] l1;
    double[] l2;

    //variables de metodo heyer diametrosCB
    String str1CB;
    String[] d1CB;
    double[] d2CB;
    //variables de metodo heyer longitudesCB
    String str2CB;
    String[] l1CB;
    double[] l2CB;

    int contadordecomasl = 0;
    //int contadordecomasd = 0;
    String comaslongitudes;
    //String comasdiametros;
    int comasDCorrectas;

    EditText diametros, longitudes;
    TextInputLayout TILdiametros;
    TextInputLayout TILlongitudes;
    CheckBox checkBoxdiametro;


    String fecha, hora;
    String diame, longi, volu;
    String unidad;
    String nombreInforme;
    MaterialSpinner spinnerunidades;
    private TemplatePDFHeyer templatePDF;
    private String[] header = {"Diámetros de C/Sección", "Longitudes de C/Sección", "Volumen"};

    //Variables para confirmar la contraseña
    TextView textInfo;
    EditText editTextNum, editTextContra;
    String matri, passd;
    String num, contra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heyer);


        // dialog material
        dialogoError= new android.app.Dialog(this);
        btnok = (Button) findViewById(R.id.btn_botonOk);
        imgClose = (ImageView) findViewById(R.id.img_close);

        //action bar
        //setupActionBar();
        diametros = (EditText) findViewById(R.id.editTextDiams);

        longitudes = (EditText) findViewById(R.id.editTextLons);
        TILdiametros = (TextInputLayout) findViewById(R.id.impdiametros);
        TILlongitudes = (TextInputLayout) findViewById(R.id.implongitudes);
        checkBoxdiametro = (CheckBox) findViewById(R.id.checkBox);

        DBController dbController = new DBController(this);
        SQLiteDatabase sqLiteDatabase = dbController.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM Persona", null);
        if (cursor.moveToFirst()){
            matri = cursor.getString(2);
            passd = cursor.getString(3);
        }


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        fecha = simpleDateFormat.format(date);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String dataTime = sdf.format(new Date());
        hora = dataTime;

        spinnerunidades = (MaterialSpinner) findViewById(R.id.spinnerunidaes);
        List<String> unidades = new ArrayList<String>();

        unidades.add("Centímetros");
        unidades.add("Decímetros");
        unidades.add("Metros");


        //spinnerlongitudes = (MaterialSpinner) findViewById(R.id.spinnerlongitudes);
        ArrayAdapter<CharSequence> adapterv = new ArrayAdapter(this, android.R.layout.simple_spinner_item, unidades);
        adapterv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerunidades.setAdapter(adapterv);

        readName();
    }//llave layout===================================================================


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

    private void showDialogDiametrosCB() {
        dialogoError.setContentView(R.layout.error_diametros_chb);
        imgClose=(ImageView) dialogoError.findViewById(R.id.img_close);
        btnok=(Button) dialogoError.findViewById(R.id.btn_botonOk);
        ttituloTV=(TextView)dialogoError.findViewById(R.id.tituloTv);
        mensajeTV=(TextView)dialogoError.findViewById(R.id.mensajeTv);

        dialogoError.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogoError.show();

    }

    private void showDialogErrorDiametros() {
        dialogoError.setContentView(R.layout.error_diametros);
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


    //action bar fecha de retorno
    /*private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            //titulo
            actionBar.setTitle("Metodo de Heyer");
        }
    }*/

    public boolean comprobarcamposvacios() {
        if (diametros.getText().toString().isEmpty() || longitudes.getText().toString().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }



        public void calcularHeSinCHECKB() {

            unidad = spinnerunidades.getSelectedItem().toString();
            if (spinnerunidades.getSelectedItem().toString().trim().equals("Unidades de Medida")) {
                showDialogErrorUM();
                //Dialog.show(this, "Error", "Debe Seleccionar una Unidad de Medida");
                //Toast toast = Toast.makeText(getApplicationContext(),"sel",Toast.LENGTH_LONG);
                //toast.show();
            } else {
                unidad = spinnerunidades.getSelectedItem().toString();
                unidadF = null;
                if (unidad.equals("Centímetros")) {
                    unidadF = "cm";
                } else if (unidad.equals("Decímetros")) {
                    unidadF = "dm";
                } else if (unidad.equals("Metros")) {
                    unidadF = "m";
                }
                //}
                //=============================================
                str1 = diametros.getText().toString();
                d1 = str1.split(",");
                d2 = new double[d1.length];
                for (int i = 0; i <= d2.length - 1; i++) {
                    d2[i] = Double.parseDouble(d1[i]);
                }
                //============================================
                str2 = longitudes.getText().toString();
                l1 = str2.split(",");
                l2 = new double[l1.length];
                for (int i = 0; i <= l1.length - 1; i++) {
                    l2[i] = Double.parseDouble(l1[i]);
                }
                double res = 0;
                double suma = 0;
                for (int k = 0; k <= l2.length - 1; k++) {
                    suma = suma + (Math.PI * Math.pow(d2[k] + d2[k + 1], 2) * l2[k]) / 16;
                    res = suma;
                }


                volu = String.format("%.4f", res);
                Dialog.show(this, "Resultado", "Volumen: " + String.format("%.4f", res) + " " + unidadF + "3", R.drawable.sucess);
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

                            AlertDialog.Builder builder2 = new AlertDialog.Builder(HeyerActivity.this);
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
                        limpiarHe();
                        addHeyer();
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
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

    public void visualizarPDF() {
        templatePDF = new TemplatePDFHeyer(getApplicationContext());
        templatePDF.openDocument();
        templatePDF.addMetaData("Resultados", "Cubicacion");
        templatePDF.addImage();
        templatePDF.addParagraph("\n");
        templatePDF.addParagraph("\n");
        templatePDF.addParagraph("\n");
        templatePDF.addTitles("Método de Heyer", "Cubicación de madera por medio de la regla de Heyer para secciones de diferente longitud. El número de diámetros debe ser uno más que el de longitudes.", fecha, hora);
        templatePDF.addParagraph("Cálculos realizados por: " + nombreInforme);
        templatePDF.createTable(header,getClients());
        templatePDF.addParagraphResu("ATENTAMENTE");
        templatePDF.addParagraphCenter("___________________________________________________________");
        templatePDF.addParagraphCenter(nombreInforme);
        templatePDF.closeDocument();
        templatePDF.viewPDF();
        finish();
    }

    private ArrayList<String[]> getClients() {
        unidad = spinnerunidades.getSelectedItem().toString();
        String unidadF = null;
        if (unidad.equals("Centímetros")) {
            unidadF = "cm";
        } else if (unidad.equals("Decímetros")) {
            unidadF = "dm";
        } else if (unidad.equals("Metros")) {
            unidadF = "m";
        }
        diame = diametros.getText().toString();
        longi = longitudes.getText().toString();
        ArrayList<String[]> rows = new ArrayList<>();
        rows.add(new String[]{diame + " " + unidadF, longi + " " + unidadF, volu + " " + unidadF});
        return rows;
    }

    public void limpiarHe() {
        diametros.getText().clear();
        longitudes.getText().clear();
        spinnerunidades.setSelection(0, true);
    }//===================================================================================


    public void checkboxActivo() {
        str2 = longitudes.getText().toString();
        l1 = str2.split(",");
        l2 = new double[l1.length];
        int lt = l2.length;

        str1 = diametros.getText().toString();
        d1 = str1.split(",");
        d2 = new double[d1.length];
        int dt = d2.length;
        DiametrosCorrectos = lt + 1;

        String comasdiametros = diametros.getText().toString();
        int contadordecomasd = 0;
        for (int x = 0; x < comasdiametros.length(); x++) {
            if ((comasdiametros.charAt(x) == ',')) {
                contadordecomasd++;
            }
        }
        if (dt > 1 || contadordecomasd > 0) {
            showDialogDiametrosCB();
            TILdiametros.setError("Usted ha establecido un solo diámetro para todas las longitudes");
        } else {

            TILdiametros.setError(null);

            unidad = spinnerunidades.getSelectedItem().toString();
            if (spinnerunidades.getSelectedItem().toString().trim().equals("Unidades de Medida")) {
                //Dialog.show(this, "Error", "Debe Seleccionar una Unidad de Medida");
                showDialogErrorUM();
            } else {
                unidad = spinnerunidades.getSelectedItem().toString();
                unidadF = null;
                if (unidad.equals("Centímetros")) {
                    unidadF = "cm";
                } else if (unidad.equals("Decímetros")) {
                    unidadF = "dm";
                } else if (unidad.equals("Metros")) {
                    unidadF = "m";
                }
                //==============
                DiametrosCorrectos = lt + 1;
                //comas2CHECKBOX = contadordecomasl + 2
                //===========================================================
                str2 = longitudes.getText().toString();
                l1 = str2.split(",");
                l2 = new double[l1.length];
                for (int i = 0; i <= l1.length - 1; i++) {
                    l2[i] = Double.parseDouble(l1[i]);
                }//=====================================t
                //str1 = diametros.getText().toString();
                //numero = Integer.parseInt(diametros.getText().toString());
                numero = Double.parseDouble(diametros.getText().toString());
                d2 = new double[DiametrosCorrectos];//el arreglo d2 vale lo mismo del arrelo l2+1
                for (int i = 0; i < d2.length; i++) {
                    //d2[i] = Double.parseDouble(d1[i]);
                    d2[i] = numero;
                }

                double res = 0;
                double suma = 0;
                for (int k = 0; k < l2.length; k++) {
                    suma = suma + (Math.PI * Math.pow(d2[k] + d2[k + 1], 2) * l2[k]) / 16;
                    res = suma;
                }//========================================================================0


                volu = String.format("%.4f", res);
                Dialog.show(this, "Resultado", "Volumen: " + String.format("%.4f", res) + " " + unidadF + "3", R.drawable.sucess);
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

                            AlertDialog.Builder builder2 = new AlertDialog.Builder(HeyerActivity.this);
                            // builder.setTitle("Regresar");
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
                        limpiarHe();
                        addHeyer();
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }



    //=============================================================


    public void diametrosCorrectosSinCB(){
        //totaldeDiametros();
        //totaldeLongitudes();
        str2 = longitudes.getText().toString();
        l1 = str2.split(",");
        l2 = new double[l1.length];
        int lt=l2.length;

        str1 = diametros.getText().toString();
        d1 = str1.split(",");
        d2 = new double[d1.length];
        int dt=d2.length;

        DiametrosCorrectos=lt+1;

        if (DiametrosCorrectos==dt) {
            calcularHeSinCHECKB();
            TILdiametros.setError(null);
        } else {
            showDialogErrorDiametros();
            TILdiametros.setError("Diámetros invalidos");
        }
    }//==========================================================================


    //===================METODO DEL BOTON========================================================
    public void heyerValidado(View view){
        if (comprobarcamposvacios()==false) {
            //Toast toast = Toast.makeText(getApplicationContext(),"Hay campos vacios",Toast.LENGTH_LONG);
            //toast.show();
            showDialogErrorCamos();
            //Dialog.show(this,"Ups","Hay Campos Vacios");
        }else {
            if (checkBoxdiametro.isChecked()){
                checkboxActivo();
            }else {
                    diametrosCorrectosSinCB();
            }
        }
    }//==============================================



    //=================================================================================================


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_heyer, menu);
        return true;
    }

    public void resHeyer(MenuItem menuItem){
        startActivity(new Intent(this, ResHeyActivity.class));
        finish();
    }

    public void addHeyer(){
        DBController dbController = new DBController(this);
        HashMap<String, String> queryValues = new HashMap<String, String>();
        queryValues.put("diametros", str1);
        queryValues.put("longitudes", str2);
        queryValues.put("volumen", volu);
        queryValues.put("unidad", unidad);
        queryValues.put("usuario", nombreInforme);
        queryValues.put("fecha", fecha);
        queryValues.put("hora", hora);
        dbController.insertHeyer(queryValues);
    }

    @Override
    public void onBackPressed () {
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
        LayoutInflater inflater = LayoutInflater.from(HeyerActivity.this);
        View subView = inflater.inflate(R.layout.dialog_heyer, null);
        editTextNum = (EditText) subView.findViewById(R.id.editTextCNHeyer);
        editTextContra = (EditText)subView.findViewById(R.id.editTextCPHeyer);
        final ImageView subImageView = (ImageView)subView.findViewById(R.id.image);

        Drawable drawable = getResources().getDrawable(R.drawable.password);
        subImageView.setImageDrawable(drawable);
        editTextNum.setText(matri);

        num = editTextNum.getText().toString();

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
                //Toast.makeText(HeyerActivity.this, "Cancel", Toast.LENGTH_LONG).show();
            }
        });

        builder.show();
    }

    public void consulta(){
        contra = editTextContra.getText().toString();
        if (num.equals(matri) && contra.equals(passd)){
            visualizarPDF();
            addHeyer();
            Toast.makeText(getApplicationContext(), "PDF Generado Correctamente", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
        }
    }

}

