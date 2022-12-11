package com.vedantkakade.liveasytask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class EnterMobileNumber extends AppCompatActivity {

    EditText etMobileNumber;

    Button btnRequestOTP;

    CountryCodePicker countryCodePicker;

    TextView tvError;

    ImageView ivCloseButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_mobile_number);

        etMobileNumber = findViewById(R.id.etMobileNumber);
        etMobileNumber.requestFocus();

        countryCodePicker = findViewById(R.id.countryCodeHolder);

        tvError = findViewById(R.id.tvError);

        ivCloseButton = findViewById(R.id.ivCloseButton);
        ivCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });


        btnRequestOTP = findViewById(R.id.btnRequestOTP);
        btnRequestOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobileNumber = etMobileNumber.getText().toString();
                if (mobileNumber.trim().isEmpty() || mobileNumber.length() != 10) {
                    Toast.makeText(EnterMobileNumber.this, "Please enter correct mobile number", Toast.LENGTH_SHORT).show();
                } else {
//                    Request OTP

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+" + countryCodePicker.getSelectedCountryCode() + mobileNumber,
                            60,
                            TimeUnit.SECONDS,
                            EnterMobileNumber.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    tvError.setText(String.format("Error: %s", e.getMessage()));
                                    tvError.setTextColor(Color.RED);
                                }

                                @Override
                                public void onCodeSent(@NonNull String OTPFromFireBase, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    Intent i = new Intent(EnterMobileNumber.this, VerifyOTP.class);
                                    i.putExtra("mobile", mobileNumber);
                                    i.putExtra("OTP", OTPFromFireBase);
                                    i.putExtra("countryCode", countryCodePicker.getSelectedCountryCode());

                                    startActivity(i);
                                }
                            }
                    );
//                    Toast.makeText(getApplicationContext(), countryCodePicker.getSelectedCountryCode(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}