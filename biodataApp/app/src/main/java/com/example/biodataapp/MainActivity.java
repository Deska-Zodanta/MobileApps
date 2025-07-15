package com.example.biodataapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listViewData;
    ArrayList<String> dataList;
    Button btnInputData; // ✅ Tambahkan deklarasi tombol

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        listViewData = findViewById(R.id.listMahasiswa);
        btnInputData = findViewById(R.id.btnInputData); // ✅ Hubungkan tombol ke view

        // ✅ Atur tombol untuk membuka FormActivity dalam mode input baru (kosong)
        btnInputData.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FormActivity.class);
            intent.putExtra("mode", "input"); // ← penanda mode input kosong
            startActivity(intent);
        });

        dataList = new ArrayList<>();
        try {
            String fullText = readFromFile("form_data.txt");
            String[] entries = fullText.split("\n---ENTRY_SEPARATOR---\n");

            for (String entry : entries) {
                if (!entry.trim().isEmpty()) {
                    String nama = extractNama(entry);
                    if (nama != null && !nama.isEmpty()) {
                        dataList.add(entry);
                    }
                }
            }

        } catch (IOException e) {
            Toast.makeText(this, "Gagal membaca data", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        ArrayList<String> namaList = new ArrayList<>();
        for (String entry : dataList) {
            namaList.add(extractNama(entry));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, namaList);
        listViewData.setAdapter(adapter);

        registerForContextMenu(listViewData);

        listViewData.setOnItemClickListener((parent, view, position, id) ->
                openContextMenu(view)
        );
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        if (item.getItemId() == R.id.menu_lihat_data) {
            lihatData(position);
            return true;
        } else if (item.getItemId() == R.id.menu_update_data) {
            updateData(position);
            return true;
        } else if (item.getItemId() == R.id.menu_hapus_data) {
            hapusData(position);
            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void lihatData(int position) {
        String selectedEntry = dataList.get(position);
        String email = extractEmail(selectedEntry);

        if (email != null && !email.isEmpty()) {
            Intent intent = new Intent(MainActivity.this, DataActivity.class);
            intent.putExtra("email", email);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Email tidak ditemukan", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateData(int position) {
        String selectedEntry = dataList.get(position);
        Intent intent = new Intent(MainActivity.this, FormActivity.class);
        intent.putExtra("mode", "edit");
        intent.putExtra("data", selectedEntry);
        startActivity(intent);
    }

    private void hapusData(int position) {
        String selectedEntry = dataList.get(position);
        String email = extractEmail(selectedEntry);

        try {
            String fullText = readFromFile("form_data.txt");
            String[] entries = fullText.split("\n---ENTRY_SEPARATOR---\n");

            StringBuilder updatedData = new StringBuilder();
            String fotoPath = null;

            for (String entry : entries) {
                if (!entry.contains("Email: " + email)) {
                    updatedData.append(entry).append("\n---ENTRY_SEPARATOR---\n");
                } else {
                    String[] lines = entry.split("\n");
                    for (String line : lines) {
                        if (line.startsWith("Foto: ")) {
                            fotoPath = line.replace("Foto: ", "").trim();
                            break;
                        }
                    }
                }
            }

            FileOutputStream fos = openFileOutput("form_data.txt", MODE_PRIVATE);
            fos.write(updatedData.toString().getBytes());
            fos.close();

            hapusFoto(fotoPath);

            Toast.makeText(this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
            reloadData();
        } catch (IOException e) {
            Toast.makeText(this, "Gagal menghapus data", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void reloadData() {
        try {
            String fullText = readFromFile("form_data.txt");
            String[] entries = fullText.split("\n---ENTRY_SEPARATOR---\n");

            dataList.clear();
            for (String entry : entries) {
                if (!entry.trim().isEmpty()) {
                    String nama = extractNama(entry);
                    if (nama != null && !nama.isEmpty()) {
                        dataList.add(entry);
                    }
                }
            }

            ArrayList<String> namaList = new ArrayList<>();
            for (String entry : dataList) {
                namaList.add(extractNama(entry));
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, namaList);
            listViewData.setAdapter(adapter);

        } catch (IOException e) {
            Toast.makeText(this, "Gagal membaca data", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private String readFromFile(String fileName) throws IOException {
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

    private String extractNama(String entry) {
        String[] lines = entry.split("\n");
        for (String line : lines) {
            if (line.startsWith("Nama: ")) {
                return line.replace("Nama: ", "").trim();
            }
        }
        return null;
    }

    private String extractEmail(String entry) {
        String[] lines = entry.split("\n");
        for (String line : lines) {
            if (line.startsWith("Email: ")) {
                return line.replace("Email: ", "").trim();
            }
        }
        return null;
    }

    private void hapusFoto(String fotoPath) {
        if (fotoPath != null && !fotoPath.isEmpty()) {
            File file = new File(fotoPath);
            if (file.exists()) {
                boolean deleted = file.delete();
                if (!deleted) {
                    Toast.makeText(this, "Gagal menghapus foto", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
