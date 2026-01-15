package com.anunciosloc.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.anunciosloc.R;
import com.anunciosloc.data.MockDataSource;
import com.anunciosloc.dto.LocationRequestDTO;
import com.anunciosloc.models.Local;
import com.anunciosloc.network.LocationService;
import com.anunciosloc.utils.SessionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CreateLocationActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1002;
    private FusedLocationProviderClient fusedLocationClient;

    private EditText locationNameEditText;
    private RadioGroup locationTypeRadioGroup;
    private LinearLayout gpsLayout;
    private LinearLayout wifiLayout;
    private EditText latitudeEditText;
    private EditText longitudeEditText;
    private EditText radiusEditText;
    private EditText wifiIdsEditText;
    private Button captureGpsButton;
    private Button saveLocationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_location);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        locationNameEditText = findViewById(R.id.locationNameEditText);
        locationTypeRadioGroup = findViewById(R.id.locationTypeRadioGroup);
        gpsLayout = findViewById(R.id.gpsLayout);
        wifiLayout = findViewById(R.id.wifiLayout);
        latitudeEditText = findViewById(R.id.latitudeEditText);
        longitudeEditText = findViewById(R.id.longitudeEditText);
        radiusEditText = findViewById(R.id.radiusEditText);
        wifiIdsEditText = findViewById(R.id.wifiIdsEditText);
        captureGpsButton = findViewById(R.id.captureGpsButton);
        saveLocationButton = findViewById(R.id.saveLocationButton);

        locationTypeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioGps) {
                gpsLayout.setVisibility(View.VISIBLE);
                wifiLayout.setVisibility(View.GONE);
            } else if (checkedId == R.id.radioWifi) {
                gpsLayout.setVisibility(View.GONE);
                wifiLayout.setVisibility(View.VISIBLE);
            }
        });

        captureGpsButton.setOnClickListener(v -> checkLocationPermissionAndCapture());
        saveLocationButton.setOnClickListener(v -> saveLocation());
    }

    private void checkLocationPermissionAndCapture() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            captureCurrentLocation();
        }
    }

    private void captureCurrentLocation() {
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                latitudeEditText.setText(String.valueOf(location.getLatitude()));
                                longitudeEditText.setText(String.valueOf(location.getLongitude()));
                                radiusEditText.setText("20"); // Valor padrão sugerido
                                Toast.makeText(CreateLocationActivity.this, "Coordenadas GPS capturadas.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CreateLocationActivity.this, "Localização não disponível.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (SecurityException e) {
            Toast.makeText(this, "Erro de permissão de localização.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureCurrentLocation();
            } else {
                Toast.makeText(this, "Permissão de localização negada.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void saveLocation() {

        String name = locationNameEditText.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this, "O nome do local é obrigatório.", Toast.LENGTH_SHORT).show();
            return;
        }

        LocationRequestDTO dto = new LocationRequestDTO();
        dto.nome = name;
        dto.username = SessionManager.getUsername(this);

        if (dto.username == null) {
            Toast.makeText(this, "Sessão inválida", Toast.LENGTH_LONG).show();
            return;
        }

        if (locationTypeRadioGroup.getCheckedRadioButtonId() == R.id.radioGps) {
            try {
                dto.tipoCoordenada = "GPS";
                dto.latitude = Double.parseDouble(latitudeEditText.getText().toString());
                dto.longitude = Double.parseDouble(longitudeEditText.getText().toString());
                dto.raioMetros = Double.parseDouble(radiusEditText.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Dados GPS inválidos.", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        else if (locationTypeRadioGroup.getCheckedRadioButtonId() == R.id.radioWifi) {
            dto.tipoCoordenada = "WIFI";
            dto.latitude = null;
            dto.longitude = null;
            dto.raioMetros = null;
            dto.wifiSSIDs = Arrays.stream(
                            wifiIdsEditText.getText().toString().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
        }

        new Thread(() -> {
            try {
                LocationService.create(dto);

                runOnUiThread(() -> {
                    Toast.makeText(this, "Local criado com sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                });

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() ->
                        Toast.makeText(this, "Erro ao guardar local", Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }
}
