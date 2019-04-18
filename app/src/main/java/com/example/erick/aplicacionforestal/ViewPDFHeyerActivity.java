package com.example.erick.aplicacionforestal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

public class ViewPDFHeyerActivity extends AppCompatActivity {
    private PDFView pdfView;
    private File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pdfheyer);

        createDialog();
        pdfView = (PDFView) findViewById(R.id.pdfViewHeyer);
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            file = new File(bundle.getString("path",""));
        }

        pdfView.fromFile(file).enableSwipe(true).swipeHorizontal(false).enableDoubletap(true).enableAntialiasing(true).load();
    }

    private void createDialog(){
        final ProgressDialog progressDialog = new ProgressDialog(ViewPDFHeyerActivity.this);
        progressDialog.setIcon(R.drawable.pdf);
        progressDialog.setTitle("PDF Generado");
        progressDialog.setMessage("Se esta Guardando su Documento espere...!!");
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    while (progressDialog.getProgress()<=progressDialog.getMax()){
                        Thread.sleep(200);
                        progressDialog.incrementProgressBy(5);
                        if (progressDialog.getProgress()==progressDialog.getMax()){
                            progressDialog.dismiss();
                        }
                    }
                }catch (Exception e){
                    Toast.makeText(ViewPDFHeyerActivity.this, "Exception: " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
        progressDialog.show();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, HeyerActivity.class));
        finish();
    }
}
