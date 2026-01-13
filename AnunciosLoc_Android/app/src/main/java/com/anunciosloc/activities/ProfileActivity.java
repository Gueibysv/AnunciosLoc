package com.anunciosloc.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anunciosloc.R;
import com.anunciosloc.adapters.ProfileKeyAdapter;
import com.anunciosloc.data.MockDataSource;
import com.anunciosloc.models.Perfil;

public class ProfileActivity extends AppCompatActivity {

    private TextView usernameProfileTextView;
    private RecyclerView profileKeysRecyclerView;
    private EditText newKeyEditText;
    private Button addKeyButton;
    private Perfil currentUserProfile;
    private ProfileKeyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        currentUserProfile = MockDataSource.getCurrentUserProfile();
        if (currentUserProfile == null) {
            Toast.makeText(this, "Erro: Perfil de utilizador não encontrado.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        usernameProfileTextView = findViewById(R.id.usernameProfileTextView);
        profileKeysRecyclerView = findViewById(R.id.profileKeysRecyclerView);
        newKeyEditText = findViewById(R.id.newKeyEditText);
        addKeyButton = findViewById(R.id.addKeyButton);

        usernameProfileTextView.setText("Perfil de Utilizador: " + currentUserProfile.getUsername());

        profileKeysRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProfileKeyAdapter(currentUserProfile);
        profileKeysRecyclerView.setAdapter(adapter);

        addKeyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProfileKey();
            }
        });
    }

    private void addProfileKey() {
        String keyValueText = newKeyEditText.getText().toString().trim();
        if (keyValueText.isEmpty()) {
            Toast.makeText(this, "Insira a chave e o valor no formato Chave=Valor.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String[] parts = keyValueText.split("=");
            if (parts.length == 2) {
                String key = parts[0].trim();
                String value = parts[1].trim();

                if (key.isEmpty() || value.isEmpty()) {
                    Toast.makeText(this, "Chave e Valor não podem ser vazios.", Toast.LENGTH_SHORT).show();
                    return;
                }

                currentUserProfile.adicionarChave(key, value);
                adapter.updateData();
                newKeyEditText.setText("");
                Toast.makeText(this, "Chave de perfil adicionada: " + key, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Formato inválido. Use Chave=Valor.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao processar a chave.", Toast.LENGTH_LONG).show();
        }
    }
}
