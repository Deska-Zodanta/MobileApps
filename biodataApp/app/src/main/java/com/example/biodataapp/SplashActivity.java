package com.example.biodataapp;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Handler untuk delay 5 detik
        new Handler().postDelayed(() -> {
            // Pindah ke LoginActivity
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Tutup SplashActivity agar tidak bisa kembali
        }, 5000); // 5000 ms = 5 detik
    }
}
