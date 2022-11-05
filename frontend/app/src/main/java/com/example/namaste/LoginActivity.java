package com.example.namaste;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;


import java.io.IOException;

import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView username = findViewById(R.id.username);
        TextView password = findViewById(R.id.password);

        Button loginButton = findViewById(R.id.loginBtn);
        Button registerButton = findViewById(R.id.registerBtn);

        // Show plain action bar with only app name
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.show();

        loginButton.setOnClickListener(view -> {
            OkHttpPostRequest postReq = new OkHttpPostRequest();
            String msg = String.format("{\r\n    \"username\": \"%s\",\r\n    \"password\": \"%s\"\r\n}", username.getText().toString(), password.getText().toString());
            Log.d("message content: ", msg);
            Response response = postReq.doPostRequest(username.getText().toString(), password.getText().toString(), null, null, null, "users/login");
            //Log.d("got here", "got here!");
            if(response.toString().contains("200")) {
                Toast.makeText(LoginActivity.this, "Login success!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), MainActivity.class);
                intent.putExtra("EXTRA_SESSION_ID", response.headers().get("Set-Cookie"));
                try {
                    //boolean isAdmin = response.body().toString().contains("isAdmin=true"); // Check if this is correct syntax after backend implementation
                    boolean temp = username.getText().toString().equals("Admin"); // !!! REMOVE THIS LINE AFTER PROPER IMPLEMENTATION
                    intent.putExtra("IS_ADMIN", temp); // Pass admin status to MainActivity
                } catch (NullPointerException e){
                    e.printStackTrace();
                }
                intent.putExtra("USERNAME", username.getText().toString());
                Log.d("intent extras", intent.getExtras().toString());
                startActivity(intent);
            } else {
                Toast.makeText(LoginActivity.this, "login failed.", Toast.LENGTH_SHORT).show();
            }
        });

        registerButton.setOnClickListener(view -> {
            // Intent into a register page
            Intent intent = new Intent(view.getContext(), RegisterActivity.class);
            startActivity(intent);
        });
    }

}