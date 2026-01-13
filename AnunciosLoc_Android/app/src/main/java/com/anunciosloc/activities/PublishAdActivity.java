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
import com.anunciosloc.models.Anuncio;
import com.anunciosloc.models.Local;

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

    private List<Local> availableLocations;

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

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishAd();
            }
        });
    }

    private void loadLocations() {
        availableLocations = MockDataSource.getLocations();
        ArrayAdapter<Local> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, availableLocations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);
    }

    private void publishAd() {
        String adText = adTextEditText.getText().toString().trim();
        if (adText.isEmpty()) {
            Toast.makeText(this, "O texto do anúncio é obrigatório.", Toast.LENGTH_SHORT).show();
            return;
        }

        Local selectedLocal = (Local) locationSpinner.getSelectedItem();
        if (selectedLocal == null) {
            Toast.makeText(this, "Selecione um local de destino.", Toast.LENGTH_SHORT).show();
            return;
        }

        String policyType = policyTypeRadioGroup.getCheckedRadioButtonId() == R.id.radioWhitelist ? "Whitelist" : "Blacklist";
        String deliveryMode = deliveryModeRadioGroup.getCheckedRadioButtonId() == R.id.radioCentralized ? "Centralizado" : "Descentralizado";

        Map<String, String> policyRestrictions = new HashMap<>();
        String keyValueText = policyKeyValueEditText.getText().toString().trim();
        if (!keyValueText.isEmpty()) {
            try {
                String[] parts = keyValueText.split("=");
                if (parts.length == 2) {
                    policyRestrictions.put(parts[0].trim(), parts[1].trim());
                } else {
                    Toast.makeText(this, "Formato de restrição inválido. Use Chave=Valor.", Toast.LENGTH_LONG).show();
                    return;
                }
            } catch (Exception e) {
                Toast.makeText(this, "Erro ao processar restrição.", Toast.LENGTH_LONG).show();
                return;
            }
        }

        String adId = UUID.randomUUID().toString();
        String editor = MockDataSource.getCurrentUserProfile() != null ? MockDataSource.getCurrentUserProfile().getUsername() : "Desconhecido";

        Anuncio newAd = new Anuncio(adId, adText, editor, selectedLocal.getId(), System.currentTimeMillis(), policyType, policyRestrictions, deliveryMode);

        MockDataSource.addAd(newAd);
        Toast.makeText(this, "Anúncio publicado com sucesso!", Toast.LENGTH_LONG).show();
        finish();
    }
}
