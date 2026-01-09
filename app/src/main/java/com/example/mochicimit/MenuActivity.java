package com.example.mochicimit;

// Import yang diperlukan
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import java.util.Locale;

public class MenuActivity extends AppCompatActivity {

    private Button btnResep;

    // --- (1) Logika Localization: Mengatur Locale dari SharedPreferences ---
    private void applyLanguageSetting(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        // Ambil kode bahasa yang tersimpan, default ke Indonesia ("in")
        String languageCode = prefs.getString("APP_LANGUAGE", "in");

        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);

        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    // --- (2) PENTING: Memanggil logika perubahan bahasa sebelum onCreate ---
    @Override
    protected void attachBaseContext(Context newBase) {
        applyLanguageSetting(newBase);
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // applyLanguageSetting() sudah dipanggil di attachBaseContext()
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnResep = findViewById(R.id.btnResep);

        // Logika Button klik
        btnResep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ganti DetailActivity.class dengan Activity yang menampilkan resep/menu detail
                Intent intent = new Intent(MenuActivity.this, ResepMochiActivity.class);
                startActivity(intent);
            }
        });
    }
}