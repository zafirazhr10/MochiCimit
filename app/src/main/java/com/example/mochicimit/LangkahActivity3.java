package com.example.mochicimit;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity; // 1. Gunakan AppCompatActivity yang lebih modern

public class LangkahActivity3 extends AppCompatActivity { // 2. Ubah extends menjadi AppCompatActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_langkah3);

        // ================== KODE BARU UNTUK FUNGSI KEMBALI ==================

        // 3. Cari ImageView untuk tombol kembali dari layout activity_langkah.xml
        //    (Pastikan ID-nya adalah 'btnBack' di file XML Anda)
        ImageView tombolKembali = findViewById(R.id.btnBack);

        // 4. Tambahkan listener klik pada tombol tersebut
        if (tombolKembali != null) {
            tombolKembali.setOnClickListener(v -> {
                // finish() adalah perintah untuk menutup Activity saat ini (LangkahActivity3)
                // dan secara otomatis akan kembali ke Activity sebelumnya (DetailResepActivity3).
                finish();
            });
        }
        // =====================================================================
    }
}
