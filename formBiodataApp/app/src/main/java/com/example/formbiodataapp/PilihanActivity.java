package com.example.formbiodataapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class PilihanActivity extends AppCompatActivity {

    Button btnKembali, btnLihatData;
    String emailLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pilihan);

        btnKembali = findViewById(R.id.btnKembali);
        btnLihatData = findViewById(R.id.btnLihatData);

        emailLogin = getIntent().getStringExtra("email");

        btnKembali.setOnClickListener(v -> {
            startActivity(new Intent(PilihanActivity.this, MainActivity.class));
        });

        btnLihatData.setOnClickListener(v -> {
            Intent intent = new Intent(PilihanActivity.this, DataActivity.class);
            intent.putExtra("email", emailLogin);
            startActivity(intent);
        });
    }
}