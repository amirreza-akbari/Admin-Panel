package com.mrbackend.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerUsers;
    private UserAdapter adapter;
    private ArrayList<User> users = new ArrayList<>();
    private Button btnAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerUsers = findViewById(R.id.recyclerUsers);
        btnAddUser = findViewById(R.id.aad);

        recyclerUsers.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdapter(this, users);
        recyclerUsers.setAdapter(adapter);

        btnAddUser.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, AddEditUserActivity.class));
        });

        loadUsers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUsers();
    }

    private void loadUsers() {
        new Thread(() -> {
            try {
                URL url = new URL("https://b.mrbackend.ir/api-admin/get_users.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream is = conn.getInputStream();
                StringBuilder sb = new StringBuilder();
                int b;
                while((b = is.read()) != -1) sb.append((char)b);
                String response = sb.toString();

                JSONObject json = new JSONObject(response);
                if(json.getString("status").equals("success")){
                    JSONArray arr = json.getJSONArray("users");
                    users.clear();
                    for(int i = 0; i < arr.length(); i++){
                        JSONObject u = arr.getJSONObject(i);
                        users.add(new User(
                                u.getInt("id"),
                                u.getString("name"),
                                u.getString("surname"),
                                u.getString("email"),
                                u.getString("score"),
                                u.getString("created_at")
                        ));
                    }
                    runOnUiThread(() -> adapter.notifyDataSetChanged());
                } else {
                    runOnUiThread(() -> Toast.makeText(this, "خطا در دریافت کاربران", Toast.LENGTH_SHORT).show());
                }

            } catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }

    public void deleteUser(int id){
        new Thread(() -> {
            try {
                URL url = new URL("https://b.mrbackend.ir/api-admin/delete_user.php?id=" + id);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.getInputStream();
                runOnUiThread(() -> {
                    Toast.makeText(this, "کاربر حذف شد", Toast.LENGTH_SHORT).show();
                    loadUsers();
                });
            } catch (Exception e){
                e.printStackTrace();
            }
        }).start();
    }
}
