package com.example.aplikasisavenama;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.io.*;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    public static final String FILENAME = "contohfile.txt";

    EditText inputText, inputJudul;
    Button btnSimpan, btnBaca, btnEdit, btnHapus;
    TextView textIsiCatatan; // Hanya menyisakan textIsiCatatan
    Spinner spinnerJudul;

    List<String> daftarJudul = new ArrayList<>();
    Map<String, String> petaCatatan = new HashMap<>();
    ArrayAdapter<String> adapterSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi view
        inputJudul = findViewById(R.id.editTextJudul);
        inputText = findViewById(R.id.editTextInput);
        btnSimpan = findViewById(R.id.buttonSimpan);
        btnBaca = findViewById(R.id.buttonBaca);
        btnEdit = findViewById(R.id.buttonEdit);
        btnHapus = findViewById(R.id.buttonHapus);
        spinnerJudul = findViewById(R.id.spinnerJudul);
        textIsiCatatan = findViewById(R.id.textIsiCatatan); // Inisialisasi

        // Sembunyikan awalnya
        textIsiCatatan.setVisibility(View.GONE);

        adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, daftarJudul);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerJudul.setAdapter(adapterSpinner);

        muatCatatanKeMemori();

        btnSimpan.setOnClickListener(v -> simpanCatatan());
        btnBaca.setOnClickListener(v -> tampilkanCatatanDipilih());
        btnEdit.setOnClickListener(v -> editCatatan());
        btnHapus.setOnClickListener(v -> hapusCatatan());
    }

    private void simpanCatatan() {
        String judul = inputJudul.getText().toString().trim();
        String isi = inputText.getText().toString().trim();

        if (judul.isEmpty() || isi.isEmpty()) {
            Toast.makeText(this, "Judul dan isi tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        String formatTeks = "[Judul: " + judul + "]\n" + isi + "\n<END>\n";

        File file = new File(getFilesDir(), FILENAME);
        try (FileOutputStream fos = new FileOutputStream(file, true)) {
            fos.write(formatTeks.getBytes());
            fos.flush();

            inputJudul.setText("");
            inputText.setText("");

            Toast.makeText(this, "Catatan disimpan", Toast.LENGTH_SHORT).show();
            muatCatatanKeMemori();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void muatCatatanKeMemori() {
        daftarJudul.clear();
        petaCatatan.clear();

        File file = new File(getFilesDir(), FILENAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String baris;
            String judul = null;
            StringBuilder isi = new StringBuilder();

            while ((baris = br.readLine()) != null) {
                if (baris.startsWith("[Judul: ")) {
                    judul = baris.substring(8, baris.length() - 1);
                    isi = new StringBuilder();
                } else if (baris.equals("<END>")) {
                    if (judul != null) {
                        petaCatatan.put(judul, isi.toString().trim());
                        daftarJudul.add(judul);
                        judul = null;
                    }
                } else if (judul != null) {
                    isi.append(baris).append("\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        adapterSpinner.notifyDataSetChanged();
    }

    private void tampilkanCatatanDipilih() {
        String judulDipilih = spinnerJudul.getSelectedItem().toString();
        String isi = petaCatatan.get(judulDipilih);

        inputJudul.setText(judulDipilih);
        inputText.setText(isi);

        if (isi != null && !isi.isEmpty()) {
            textIsiCatatan.setText("Isi Catatan:\n" + isi);
            textIsiCatatan.setVisibility(View.VISIBLE);
        } else {
            textIsiCatatan.setVisibility(View.GONE);
        }
    }

    private void editCatatan() {
        String judulLama = spinnerJudul.getSelectedItem().toString();
        String judulBaru = inputJudul.getText().toString().trim();
        String isiBaru = inputText.getText().toString().trim();

        if (judulBaru.isEmpty() || isiBaru.isEmpty()) {
            Toast.makeText(this, "Judul dan isi tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(getFilesDir(), FILENAME);
        StringBuilder kontenBaru = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String baris;
            String judul = null;
            StringBuilder isi = new StringBuilder();

            while ((baris = br.readLine()) != null) {
                if (baris.startsWith("[Judul: ")) {
                    judul = baris.substring(8, baris.length() - 1);
                    isi = new StringBuilder();
                } else if (baris.equals("<END>")) {
                    if (judul.equals(judulLama)) {
                        kontenBaru.append("[Judul: ").append(judulBaru).append("]\n")
                                .append(isiBaru).append("\n<END>\n");
                    } else {
                        kontenBaru.append("[Judul: ").append(judul).append("]\n")
                                .append(isi.toString()).append("<END>\n");
                    }
                    judul = null;
                } else if (judul != null) {
                    isi.append(baris).append("\n");
                }
            }

            try (FileOutputStream fos = new FileOutputStream(file, false)) {
                fos.write(kontenBaru.toString().getBytes());
            }

            Toast.makeText(this, "Catatan diperbarui", Toast.LENGTH_SHORT).show();
            inputJudul.setText("");
            inputText.setText("");
            muatCatatanKeMemori();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void hapusCatatan() {
        String judulHapus = spinnerJudul.getSelectedItem().toString();

        File file = new File(getFilesDir(), FILENAME);
        StringBuilder kontenBaru = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String baris;
            String judul = null;
            StringBuilder isi = new StringBuilder();

            while ((baris = br.readLine()) != null) {
                if (baris.startsWith("[Judul: ")) {
                    judul = baris.substring(8, baris.length() - 1);
                    isi = new StringBuilder();
                } else if (baris.equals("<END>")) {
                    if (!judul.equals(judulHapus)) {
                        kontenBaru.append("[Judul: ").append(judul).append("]\n")
                                .append(isi.toString()).append("<END>\n");
                    }
                    judul = null;
                } else if (judul != null) {
                    isi.append(baris).append("\n");
                }
            }

            try (FileOutputStream fos = new FileOutputStream(file, false)) {
                fos.write(kontenBaru.toString().getBytes());
            }

            Toast.makeText(this, "Catatan dihapus", Toast.LENGTH_SHORT).show();
            inputJudul.setText("");
            inputText.setText("");
            muatCatatanKeMemori();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}