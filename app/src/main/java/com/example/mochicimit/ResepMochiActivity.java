package com.example.mochicimit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class ResepMochiActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resep_mochi); // Layout memanggil string, bukan Java

        // 1. Cari semua View yang bisa diklik berdasarkan ID-nya
        ImageView tombolKembali = findViewById(R.id.btnBack);
        View itemMochiIceCream = findViewById(R.id.itemMochiIceCream);
        View itemMochiDaifuku = findViewById(R.id.itemMochiDaifuku);
        View itemMochiBites = findViewById(R.id.itemMochiBites);
        View itemMochiDango = findViewById(R.id.itemMochiDango);
        View itemCreamyMochi = findViewById(R.id.itemCreamyMochi);

        // 2. Berikan OnClickListener pada semua View tersebut
        if (tombolKembali != null) tombolKembali.setOnClickListener(this);
        if (itemMochiIceCream != null) itemMochiIceCream.setOnClickListener(this);
        if (itemMochiDaifuku != null) itemMochiDaifuku.setOnClickListener(this);
        if (itemMochiBites != null) itemMochiBites.setOnClickListener(this);
        if (itemMochiDango != null) itemMochiDango.setOnClickListener(this);
        if (itemCreamyMochi != null) itemCreamyMochi.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if (viewId == R.id.btnBack) {
            // Aksi kembali
            finish();
            Log.d("ResepMochiActivity", "Tombol Kembali diklik");

        } else if (viewId == R.id.itemMochiIceCream) {
            // Navigasi ke detail resep Ice Cream
            Intent intent = new Intent(ResepMochiActivity.this, DetailResepActivity.class);
            startActivity(intent);
            Log.d("ResepMochiActivity", "Mochi Ice Cream diklik");

        } else if (viewId == R.id.itemMochiDaifuku) {
            // Navigasi ke detail resep Daifuku
            Intent intent = new Intent(ResepMochiActivity.this, DetailResepActivity2.class);
            startActivity(intent);
            Log.d("ResepMochiActivity", "Mochi Daifuku diklik");

        } else if (viewId == R.id.itemMochiBites) {
            // Navigasi ke detail resep Bites
            Intent intent = new Intent(ResepMochiActivity.this, DetailResepActivity3.class);
            startActivity(intent);
            Log.d("ResepMochiActivity", "Mochi Bites diklik");

        } else if (viewId == R.id.itemMochiDango) {
            // Navigasi ke detail resep Dango
            Intent intent = new Intent(ResepMochiActivity.this, DetailResepActivity4.class);
            startActivity(intent);
            Log.d("ResepMochiActivity", "Mochi Dango diklik");

        } else if (viewId == R.id.itemCreamyMochi) {
            // Navigasi ke detail resep Creamy
            Intent intent = new Intent(ResepMochiActivity.this, DetailResepActivity5.class);
            startActivity(intent);
            Log.d("ResepMochiActivity", "Creamy Mochi diklik");

        }
    }
}