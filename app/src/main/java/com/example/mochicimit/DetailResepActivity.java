package com.example.mochicimit;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.content.Intent;

public class DetailResepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_resep);

        // Tombol kembali
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        // Tombol menuju langkah-langkah
        Button btnLangkah = findViewById(R.id.btnLangkah);
        btnLangkah.setOnClickListener(v -> {
            Intent intent = new Intent(this, LangkahActivity.class);
            startActivity(intent);
        });
    }
}