package com.example.mochicimit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    // Koordinat Kampus (Diperbarui ke Universitas Pelita Bangsa)
    private static final double CAMPUS_LAT = -6.3241085; // Latitude UPB (Lebih Presisi)
    private static final double CAMPUS_LNG = 107.1696207; // Longitude UPB (Lebih Presisi)
    private static final float RADIUS_METERS = 150f; // Jarak toleransi (Ditingkatkan menjadi 150m untuk akurasi GPS)

    private String mCountryCode = "ID";
    private TextView greetingTextView, statusTextView, addressTextView;
    private ImageView flagImageView;

    // Definisikan Model Data Negara (Tidak diubah)
    private static class CountryData {
        final String countryName;
        final String greeting;
        final int flagResId;

        CountryData(String countryName, String greeting, int flagResId) {
            this.countryName = countryName;
            this.greeting = greeting;
            this.flagResId = flagResId;
        }
    }

    // MAP NEGARA DAN SAPAAN (Tidak diubah)
    private static final Map<String, CountryData> COUNTRY_MAP = new HashMap<>();

    static {
        COUNTRY_MAP.put("ID", new CountryData("Indonesia", "Halo! Selamat Datang!", R.drawable.bendera_id));
        COUNTRY_MAP.put("JP", new CountryData("Jepang", "こんにちは! (Konnichiwa!)", R.drawable.ic_flag_jp));
        COUNTRY_MAP.put("KR", new CountryData("Korea Selatan", "환영합니다! (Hwanyounghamnida!)", R.drawable.ic_flag_kr));
        COUNTRY_MAP.put("US", new CountryData("Amerika Serikat", "Hello! Welcome!", R.drawable.ic_flag_us));
        COUNTRY_MAP.put("CIKARANG", new CountryData("Cikarang", "Selamat Datang di Cikarang!", R.drawable.logo_cikarang));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        greetingTextView = findViewById(R.id.greetingTextView);
        statusTextView = findViewById(R.id.statusTextView);
        addressTextView = findViewById(R.id.addressTextView);
        flagImageView = findViewById(R.id.flagImageView);

        checkLocationPermissions();
    }

    // --- METODE: Memaksa update Locale pada Context yang sedang berjalan ---
    private void applyLocaleToCurrentContext(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = getResources().getConfiguration();
        config.setLocale(locale);

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    // --- METODE: Cek Izin Lokasi ---
    private void checkLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            detectLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            detectLocation();
        } else {
            Toast.makeText(this, "Izin lokasi ditolak. Absen dibatalkan.", Toast.LENGTH_LONG).show();
            goToMainActivity(false);
        }
    }

    // --- METODE: Deteksi Lokasi dan Tentukan Negara ---
    private void detectLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            goToMainActivity(false);
            return;
        }

        // Menggunakan getLastKnownLocation (dianggap lokasi terakhir yang terbaik)
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();

            String languageCode = "in"; // Default: Bahasa Indonesia

            // Geocoder dengan Locale default untuk mendapatkan kode negara/alamat
            Geocoder geocoder = new Geocoder(SplashActivity.this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address addr = addresses.get(0);
                    mCountryCode = addr.getCountryCode() != null ? addr.getCountryCode().toUpperCase(Locale.ROOT) : "ID";

                    // START: PENENTUAN BAHASA DAN APPLY LOCALE SEGERA
                    if (mCountryCode.equalsIgnoreCase("JP")) {
                        languageCode = "ja";
                    } else if (mCountryCode.equalsIgnoreCase("KR")) {
                        languageCode = "ko";
                    } else if (mCountryCode.equalsIgnoreCase("US")) {
                        languageCode = "en";
                    }

                    // 1. Simpan bahasa untuk Activity selanjutnya
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("APP_LANGUAGE", languageCode);
                    editor.apply();

                    // 2. PAKSA UPDATE LOCALE PADA CONTEXT SPLASHACTIVITY
                    applyLocaleToCurrentContext(languageCode);
                    // END: PENENTUAN BAHASA DAN APPLY LOCALE SEGERA

                    String locality = addr.getLocality();
                    String countryName = addr.getCountryName();

                    // Panggilan ini akan menggunakan Locale baru yang telah disetel
                    setFlagAndGreeting(mCountryCode, locality, countryName);

                    // Lokasi Detail
                    addressTextView.setText("Lokasi Detail: " + (addr.getAddressLine(0) != null ? addr.getAddressLine(0) : countryName));
                } else {
                    mCountryCode = "ID";
                }
            } catch (IOException e) {
                mCountryCode = "ID";
                e.printStackTrace();
            }

            // Cek lokasi dengan koordinat yang sudah diperbarui
            boolean isWithinCampus = isWithinCampusRadius(lat, lng);

            goToMainActivity(isWithinCampus);

        } else {
            Toast.makeText(this, "Gagal mendapatkan lokasi. Absen Gagal.", Toast.LENGTH_LONG).show();
            mCountryCode = "ID";
            goToMainActivity(false);
        }
    }

    // --- METODE: Cek Radius Kampus ---
    private boolean isWithinCampusRadius(double currentLat, double currentLng) {
        float[] results = new float[1];
        Location.distanceBetween(CAMPUS_LAT, CAMPUS_LNG, currentLat, currentLng, results);

        boolean isWithin = results[0] <= RADIUS_METERS;

        // getString() sekarang akan menggunakan Locale yang baru di-update
        String successText = getString(R.string.status_absen_success);
        String failText = getString(R.string.status_absen_fail);

        // Tambahkan detail jarak untuk debugging (Opsional)
        statusTextView.setText((isWithin ? successText : failText) + " (Jarak: " + String.format("%.2f", results[0]) + "m)");

        return isWithin;
    }

    // --- METODE: Set Tampilan Splash ---
    private void setFlagAndGreeting(String countryCode, String locality, String countryName) {
        CountryData data = null;

        // Cek jika ID dan berada di Cikarang
        if (countryCode.equalsIgnoreCase("ID") && locality != null && locality.toLowerCase(Locale.ROOT).contains("cikarang")) {
            data = COUNTRY_MAP.get("CIKARANG");
        } else {
            data = COUNTRY_MAP.get(countryCode);
        }

        if (data == null) {
            data = COUNTRY_MAP.get("ID");
            mCountryCode = "ID";
        }

        greetingTextView.setText(data.greeting);
        flagImageView.setImageResource(data.flagResId);
        flagImageView.setVisibility(View.VISIBLE);
    }

    // --- METODE: PINDAH KE MAINACTIVITY ---
    private void goToMainActivity(boolean isWithinCampus) {
        // Pindah ke MainActivity setelah jeda
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.putExtra("IS_WITHIN_CAMPUS", isWithinCampus);
            startActivity(intent);
            finish();
        }, 2500);
    }
}