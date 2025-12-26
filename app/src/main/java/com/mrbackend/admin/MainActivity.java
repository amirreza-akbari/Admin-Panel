package com.mrbackend.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    EditText edtEmail;
    Button btnSendOtp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail = findViewById(R.id.edtEmail);
        btnSendOtp = findViewById(R.id.btnSendOtp);

        edtEmail.setEnabled(false);
        btnSendOtp.setEnabled(false);

        startFingerprint();

        btnSendOtp.setOnClickListener(v -> {
            String email = edtEmail.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "ایمیل را وارد کنید", Toast.LENGTH_SHORT).show();
                return;
            }
            sendOtp(email);
        });
    }

    private void startFingerprint() {

        BiometricManager biometricManager = BiometricManager.from(this);
        if (biometricManager.canAuthenticate(
                BiometricManager.Authenticators.BIOMETRIC_STRONG)
                != BiometricManager.BIOMETRIC_SUCCESS) {

            Toast.makeText(this,
                    "اثر انگشت فعال نیست",
                    Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Executor executor = ContextCompat.getMainExecutor(this);

        BiometricPrompt biometricPrompt =
                new BiometricPrompt(this, executor,
                        new BiometricPrompt.AuthenticationCallback() {

                            @Override
                            public void onAuthenticationSucceeded(
                                    BiometricPrompt.AuthenticationResult result) {
                                super.onAuthenticationSucceeded(result);

                                runOnUiThread(() -> {
                                    Toast.makeText(MainActivity.this,
                                            "احراز هویت موفق",
                                            Toast.LENGTH_SHORT).show();

                                    edtEmail.setEnabled(true);
                                    btnSendOtp.setEnabled(true);
                                });
                            }

                            @Override
                            public void onAuthenticationFailed() {
                                super.onAuthenticationFailed();
                                Toast.makeText(MainActivity.this,
                                        "اثر انگشت نادرست",
                                        Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAuthenticationError(
                                    int errorCode, CharSequence errString) {
                                super.onAuthenticationError(errorCode, errString);
                                finish();
                            }
                        });

        BiometricPrompt.PromptInfo promptInfo =
                new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("ورود ایمن")
                        .setSubtitle("لطفاً اثر انگشت خود را وارد کنید")
                        .setNegativeButtonText("خروج")
                        .build();

        biometricPrompt.authenticate(promptInfo);
    }

    private void sendOtp(String email) {
        new Thread(() -> {
            try {
                URL url = new URL("https://b.mrbackend.ir/api/send_otp.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                String data = "email=" + email;
                OutputStream os = conn.getOutputStream();
                os.write(data.getBytes());
                os.flush();
                os.close();

                if (conn.getResponseCode() == 200) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "کد ارسال شد", Toast.LENGTH_SHORT).show();
                        Intent intent =
                                new Intent(MainActivity.this, VerifyOtpActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    });
                } else {
                    runOnUiThread(() ->
                            Toast.makeText(this,
                                    "خطا در ارسال کد",
                                    Toast.LENGTH_SHORT).show());
                }

            } catch (Exception e) {
                runOnUiThread(() ->
                        Toast.makeText(this,
                                "خطا: " + e.getMessage(),
                                Toast.LENGTH_SHORT).show());
            }
        }).start();
    }
}
