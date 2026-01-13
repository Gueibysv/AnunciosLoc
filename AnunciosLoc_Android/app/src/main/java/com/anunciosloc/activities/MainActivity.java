package com.anunciosloc.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.anunciosloc.R;
import com.anunciosloc.data.MockDataSource;
import com.anunciosloc.models.Local;
import com.anunciosloc.utils.SessionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private static final long LOCATION_UPDATE_INTERVAL = 5000; // 5 segundos
    private static final long LOCATION_FASTEST_INTERVAL = 2000; // 2 segundos

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private boolean locationObtained = false;

    // Localização padrão: Largo da Independência, Lisboa
    private static final LatLng DEFAULT_LOCATION = new LatLng(38.7343829, -9.1403882);
    private static final int DEFAULT_ZOOM = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Verifica se o utilizador está logado
        if (!SessionManager.isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        // Configura o mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Configura o callback de localização
        setupLocationCallback();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Desenha os locais no mapa
        drawLocationsOnMap();

        // Tenta obter a localização do dispositivo
        checkLocationPermissionAndGetLocation();
    }

    /**
     * Configura o callback que será chamado quando a localização for atualizada.
     */
    private void setupLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                if (locationResult != null && !locationResult.getLocations().isEmpty()) {
                    Location location = locationResult.getLocations().get(0);
                    LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                    // Move a câmara para a localização atual
                    if (mMap != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM));
                    }

                    // Marca que a localização foi obtida com sucesso
                    if (!locationObtained) {
                        locationObtained = true;
                        Toast.makeText(MainActivity.this, "Localização obtida com sucesso!", Toast.LENGTH_SHORT).show();

                        // Para de receber atualizações após obter a primeira localização
                        stopLocationUpdates();
                    }
                }
            }
        };
    }

    /**
     * Verifica a permissão de localização e obtém a localização do dispositivo.
     */
    private void checkLocationPermissionAndGetLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Solicita a permissão se ainda não foi concedida
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Permissão já foi concedida
            try {
                if (mMap != null) {
                    mMap.setMyLocationEnabled(true);
                }
                getDeviceLocation();
            } catch (SecurityException e) {
                Toast.makeText(this, "Erro de permissão de localização.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Obtém a localização do dispositivo usando requestLocationUpdates.
     * Este método é mais robusto que getLastLocation() pois obtém a localização em tempo real.
     */
    private void getDeviceLocation() {
        try {
            // Primeiro, tenta obter a última localização conhecida
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            // Se conseguir a última localização, usa-a
                            LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                            if (mMap != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, DEFAULT_ZOOM));
                            }
                            locationObtained = true;
                            Toast.makeText(MainActivity.this, "Localização obtida!", Toast.LENGTH_SHORT).show();
                        } else {
                            // Se não conseguir, solicita atualizações de localização em tempo real
                            requestLocationUpdates();
                        }
                    })
                    .addOnFailureListener(e -> {
                        // Se falhar, usa a localização padrão e solicita atualizações
                        useDefaultLocation();
                        requestLocationUpdates();
                    });
        } catch (SecurityException e) {
            Toast.makeText(this, "Erro de permissão de localização.", Toast.LENGTH_SHORT).show();
            useDefaultLocation();
        }
    }

    /**
     * Solicita atualizações de localização em tempo real.
     * Útil quando getLastLocation() retorna null (ex: dispositivo novo, emulador).
     */
    private void requestLocationUpdates() {
        try {
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(LOCATION_UPDATE_INTERVAL);
            locationRequest.setFastestInterval(LOCATION_FASTEST_INTERVAL);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao solicitar atualizações de localização.", Toast.LENGTH_SHORT).show();
            useDefaultLocation();
        }
    }

    /**
     * Para de receber atualizações de localização.
     */
    private void stopLocationUpdates() {
        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
    }

    /**
     * Usa a localização padrão (Largo da Independência, Lisboa) como fallback.
     */
    private void useDefaultLocation() {
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, DEFAULT_ZOOM));
        }
        Toast.makeText(this, "Usando localização padrão (Largo da Independência).", Toast.LENGTH_SHORT).show();
    }

    /**
     * Desenha os locais no mapa (marcadores e círculos).
     */
    private void drawLocationsOnMap() {
        if (mMap == null) {
            return;
        }

        for (Local local : MockDataSource.getLocations()) {
            if ("GPS".equals(local.getTipoCoordenada())) {
                LatLng locationLatLng = new LatLng(local.getLatitude(), local.getLongitude());

                // Adiciona o marcador
                mMap.addMarker(new MarkerOptions()
                        .position(locationLatLng)
                        .title(local.getNome()));

                // Adiciona o círculo (área de alcance)
                mMap.addCircle(new CircleOptions()
                        .center(locationLatLng)
                        .radius(local.getRaioMetros())
                        .strokeColor(ContextCompat.getColor(this, R.color.location_circle_stroke))
                        .fillColor(ContextCompat.getColor(this, R.color.location_circle_fill)));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissão foi concedida
                if (mMap != null) {
                    try {
                        mMap.setMyLocationEnabled(true);
                        getDeviceLocation();
                    } catch (SecurityException e) {
                        Toast.makeText(this, "Erro ao ativar localização no mapa.", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                // Permissão foi negada
                Toast.makeText(this, "Permissão de localização é necessária para o mapa.", Toast.LENGTH_LONG).show();
                useDefaultLocation();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Para de receber atualizações de localização quando a Activity é pausada
        stopLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Retoma as atualizações de localização quando a Activity é retomada
        if (locationObtained == false && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            requestLocationUpdates();
        }
    }

    // --- Menu ---

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_locations) {
            startActivity(new Intent(this, LocationManagementActivity.class));
            return true;
        } else if (id == R.id.action_ads) {
            startActivity(new Intent(this, AdManagementActivity.class));
            return true;
        } else if (id == R.id.action_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        } else if (id == R.id.action_logout) {
            SessionManager.clearSession();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}