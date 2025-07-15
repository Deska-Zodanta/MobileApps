package com.example.biodataapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText editEmail, editPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editTextEmail);
        editPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString();

            // Validasi sederhana: email dan password tidak boleh kosong
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan Password harus diisi", Toast.LENGTH_SHORT).show();
            } else {
                // Arahkan ke PilihanActivity setelah login berhasil
                Intent intent = new Intent(LoginActivity.this, PilihanActivity.class);
                intent.putExtra("email", email); // Kirim email ke PilihanActivity
                startActivity(intent);
                finish(); // Tutup LoginActivity agar tidak bisa kembali
            }
        });
    }
}