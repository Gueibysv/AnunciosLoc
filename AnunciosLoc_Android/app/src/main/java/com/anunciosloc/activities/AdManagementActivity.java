package com.anunciosloc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anunciosloc.R;
import com.anunciosloc.adapters.AdAdapter;
import com.anunciosloc.dto.AdDTO;
import com.anunciosloc.network.AdService;

import java.util.List;
import java.util.function.Consumer;

import android.Manifest;
import android.location.Location;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

import com.anunciosloc.utils.SessionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;



public class AdManagementActivity extends AppCompatActivity {

    private RecyclerView adsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_management);

        adsRecyclerView = findViewById(R.id.adsRecyclerView);
        adsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button publishAdButton = findViewById(R.id.publishAdButton);
        publishAdButton.setOnClickListener(v ->
                startActivity(new Intent(this, PublishAdActivity.class))
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCurrentLocation(location -> {
            loadAvailableAds(location);
        });
    }

    private void getCurrentLocation(java.util.function.Consumer<Location> callback) {

        FusedLocationProviderClient client =
                LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(this,
                    "Permissão de localização não concedida",
                    Toast.LENGTH_LONG).show();
            return;
        }

        client.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        callback.accept(location);
                    } else {
                        Toast.makeText(this,
                                "Não foi possível obter localização",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }


    private void loadAvailableAds(Location location) {

        new Thread(() -> {
            try {
                List<AdDTO> ads = AdService.getAvailableAds(
                        location.getLatitude(),
                        location.getLongitude(),
                        SessionManager.getUsername(this)
                );

                runOnUiThread(() ->
                        adsRecyclerView.setAdapter(new AdAdapter(ads))
                );

            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this,
                                "Erro ao carregar anúncios disponíveis",
                                Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }


}
