package com.anunciosloc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anunciosloc.R;
import com.anunciosloc.data.MockDataSource;
import com.anunciosloc.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Se já estiver logado, redireciona para a tela principal
        if (SessionManager.isLoggedIn()) {
            navigateToMainActivity();
            return;
        }

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performRegistration();
            }
        });
    }

    private void performLogin() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        String sessionId = MockDataSource.login(username, password);

        if (sessionId != null) {
            SessionManager.createSession(sessionId);
            Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
            navigateToMainActivity();
        } else {
            Toast.makeText(this, "Credenciais inválidas.", Toast.LENGTH_LONG).show();
        }
    }

    private void performRegistration() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos para registo.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean success = MockDataSource.registerUser(username, password);

        if (success) {
            Toast.makeText(this, "Registo bem-sucedido! Faça login agora.", Toast.LENGTH_LONG).show();
            // Limpa os campos para o login
            passwordEditText.setText("");
        } else {
            Toast.makeText(this, "Nome de utilizador já existe.", Toast.LENGTH_LONG).show();
        }
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Fecha a tela de login para que o utilizador não possa voltar
    }
}
