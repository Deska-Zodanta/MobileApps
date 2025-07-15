package com.example.biodataapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PilihanActivity extends AppCompatActivity {

    Button btnLihatData, btnInputData, btnInformasia;
    String emailLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilihan); // Pastikan layout ini benar

        // Ambil email dari LoginActivity
        emailLogin = getIntent().getStringExtra("email");

        // Inisialisasi tombol
        btnLihatData = findViewById(R.id.btnLihatData);
        btnInputData = findViewById(R.id.btnInputData);
        btnInformasia = findViewById(R.id.btnInformasia);

        // Arahkan ke MainActivity (List Data Mahasiswa)
        btnLihatData.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(PilihanActivity.this, MainActivity.class);
                intent.putExtra("email", emailLogin); // Kirim email jika dibutuhkan
                startActivity(intent);
            }
        });

        // Tombol Input Data (misalnya ke FormActivity)
        btnInputData.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(PilihanActivity.this, FormActivity.class);
                //intent.putExtra("email", emailLogin);
                intent.putExtra("mode", "input");
                startActivity(intent);
            }
        });

        // Tombol Informasi (misalnya ke InformasiActivity)
        btnInformasia.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                Intent intent = new Intent(PilihanActivity.this, InformasiActivity.class);
                startActivity(intent);
            }
        });
    }
}