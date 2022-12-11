package com.vedantkakade.liveasytask;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class WelcomePage extends AppCompatActivity {

    TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        tvWelcome = findViewById(R.id.tvWelcome);

        String welcomeMessage = "Welcome to " + getIntent().getStringExtra("profile") + " Community";
        tvWelcome.setText(welcomeMessage);
    }
}