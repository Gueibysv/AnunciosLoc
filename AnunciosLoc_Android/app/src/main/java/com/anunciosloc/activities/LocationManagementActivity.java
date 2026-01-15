package com.anunciosloc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anunciosloc.R;
import com.anunciosloc.adapters.LocationAdapter;
import com.anunciosloc.data.MockDataSource;
import com.anunciosloc.network.LocationService;

public class LocationManagementActivity extends AppCompatActivity {

    private RecyclerView locationsRecyclerView;
    private LocationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_management);

        locationsRecyclerView = findViewById(R.id.locationsRecyclerView);
        locationsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button addLocationButton = findViewById(R.id.addLocationButton);
        addLocationButton.setOnClickListener(v -> {
            startActivity(new Intent(this, CreateLocationActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Thread(() -> {
            try {
                var locations = LocationService.getLocations();

                runOnUiThread(() -> {
                    adapter = new LocationAdapter(locations);
                    locationsRecyclerView.setAdapter(adapter);
                });

            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this,
                                "Erro ao carregar locais",
                                Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }


}
