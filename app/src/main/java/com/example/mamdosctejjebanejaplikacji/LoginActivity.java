package com.example.mamdosctejjebanejaplikacji;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_MamDoscTejJebanejAplikacji);
        setContentView(R.layout.activity_login);
        EditText nameEdit = findViewById(R.id.editName);
        EditText passwordEdit = findViewById(R.id.editPassword);
        Button loginBtn = findViewById(R.id.loginBtn);
        Context context = this;
        String savedName = getSharedPreferences("user_prefs", Context.MODE_PRIVATE).getString("user_name", null);
        String savedPassword = getSharedPreferences("user_prefs", Context.MODE_PRIVATE).getString("user_password", null);
        if (savedName != null && savedPassword != null) {
            startMain();
            finish();
        }
        loginBtn.setOnClickListener(v -> {
            String name = nameEdit.getText().toString().trim();
            String password = passwordEdit.getText().toString().trim();
            if (name.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Enter name and password", Toast.LENGTH_SHORT).show();
            } else {
                // TODO: Replace with Java async DB or background thread for DB check
                // For now, just save to prefs for demo
                getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                        .edit().putString("user_name", name).putString("user_password", password).apply();
                startMain();
                finish();
            }
        });
    }
    private void startMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
