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
        loadAdsFromBackend();
    }

    private void loadAdsFromBackend() {

        new Thread(() -> {
            try {
                List<AdDTO> ads = AdService.getAllAds();

                runOnUiThread(() -> {
                    AdAdapter adapter = new AdAdapter(ads);
                    adsRecyclerView.setAdapter(adapter);
                });

            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this,
                                "Erro ao carregar anúncios",
                                Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }
}
