package com.vedantkakade.liveasytask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerifyOTP extends AppCompatActivity {

    TextView tvVerificationOTPSendMessage;
    TextView tvInvalidOTP;
    TextView tvRequestAgain;

    EditText etInputOTP1;
    EditText etInputOTP2;
    EditText etInputOTP3;
    EditText etInputOTP4;
    EditText etInputOTP5;
    EditText etInputOTP6;

    StringBuffer OTPFromUser;

    String OTPFromBackEnd;
    String mobileNumber;
    String countryCode;

    Button btnVerifyOTP;

    ImageView ivBackButton;

    public static final int REQ_USER_CONSENT = 200;

    SMSBroadCastReceiver smsBroadCastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        initViews();

        initTextChangeListener();

        startSmartUserConsent();

        String verificationOTPSendMessage = "Code is sent to " + mobileNumber ;
        tvVerificationOTPSendMessage.setText(verificationOTPSendMessage);

        tvInvalidOTP.setVisibility(View.GONE);
        tvInvalidOTP.setTextColor(Color.RED);

        ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnVerifyOTP.setOnClickListener(v -> {
            tvInvalidOTP.setVisibility(View.GONE);

            if (OTPFromUser.length() != 6) {
                tvInvalidOTP.setText("Invalid OTP!");
                tvInvalidOTP.setVisibility(View.VISIBLE);
            }

            if (OTPFromBackEnd != null) {
                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(OTPFromBackEnd, OTPFromUser.toString());

                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(VerifyOTP.this, "OTP verified", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(VerifyOTP.this, ProfileSelection.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                tvInvalidOTP.setVisibility(View.VISIBLE);
                            }
                        });
            } else {
                Toast.makeText(VerifyOTP.this, "Please check Internet connection", Toast.LENGTH_SHORT).show();
            }
        });

        tvRequestAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+" + countryCode + mobileNumber,
                        60,
                        TimeUnit.SECONDS,
                        VerifyOTP.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                tvInvalidOTP.setText(e.getMessage());
                                tvInvalidOTP.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onCodeSent(@NonNull String newOTPFromFireBase, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                Toast.makeText(VerifyOTP.this, "OTP resent successfully!", Toast.LENGTH_SHORT).show();
                                OTPFromBackEnd = newOTPFromFireBase;
                            }
                        }
                );
            }
        });
    }

    private void startSmartUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(VerifyOTP.this);
        client.startSmsUserConsent(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) || data != null) {
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                getOTPFromMessage(message);
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void getOTPFromMessage(String message) {
        Pattern otpPatter = Pattern.compile("(|^)\\d{6}");

        Matcher matcher = otpPatter.matcher(message);
        if (matcher.find()) {
            String otp = matcher.group();

//            Toast.makeText(getApplicationContext(), String.valueOf(otp.length()), Toast.LENGTH_SHORT).show();

            etInputOTP1.setText(otp.charAt(0) + "");
            etInputOTP2.setText(otp.charAt(1) + "");
            etInputOTP3.setText(otp.charAt(2) + "");
            etInputOTP4.setText(otp.charAt(3) + "");
            etInputOTP5.setText(otp.charAt(4) + "");
            etInputOTP6.setText(otp.charAt(5) + "");
        }
    }

    private void registerBroadcastReceiver() {
        smsBroadCastReceiver = new SMSBroadCastReceiver();
        smsBroadCastReceiver.smsBroadCaseReceiverListener = new SMSBroadCastReceiver.SmsBroadCaseReceiverListener() {
            @Override
            public void onSuccess(Intent intent) {
                startActivityForResult(intent, REQ_USER_CONSENT);
            }

            @Override
            public void onFailure() {

            }
        };

        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadCastReceiver, intentFilter);
    }

    private void initTextChangeListener() {
        etInputOTP1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    OTPFromUser.setCharAt(0, etInputOTP1.getText().toString().charAt(0));
                    etInputOTP2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etInputOTP2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    OTPFromUser.setCharAt(1, etInputOTP2.getText().toString().charAt(0));
                    etInputOTP3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etInputOTP3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    OTPFromUser.setCharAt(2, etInputOTP3.getText().toString().charAt(0));
                    etInputOTP4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etInputOTP4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    OTPFromUser.setCharAt(3, etInputOTP4.getText().toString().charAt(0));
                    etInputOTP5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etInputOTP5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    OTPFromUser.setCharAt(4, etInputOTP5.getText().toString().charAt(0));
                    etInputOTP6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etInputOTP6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    OTPFromUser.setCharAt(5, etInputOTP6.getText().toString().charAt(0));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initViews() {
        tvVerificationOTPSendMessage = findViewById(R.id.tvVerificationOTPSendMessage);
        tvInvalidOTP = findViewById(R.id.tvInvalidOTP);
        tvRequestAgain = findViewById(R.id.tvRequestAgainOTP);

        etInputOTP1 = findViewById(R.id.etInputOTP1);
        etInputOTP2 = findViewById(R.id.etInputOTP2);
        etInputOTP3 = findViewById(R.id.etInputOTP3);
        etInputOTP4 = findViewById(R.id.etInputOTP4);
        etInputOTP5 = findViewById(R.id.etInputOTP5);
        etInputOTP6 = findViewById(R.id.etInputOTP6);

        btnVerifyOTP = findViewById(R.id.btnVerifyOTP);

        OTPFromBackEnd = getIntent().getStringExtra("OTP");
        mobileNumber = getIntent().getStringExtra("mobile");
        countryCode = getIntent().getStringExtra("countryCode");

        OTPFromUser = new StringBuffer("000000");

        ivBackButton = findViewById(R.id.ivBackButton);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadCastReceiver);
    }
}