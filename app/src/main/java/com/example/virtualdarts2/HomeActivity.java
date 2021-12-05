package com.example.virtualdarts2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    TextView welcome_user_ID;
    Button btnProfile, btnStats, btnModes, btnLogOut, btnBluetooth;
    String user_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // sets welcome to current user_ID
        welcome_user_ID = (TextView)findViewById(R.id.welcome_username);

        user_ID = getIntent().getStringExtra("current_user");
        if(user_ID == null)
            user_ID = "user_ID";

        String welcome_user_ID_string = "Welcome, " +  user_ID;
        welcome_user_ID.setText(welcome_user_ID_string);

        // button initialization
        btnProfile = (Button) findViewById(R.id.btn_profile);
        btnStats = (Button) findViewById(R.id.btn_mystats);
        btnModes = (Button) findViewById(R.id.btn_game_modes);
        btnLogOut = (Button) findViewById(R.id.btnLogOut);
        btnBluetooth = (Button) findViewById(R.id.btnBluetoothHome);

        // takes you to profile page
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                intent.putExtra("current_user", user_ID);
                startActivity(intent);
            }
        });

        // takes you to statistics page
        btnStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, StatisticsActivity.class);
                intent.putExtra("current_user", user_ID);
                startActivity(intent);
            }
        });

        // takes you back to login page
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        // takes you to bluetooth page
        btnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, BluetoothActivity.class);
                intent.putExtra("current_user", user_ID);
                startActivity(intent);
            }
        });

        // takes you to bluetooth page
        btnModes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, GameModesActivity.class);
                intent.putExtra("current_user", user_ID);
                startActivity(intent);
            }
        });
    }
}
