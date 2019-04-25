package com.example.erick.aplicacionforestal;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    private Session session;
    private final int REQUEST_ACCESS_FINE =0;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModelArrayList;

    private int[] myImageList = new int[]{R.drawable.slide1, R.drawable.slide2, R.drawable.slide3, R.drawable.slide4, R.drawable.slide5, R.drawable.slide6, R.drawable.slide7, R.drawable.slide8};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new Session(this);
        if (!session.loggedin()){
            logout();
        }

        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();

        init();
        validaPermisos();


    }

    private boolean validaPermisos(){
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M){
            return true;
        }
        if((checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED)){
            return true;
        }
        if((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE))){
            cargardialogorecomendacion();
        }else{
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE},100);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==100){
            if(grantResults.length==1 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

            }else{
                solicitarPermisosManual();
            }
        }

    }
    private void solicitarPermisosManual() {
        final CharSequence[] opciones={"SI","NO"};
        final AlertDialog.Builder alertOpciones=new AlertDialog.Builder(MainActivity.this);
        alertOpciones.setIcon(R.drawable.question);
        alertOpciones.setTitle("¿Desea configurar los permisos de forma manual?");
        alertOpciones.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("SI")){
                    Intent intent=new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri=Uri.fromParts("package",getPackageName(),null);
                    intent.setData(uri);
                    startActivity(intent);
                }else{

                    Toast.makeText(getApplicationContext(),"Los permisos no fueron aceptados",Toast.LENGTH_SHORT).show();
                    dialogInterface.dismiss();
                }
            }
        });
        alertOpciones.show();
    }


    private void cargardialogorecomendacion() {

        AlertDialog.Builder dialogo=new AlertDialog.Builder(MainActivity.this);
        dialogo.setTitle("Permisos desactivados");
        dialogo.setIcon(R.drawable.warning);
        dialogo.setMessage("Debe aceptar los permisos para el correcto funcionamiento de la App, los permisos son utilizados para poder generar correctamente el documento PDF");

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE},100);
            }
        });
        dialogo.show();
    }
    //============================TERMINA VALIDACION DE PERMISOS


    private void logout() {
        session.setLoggedin(false);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }


    //añade el menu, de la carpeta de munus
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menus_main,menu);
        return true;
    }


    //FUNCIONALIDAD DE CODIGO
    public void info (MenuItem item){
        startActivity(new Intent(this, InformationActivity.class));
        finish();
    }

    public void datos (MenuItem item){
        startActivity(new Intent(this, DatosInformeActivity.class));
        finish();
    }

    public void cerrarSes (MenuItem item){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cerrar sesión");
        builder.setMessage("¿Desea cerrar sesión?\n\nAl cerrar sesión se eliminarán los registros de los cálculos realizados\n\nRecomendamos sincronizar los datos antes de cerrar sesión");
        builder.setIcon(R.drawable.question);
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
                finish();
                cleanDB();
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

    //termina metodo
    public void huber(View view){
        startActivity(new Intent(this, HuberActivity.class));
        finish();
    }

    public void simpson(View view){
        startActivity(new Intent(this, SimpsonActivity.class));
        finish();
    }
    public void newton(View view){
        startActivity(new Intent(this, NewtonActivity.class));
        finish();
    }
    public void heyer(View view){
        startActivity(new Intent(this, HeyerActivity.class));
        finish();
    }
    //==========================================================================================================================

    //Metodo para abrir acceder a la ruta de los PDF´s
    public void openFolder(View view) {
        String folderPath = Environment.getExternalStorageDirectory() + "/PDFCubicacion/";
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        Uri myUri = Uri.parse(folderPath);
        intent.setDataAndType(myUri, "file/*");
        startActivity(intent);
    }


    //Muestra  un dialogo preguntando si desea salir de la aplicacion
    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Salir");
        builder.setIcon(R.drawable.warning);
        builder.setMessage("¿Desea salir de la aplicación?");
        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
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

    private ArrayList<ImageModel> populateList(){

        ArrayList<ImageModel> list = new ArrayList<>();

        for(int i = 0; i < 8; i++){
            ImageModel imageModel = new ImageModel();
            imageModel.setImage_drawable(myImageList[i]);
            list.add(imageModel);
        }

        return list;
    }

    private void init() {
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(MainActivity.this,imageModelArrayList));

        CirclePageIndicator indicator = (CirclePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;
        //Set circle indicator radius
        indicator.setRadius(5 * density);
        NUM_PAGES =imageModelArrayList.size();
        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 5000, 5000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    public void cleanDB(){
        DBController dbController = new DBController(this);
        SQLiteDatabase sqLiteDatabase = dbController.getWritableDatabase();
        sqLiteDatabase.execSQL("DELETE FROM Persona");
        sqLiteDatabase.execSQL("DELETE FROM Heyer");
        sqLiteDatabase.execSQL("DELETE FROM Huber");
        sqLiteDatabase.execSQL("DELETE FROM Newton");
        sqLiteDatabase.execSQL("DELETE FROM Simpson");
        sqLiteDatabase.close();
    }
}
