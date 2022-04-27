package com.example.virtualdarts2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class HomeActivity extends AppCompatActivity {

    TextView welcome_user_ID;
    Button btnProfile, btnStats, btnModes,
            btnLogOut, btnBluetooth, btnLDB;
    ImageView profilePic;
    CardView card;
    DBProfile dbProfile;
    String user_ID;

    // check if the picture path exists (profile pic isn't default)
    void checkIfPic() {
        File imgFile;
        if (dbProfile.getProfileEntry(user_ID).getPic_path() != null) {
            imgFile = new File(dbProfile.getProfileEntry(user_ID).getPic_path());
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                profilePic.setImageBitmap(myBitmap);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // sets welcome to current user_ID
        welcome_user_ID = (TextView)findViewById(R.id.welcome_username);

        user_ID = getIntent().getStringExtra("current_user");
        if(user_ID == null)
            user_ID = "user_ID";

        String welcome_user_ID_string = "Welcome, " +  '\n' + user_ID;
        welcome_user_ID.setText(welcome_user_ID_string);

        // button initialization
        btnProfile = (Button) findViewById(R.id.btn_profile);
        btnStats = (Button) findViewById(R.id.btn_mystats);
        btnModes = (Button) findViewById(R.id.btn_game_modes);
        btnLogOut = (Button) findViewById(R.id.btnLogOut);
        btnBluetooth = (Button) findViewById(R.id.btnBluetoothHome);
        btnLDB = (Button) findViewById(R.id.btnLDB);

        // image
        card = (CardView) findViewById(R.id.picCardHome);
        profilePic = (ImageView) findViewById(R.id.proPicHome);

        // database stuff
        dbProfile = new DBProfile(this);

        // display profile picture if it exists
        // if it exists, retrieve and set TVs and ETs to it
        Boolean userDataExists = dbProfile.checkProfileEntry(user_ID);
        if(userDataExists) {
            checkIfPic();
        }

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
                Intent intent = new Intent(HomeActivity.this, StatsBActivity.class);
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
                //Intent intent = new Intent(HomeActivity.this, UpdatesActivity.class);
                Intent intent = new Intent(HomeActivity.this, UpdatesActivity.class);
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

        // takes you to leaderboard page
        btnLDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, LeaderboardActivity.class);
                intent.putExtra("current_user", user_ID);
                startActivity(intent);
            }
        });
    }
}
