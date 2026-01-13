package com.anunciosloc.activities;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anunciosloc.R;
import com.anunciosloc.data.MockDataSource;
import com.anunciosloc.models.Anuncio;

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

        String adId = getIntent().getStringExtra("AD_ID");
        if (adId != null) {
            loadAdDetails(adId);
        } else {
            Toast.makeText(this, "Anúncio não encontrado.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadAdDetails(String adId) {
        Anuncio anuncio = MockDataSource.getAdById(adId);

        if (anuncio != null) {
            adTextView.setText(anuncio.getTexto());
            editorTextView.setText("Editor: " + anuncio.getEditor());
            localTextView.setText("Local de Destino: " + anuncio.getLocalDestinoId());
            deliveryTextView.setText("Modo de Entrega: " + anuncio.getModoEntrega());

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            timeTextView.setText("Publicado em: " + sdf.format(anuncio.getHoraPublicacao()));

            String policy = anuncio.getPoliticaTipo() + ": " + anuncio.getPoliticaRestricoes().toString();
            policyTextView.setText("Política: " + policy);

            // Simulação de "Receber Mensagem" - o anúncio já está disponível para visualização
            // Em um cenário real, haveria um botão "Receber" aqui.
        } else {
            Toast.makeText(this, "Anúncio não encontrado.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
