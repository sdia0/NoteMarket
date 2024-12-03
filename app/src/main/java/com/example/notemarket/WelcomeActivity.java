package com.example.notemarket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WelcomeActivity extends AppCompatActivity {
    EditText etLogin, etPassword;
    Button btnLogin, btnContinue;
    private final String adminLogin = "admin", adminPassword = "admin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnContinue = findViewById(R.id.btnContinue);

        // Получаем сохраненные данные о пользователе
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

        // Если пользователь авторизован, сразу переходим в MainActivity
        if (isLoggedIn) {
            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            finish(); // Закрываем WelcomeActivity
            return;
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(etLogin.getText().toString(), etPassword.getText().toString());
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isLoggedIn", true);
                editor.apply();
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            }
        });
    }
    private void loginUser(String login, String password) {
        if (login.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, введите логин и пароль", Toast.LENGTH_SHORT).show();
            return;
        }

        if (adminLogin.equals(login) && adminPassword.equals(password)) {
            SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isLoggedIn", true);
            editor.apply();
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            intent.putExtra("isAdmin", true);
            startActivity(intent);
        }
        else Toast.makeText(this, "Неправильный логин или пароль", Toast.LENGTH_SHORT).show();
    }
}