package com.example.formbiodataapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button btnDaftar, btnLihatData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDaftar = findViewById(R.id.btnDaftar);
        btnLihatData = findViewById(R.id.btnLihatData);

        // Tombol Daftar
        btnDaftar.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("tujuan", "daftar");
            startActivity(intent);
        });

        // Tombol Lihat Data
        btnLihatData.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("tujuan", "lihat_data");
            startActivity(intent);
        });
    }
}