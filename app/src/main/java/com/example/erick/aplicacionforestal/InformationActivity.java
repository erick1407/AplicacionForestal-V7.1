package com.example.erick.aplicacionforestal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class InformationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(InformationActivity.this, MainActivity.class));
        finish();
    }
}
