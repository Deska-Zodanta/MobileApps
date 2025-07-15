package com.example.biodataapp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import java.io.*;
import java.util.HashMap;

public class DataActivity extends AppCompatActivity {

    TextView textViewNama, textViewUsia, textViewSekolah, textViewEmail, textViewPeminatan;
    ImageView ivFotoProfil;
    String emailLogin;
    String fotoPathString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        textViewNama = findViewById(R.id.textViewNama);
        textViewUsia = findViewById(R.id.textViewUsia);
        textViewSekolah = findViewById(R.id.textViewSekolah);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewPeminatan = findViewById(R.id.textViewPeminatan);
        ivFotoProfil = findViewById(R.id.ivFotoProfil);

        emailLogin = getIntent().getStringExtra("email");

        if (emailLogin == null || emailLogin.isEmpty()) {
            Toast.makeText(this, "Email tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        boolean dataDitemukan = false;

        try (FileInputStream fis = openFileInput("form_data.txt")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder fullText = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                fullText.append(line).append("\n");
            }

            String[] entries = fullText.toString().split("\\n---ENTRY_SEPARATOR---\\n");

            for (String entry : entries) {
                if (entry.trim().isEmpty()) continue;

                HashMap<String, String> dataMap = new HashMap<>();
                String[] lines = entry.split("\n");

                for (String d : lines) {
                    d = d.trim();
                    if (d.contains(": ")) {
                        String[] parts = d.split(": ", 2);
                        dataMap.put(parts[0].trim(), parts.length > 1 ? parts[1].trim() : "");
                    }
                }

                String storedEmail = dataMap.get("Email");
                if (emailLogin.equals(storedEmail)) {
                    textViewNama.setText(dataMap.getOrDefault("Nama", ""));
                    textViewUsia.setText(dataMap.getOrDefault("Usia", ""));
                    textViewSekolah.setText(dataMap.getOrDefault("Asal Sekolah", ""));
                    textViewEmail.setText(dataMap.getOrDefault("Email", ""));
                    textViewPeminatan.setText(dataMap.getOrDefault("Peminatan", ""));
                    fotoPathString = dataMap.getOrDefault("Foto", "");

                    // Tampilkan foto jika tersedia
                    try {
                        if (!fotoPathString.isEmpty()) {
                            File file = new File(fotoPathString);
                            if (file.exists()) {
                                Uri contentUri = FileProvider.getUriForFile(this,
                                        getPackageName() + ".fileprovider", file);
                                ivFotoProfil.setImageURI(contentUri);
                            } else {
                                ivFotoProfil.setImageResource(android.R.drawable.ic_menu_gallery);
                            }
                        } else {
                            ivFotoProfil.setImageResource(android.R.drawable.ic_menu_gallery);
                        }
                    } catch (Exception e) {
                        ivFotoProfil.setImageResource(android.R.drawable.ic_menu_gallery);
                        e.printStackTrace();
                    }

                    dataDitemukan = true;
                    break;
                }
            }

            if (!dataDitemukan) {
                textViewNama.setText("Data tidak ditemukan.");
            }

        } catch (IOException e) {
            textViewNama.setText("Gagal membaca data.");
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        
    }
}
