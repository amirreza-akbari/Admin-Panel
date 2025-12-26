package com.mrbackend.admin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class AddEditUserActivity extends AppCompatActivity {

    EditText edtName, edtSurname, edtEmail, edtPassword, edtScore;
    Button btnSave;
    int userId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_user);

        edtName = findViewById(R.id.edtName);
        edtSurname = findViewById(R.id.edtSurname);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtScore = findViewById(R.id.edtScore);
        btnSave = findViewById(R.id.btnSave);

        if (getIntent().hasExtra("id")) {
            userId = getIntent().getIntExtra("id", -1);
            edtName.setText(getIntent().getStringExtra("name"));
            edtSurname.setText(getIntent().getStringExtra("surname"));
            edtEmail.setText(getIntent().getStringExtra("email"));
            edtScore.setText(getIntent().getStringExtra("score"));
        }

        btnSave.setOnClickListener(v -> saveUser());
    }

    private void saveUser() {
        String name = edtName.getText().toString().trim();
        String surname = edtSurname.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String score = edtScore.getText().toString().trim();

        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty() || score.isEmpty()) {
            Toast.makeText(this, "تمام فیلدها را پر کنید", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(() -> {
            try {
                String n = URLEncoder.encode(name, "UTF-8");
                String s = URLEncoder.encode(surname, "UTF-8");
                String e = URLEncoder.encode(email, "UTF-8");
                String p = URLEncoder.encode(password, "UTF-8");

                String userApi;
                String scoreApi;

                if (userId == -1) {
                    userApi =
                            "https://b.mrbackend.ir/api-admin/insert_user.php" +
                                    "?name=" + n +
                                    "&surname=" + s +
                                    "&email=" + e +
                                    "&password=" + p;
                } else {
                    userApi =
                            "https://b.mrbackend.ir/api-admin/update_user.php" +
                                    "?id=" + userId +
                                    "&name=" + n +
                                    "&surname=" + s +
                                    "&email=" + e +
                                    "&password=" + p;
                }

                scoreApi =
                        "https://b.mrbackend.ir/api-admin/insert_score.php" +
                                "?name=" + n +
                                "&surname=" + s +
                                "&email=" + e +
                                "&score=" + score;

                HttpURLConnection conn1 = (HttpURLConnection) new URL(userApi).openConnection();
                conn1.getInputStream();

                HttpURLConnection conn2 = (HttpURLConnection) new URL(scoreApi).openConnection();
                conn2.getInputStream();

                runOnUiThread(() -> {
                    Toast.makeText(this, "کاربر و نمره با موفقیت ذخیره شدند", Toast.LENGTH_SHORT).show();
                    finish();
                });

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }
}