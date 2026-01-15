package com.anunciosloc.activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anunciosloc.R;
import com.anunciosloc.dto.AdDTO;
import com.anunciosloc.network.AdService;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ViewAdActivity extends AppCompatActivity {

    private TextView adTextView;
    private TextView editorTextView;
    private TextView localTextView;
    private TextView policyTextView;
    private TextView deliveryTextView;
    private TextView timeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ad);

        adTextView = findViewById(R.id.adTextView);
        editorTextView = findViewById(R.id.editorTextView);
        localTextView = findViewById(R.id.localTextView);
        policyTextView = findViewById(R.id.policyTextView);
        deliveryTextView = findViewById(R.id.deliveryTextView);
        timeTextView = findViewById(R.id.timeTextView);

        long adId = getIntent().getLongExtra("AD_ID", -1);

        if (adId == -1) {
            Toast.makeText(this, "Anúncio inválido.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadAdDetails(adId);
    }

    private void loadAdDetails(long adId) {

        new Thread(() -> {
            try {
                AdDTO ad = AdService.getAdById(adId);

                runOnUiThread(() -> {
                    adTextView.setText(ad.texto);
                    editorTextView.setText("Editor: " + ad.editor);
                    localTextView.setText("Local: " + ad.localNome);
                    deliveryTextView.setText("Modo de entrega: " + ad.modoEntrega);

                    SimpleDateFormat sdf =
                            new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                    timeTextView.setText(
                            "Publicado em: " + sdf.format(ad.horaPublicacao)
                    );

                    if (ad.politicaRestricoes != null && !ad.politicaRestricoes.isEmpty()) {
                        policyTextView.setText(
                                "Política: " + ad.politicaTipo + " " + ad.politicaRestricoes
                        );
                    } else {
                        policyTextView.setText("Política: " + ad.politicaTipo);
                    }
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this,
                            "Erro ao carregar anúncio",
                            Toast.LENGTH_LONG).show();
                    finish();
                });
            }
        }).start();
    }
}
