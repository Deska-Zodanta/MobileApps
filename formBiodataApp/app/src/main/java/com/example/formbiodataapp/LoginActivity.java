package com.example.formbiodataapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.io.*;

public class LoginActivity extends AppCompatActivity {

    EditText editEmail, editPassword;
    Button btnLogin;
    String tujuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.editTextEmail);
        editPassword = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.btnLogin);

        tujuan = getIntent().getStringExtra("tujuan");

        btnLogin.setOnClickListener(v -> {
            String email = editEmail.getText().toString().trim();
            String password = editPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email dan Password harus diisi", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent;

                if (tujuan.equals("daftar")) {
                    // Cek apakah email sudah terdaftar
                    if (isEmailTerdaftar(email)) {
                        Toast.makeText(this, "Email sudah terdaftar!", Toast.LENGTH_LONG).show();
                        return; // batalkan pendaftaran
                    }

                    intent = new Intent(this, FormActivity.class);
                } else {
                    // Tujuan = login, lanjut saja
                    intent = new Intent(this, DataActivity.class);
                }

                intent.putExtra("email", email);
                startActivity(intent);
            }
        });
    }

    /**
     * Mengecek apakah email sudah pernah disimpan di form_data.txt
     */
    private boolean isEmailTerdaftar(String email) {
        try (FileInputStream fis = openFileInput("form_data.txt")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder fullText = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                fullText.append(line).append("\n");
            }

            String[] entries = fullText.toString().split("\\n---ENTRY_SEPARATOR---\\n");

            for (String entry : entries) {
                if (entry.contains("Email: " + email)) {
                    return true;
                }
            }
        } catch (IOException e) {
            // Jika file belum ada, maka tidak ada email yang terdaftar
        }

        return false;
    }
}
