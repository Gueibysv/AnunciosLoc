package com.anunciosloc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anunciosloc.R;
import com.anunciosloc.adapters.AdAdapter;
import com.anunciosloc.data.MockDataSource;

public class AdManagementActivity extends AppCompatActivity {

    private RecyclerView adsRecyclerView;
    private AdAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_management);

        adsRecyclerView = findViewById(R.id.adsRecyclerView);
        adsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Button publishAdButton = findViewById(R.id.publishAdButton);
        publishAdButton.setOnClickListener(v -> {
            startActivity(new Intent(this, PublishAdActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarrega a lista sempre que a Activity volta ao primeiro plano
        adapter = new AdAdapter(MockDataSource.getAds());
        adsRecyclerView.setAdapter(adapter);
    }
}
