package com.anunciosloc.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anunciosloc.R;
import com.anunciosloc.dto.UserDTO;
import com.anunciosloc.network.AuthService;
import com.anunciosloc.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LOGIN_FLOW";

    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Log.d(TAG, "LoginActivity criada");

        if (SessionManager.isLoggedIn(this)) {
            Log.d(TAG, "Utilizador já logado, indo para MainActivity");
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            Log.d(TAG, "Botão LOGIN clicado");
            login();
        });
        Button registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> {
            Log.d(TAG, "Botão REGISTER clicado");
            register();
        });
    }

    private void login() {

        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        Log.d(TAG, "Login clicked: " + username);

        new Thread(() -> {
            try {
                UserDTO user = AuthService.login(username, password);

                runOnUiThread(() -> {
                    SessionManager.createSession(this, user.userId, user.username);
                    Toast.makeText(this, "Login OK", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                });

            } catch (Exception e) {
                Log.e(TAG, "LOGIN ERROR", e);
                runOnUiThread(() ->
                        Toast.makeText(this, "Erro: " + e.getClass().getSimpleName(), Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }

    private void register() {

        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "Register iniciado: " + username);

        new Thread(() -> {
            try {
                UserDTO user = AuthService.register(username, password);

                runOnUiThread(() -> {
                    SessionManager.createSession(this, user.userId, user.username);
                    Toast.makeText(this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                });

            } catch (Exception e) {
                Log.e(TAG, "REGISTER ERROR", e);
                runOnUiThread(() ->
                        Toast.makeText(this, "Erro ao registar", Toast.LENGTH_LONG).show()
                );
            }
        }).start();
    }

}
