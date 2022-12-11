package com.vedantkakade.liveasytask;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ProfileSelection extends AppCompatActivity {

    Button btnSelectProfile;

    RadioGroup radio;

    LinearLayout shipper;
    LinearLayout transporter;

    RadioButton radioButtonShipper;
    RadioButton radioButtonTransporter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_selection);

        initView();

        shipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio.clearCheck();
                radio.check(radioButtonShipper.getId());

            }
        });

        transporter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio.clearCheck();
                radio.check(radioButtonTransporter.getId());

            }
        });

        btnSelectProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String profile = "";

                switch (radio.getCheckedRadioButtonId()) {
                    case R.id.radioButtonShipper:
                        profile = "Shipping";
                        break;

                    case R.id.radioButtonTransporter:
                        profile = "Transporting";
                        break;
                }

                Intent intent = new Intent(ProfileSelection.this, WelcomePage.class);
                intent.putExtra("profile", profile);
                startActivity(intent);
            }
        });

        radioButtonTransporter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButtonTransporter.setChecked(true);
                radioButtonShipper.setChecked(false);
            }
        });

        radioButtonShipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioButtonTransporter.setChecked(false);
                radioButtonShipper.setChecked(true);
            }
        });
    }

    private void initView() {
        btnSelectProfile = findViewById(R.id.btnSelectProfile);

        radio = findViewById(R.id.radio);

        shipper = findViewById(R.id.shipper);
        transporter = findViewById(R.id.transporter);

        radioButtonShipper = findViewById(R.id.radioButtonShipper);
        radioButtonTransporter = findViewById(R.id.radioButtonTransporter);
    }
}