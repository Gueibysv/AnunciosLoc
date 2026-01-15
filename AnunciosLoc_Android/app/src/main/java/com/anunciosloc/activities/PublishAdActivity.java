package com.anunciosloc.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anunciosloc.R;
import com.anunciosloc.data.MockDataSource;
import com.anunciosloc.dto.LocationDTO;
import com.anunciosloc.models.Anuncio;
import com.anunciosloc.models.Local;
import com.anunciosloc.network.AdService;
import com.anunciosloc.network.LocationService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PublishAdActivity extends AppCompatActivity {

    private EditText adTextEditText;
    private Spinner locationSpinner;
    private RadioGroup policyTypeRadioGroup;
    private EditText policyKeyValueEditText;
    private RadioGroup deliveryModeRadioGroup;
    private Button publishButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_ad);

        adTextEditText = findViewById(R.id.adTextEditText);
        locationSpinner = findViewById(R.id.locationSpinner);
        policyTypeRadioGroup = findViewById(R.id.policyTypeRadioGroup);
        policyKeyValueEditText = findViewById(R.id.policyKeyValueEditText);
        deliveryModeRadioGroup = findViewById(R.id.deliveryModeRadioGroup);
        publishButton = findViewById(R.id.publishButton);

        loadLocations();

        publishButton.setOnClickListener(v -> publishAd());
    }

    private void loadLocations() {

        new Thread(() -> {
            try {
                var locations = LocationService.getLocations();

                runOnUiThread(() -> {
                    ArrayAdapter<LocationDTO> adapter =
                            new ArrayAdapter<>(
                                    this,
                                    android.R.layout.simple_spinner_item,
                                    locations
                            );

                    adapter.setDropDownViewResource(
                            android.R.layout.simple_spinner_dropdown_item
                    );

                    locationSpinner.setAdapter(adapter);
                });

            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this,
                                "Erro ao carregar locais do servidor",
                                Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }

    private void publishAd() {

        String texto = adTextEditText.getText().toString().trim();
        if (texto.isEmpty()) {
            Toast.makeText(this, "Texto do anúncio é obrigatório", Toast.LENGTH_SHORT).show();
            return;
        }

        LocationDTO selected =
                (LocationDTO) locationSpinner.getSelectedItem();

        if (selected == null) {
            Toast.makeText(this, "Selecione um local", Toast.LENGTH_SHORT).show();
            return;
        }

        long localId = selected.id;

        String politica =
                policyTypeRadioGroup.getCheckedRadioButtonId() == R.id.radioWhitelist
                        ? "Whitelist"
                        : "Blacklist";

        String modoEntrega =
                deliveryModeRadioGroup.getCheckedRadioButtonId() == R.id.radioCentralized
                        ? "Centralizado"
                        : "Descentralizado";

        Map<String, String> restricoes = new HashMap<>();
        String kv = policyKeyValueEditText.getText().toString().trim();

        if (!kv.isEmpty()) {
            String[] parts = kv.split("=");
            if (parts.length != 2) {
                Toast.makeText(this, "Use Chave=Valor", Toast.LENGTH_LONG).show();
                return;
            }
            restricoes.put(parts[0].trim(), parts[1].trim());
        }

        // ⚠️ editor deve vir da sessão (exemplo simples)
        String editor = "admin"; // depois ligamos ao login real

        new Thread(() -> {
            try {
                AdService.publishAd(
                        texto,
                        editor,
                        localId,
                        System.currentTimeMillis(),
                        politica,
                        restricoes,
                        modoEntrega
                );

                runOnUiThread(() -> {
                    Toast.makeText(this,
                            "Anúncio publicado com sucesso!",
                            Toast.LENGTH_LONG).show();
                    finish();
                });

            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this,
                                "Erro ao publicar anúncio",
                                Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }
}

