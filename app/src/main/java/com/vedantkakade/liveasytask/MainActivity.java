package com.vedantkakade.liveasytask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnContinueToEnterMobileNumber;

    String[] languages = {"English", "Hindi", "Marathi", "Gujarati", "Tamil"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnContinueToEnterMobileNumber = findViewById(R.id.btnContinueToEnterMobileNumber);
        btnContinueToEnterMobileNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EnterMobileNumber.class));
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, languages);
        AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.application_language_selector);
        actv.setThreshold(1);
        actv.setAdapter(adapter);
    }
}