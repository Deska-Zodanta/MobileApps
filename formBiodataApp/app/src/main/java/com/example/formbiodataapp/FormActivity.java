package com.example.formbiodataapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.io.*;

public class FormActivity extends AppCompatActivity {

    EditText editTextNama, editTextUsia, editTextSekolah, editTextEmail;
    Spinner spinnerPeminatan;
    Button btnSimpan, btnLihatData, btnPilihFoto;
    ImageView ivFotoProfil;
    String emailLogin;

    private Uri selectedImageUri;

    private static final int PICK_IMAGE_REQUEST = 1;

    private String fotoPathLama = ""; // tambahkan sebagai field class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        // Inisialisasi view
        editTextNama = findViewById(R.id.editTextNama);
        editTextUsia = findViewById(R.id.editTextUsia);
        editTextSekolah = findViewById(R.id.editTextSekolah);
        editTextEmail = findViewById(R.id.editTextEmail);
        spinnerPeminatan = findViewById(R.id.spinnerPeminatan);
        btnSimpan = findViewById(R.id.btnSimpan);
        btnLihatData = findViewById(R.id.btnLihatData);
        btnPilihFoto = findViewById(R.id.btnPilihFoto);
        ivFotoProfil = findViewById(R.id.ivFotoProfil);

        emailLogin = getIntent().getStringExtra("email");
        editTextEmail.setText(emailLogin);
        editTextEmail.setEnabled(false); // tidak bisa diubah

        // Isi Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.kategori_peminatan, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPeminatan.setAdapter(adapter);

        // Muat data lama jika ada
        if (emailLogin != null && !emailLogin.isEmpty()) {
            muatDataLama(emailLogin);
        }

        // Tombol pilih foto
        btnPilihFoto.setOnClickListener(v -> openGallery());

        // Tombol Simpan
        btnSimpan.setOnClickListener(v -> {
            String nama = editTextNama.getText().toString();
            String usia = editTextUsia.getText().toString();
            String sekolah = editTextSekolah.getText().toString();
            String email = emailLogin;
            String peminatan = spinnerPeminatan.getSelectedItem().toString();

            String fotoPath = "";
            if (selectedImageUri != null) {
                fotoPath = saveImageToInternalStorage(selectedImageUri); // simpan foto baru
            } else if (!fotoPathLama.isEmpty()) {
                fotoPath = fotoPathLama; // gunakan foto lama
            }

            if (nama.isEmpty() || usia.isEmpty() || sekolah.isEmpty()) {
                Toast.makeText(this, "Semua field harus diisi", Toast.LENGTH_SHORT).show();
            } else {
                String data = "Nama: " + nama + "\n" +
                        "Usia: " + usia + "\n" +
                        "Asal Sekolah: " + sekolah + "\n" +
                        "Email: " + email + "\n" +
                        "Peminatan: " + peminatan + "\n" +
                        (fotoPath.isEmpty() ? "" : "Foto: " + fotoPath + "\n");

                simpanKeFile("form_data.txt", data);

                Intent intent = new Intent(FormActivity.this, PilihanActivity.class);
                intent.putExtra("email", emailLogin);
                startActivity(intent);
                finish();
            }
        });

        // Tombol Lihat Data
        btnLihatData.setOnClickListener(v -> {
            Intent intent = new Intent(this, DataActivity.class);
            intent.putExtra("email", emailLogin);
            startActivity(intent);
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            ivFotoProfil.setImageURI(selectedImageUri);
        }
    }

    // Simpan foto ke internal storage
    private String saveImageToInternalStorage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) return null;

            String filename = "profil_foto_" + emailLogin + ".jpg";
            FileOutputStream fos = openFileOutput(filename, MODE_PRIVATE); // satu foto per email

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }

            fos.close();
            inputStream.close();

            return getFilesDir() + "/" + filename;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Baca semua isi file
    private String bacaSemuaData(String fileName) throws IOException {
        FileInputStream fis = openFileInput(fileName);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }

        br.close();
        return sb.toString();
    }

    // Simpan data biodata ke file (update atau tambah)
    private void simpanKeFile(String fileName, String newData) {
        try {
            String fullText = "";
            File file = new File(getFilesDir(), fileName);
            if (file.exists()) {
                fullText = bacaSemuaData(fileName);
            }

            String[] entries = fullText.split("\\n---ENTRY_SEPARATOR---\\n");

            StringBuilder updatedData = new StringBuilder();
            boolean found = false;

            for (String entry : entries) {
                if (entry.trim().isEmpty()) continue;

                if (entry.contains("Email: " + emailLogin.trim())) {
                    updatedData.append(newData).append("\n---ENTRY_SEPARATOR---\n");
                    found = true;
                }
                else {
                    updatedData.append(entry).append("\n---ENTRY_SEPARATOR---\n");
                }
            }

            // Jika email belum ada, tambahkan sebagai entri baru
            if (!found) {
                updatedData.append(newData).append("\n---ENTRY_SEPARATOR---\n");
            }

            // Tulis ulang file
            FileOutputStream fos = openFileOutput(fileName, MODE_PRIVATE);
            fos.write(updatedData.toString().getBytes());
            fos.close();

            Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();

            // Reset form
            editTextNama.setText("");
            editTextUsia.setText("");
            editTextSekolah.setText("");
            spinnerPeminatan.setSelection(0);
            fotoPathLama = "";

        } catch (IOException e) {
            Toast.makeText(this, "Gagal menyimpan", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    // Muat data lama berdasarkan email
    private void muatDataLama(String email) {
        try (FileInputStream fis = openFileInput("form_data.txt")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder fullText = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                fullText.append(line).append("\n");
            }

            String[] entries = fullText.toString().split("\\n---ENTRY_SEPARATOR---\\n");

            for (String entry : entries) {
                if (entry.contains("Email: " + email.trim())) {
                    String[] lines = entry.split("\n");

                    for (String d : lines) {
                        d = d.trim(); // hindari spasi berlebih
                        if (d.startsWith("Nama:")) {
                            editTextNama.setText(d.replace("Nama: ", ""));
                        } else if (d.startsWith("Usia:")) {
                            editTextUsia.setText(d.replace("Usia: ", ""));
                        } else if (d.startsWith("Asal Sekolah:")) {
                            editTextSekolah.setText(d.replace("Asal Sekolah: ", ""));
                        } else if (d.startsWith("Peminatan:")) {
                            String peminatan = d.replace("Peminatan: ", "");
                            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinnerPeminatan.getAdapter();
                            int index = adapter.getPosition(peminatan);
                            if (index >= 0) {
                                spinnerPeminatan.setSelection(index);
                            }
                        } else if (d.startsWith("Foto:")) {
                            fotoPathLama = d.replace("Foto: ", "").trim();
                            if (!fotoPathLama.isEmpty()) {
                                File file = new File(fotoPathLama);
                                if (file.exists()) {
                                    ivFotoProfil.setImageURI(Uri.fromFile(file));
                                } else {
                                    ivFotoProfil.setBackgroundColor(0xFFE0E0E0);
                                }
                            } else {
                                ivFotoProfil.setBackgroundColor(0xFFE0E0E0);
                            }
                        }
                    }
                    break;
                }
            }

        } catch (IOException e) {
            Toast.makeText(this, "Gagal memuat data lama", Toast.LENGTH_SHORT).show();
        }
    }
}