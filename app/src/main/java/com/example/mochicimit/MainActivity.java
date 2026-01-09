package com.example.mochicimit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_DURATION = 3000;

    // --- (1) METODE UNTUK MENGUBAH BAHASA APLIKASI ---
    private void applyLanguageSetting(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        // Ambil kode bahasa yang tersimpan, default ke Indonesia ("in").
        // Kode ini disimpan di SplashActivity.java
        String languageCode = prefs.getString("APP_LANGUAGE", "in");

        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);

        // Memperbarui konfigurasi resource untuk Activity ini
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    // --- (2) PENTING: METODE INI DIJALANKAN SEBELUM onCreate() ---
    @Override
    protected void attachBaseContext(Context newBase) {
        // Panggil logika perubahan bahasa agar resource yang dimuat (strings.xml)
        // sesuai dengan bahasa yang dideteksi di SplashActivity
        applyLanguageSetting(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // applyLanguageSetting() sudah dipanggil di attachBaseContext()
        super.onCreate(savedInstanceState);

        // 1. Teks di R.layout.activity_main sekarang akan diterjemahkan
        setContentView(R.layout.activity_main);

        // 2. Handler untuk jeda 3 detik (sesuai SPLASH_SCREEN_DURATION)
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // 3. Pindah dari MainActivity (sebagai splash screen) ke MenuActivity
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);

                // Meneruskan data IS_WITHIN_CAMPUS (jika ada)
                if (getIntent().getExtras() != null) {
                    intent.putExtras(getIntent().getExtras());
                }

                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN_DURATION);
    }
}