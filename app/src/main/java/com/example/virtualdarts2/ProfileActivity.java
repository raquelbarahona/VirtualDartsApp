package com.example.virtualdarts2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    EditText firstNameET, lastNameET, ageET;
    TextView firstNameTV, lastNameTV, ageTV;
    Button btnSaveChanges, btnHome, btnEdit;
    DBProfile dbProfile;
    String user_ID;

    // visibility of textviews and edittexts
    void visibilityWhenLaunch() {
        // ET
        firstNameET.setVisibility(View.INVISIBLE);
        lastNameET.setVisibility(View.INVISIBLE);
        ageET.setVisibility(View.INVISIBLE);
        // TV
        /* already visible */

        // BTN
        btnSaveChanges.setVisibility(View.INVISIBLE); // make invisible
    }

    void visibilityWhenEdit() {
        // ET
        firstNameET.setVisibility(View.VISIBLE);
        lastNameET.setVisibility(View.VISIBLE);
        ageET.setVisibility(View.VISIBLE);
        // TV
        firstNameTV.setVisibility(View.INVISIBLE); // make invisible
        lastNameTV.setVisibility(View.INVISIBLE); // make invisible
        ageTV.setVisibility(View.INVISIBLE); // make invisible
        // BTN
        btnEdit.setVisibility(View.INVISIBLE);
        btnSaveChanges.setVisibility(View.VISIBLE);

    }

    void visibilityAfterSave() {
        // ET
        firstNameET.setVisibility(View.INVISIBLE);
        lastNameET.setVisibility(View.INVISIBLE);
        ageET.setVisibility(View.INVISIBLE);
        // TV
        firstNameTV.setVisibility(View.VISIBLE);
        lastNameTV.setVisibility(View.VISIBLE);
        ageTV.setVisibility(View.VISIBLE);
        // BTN
        btnEdit.setVisibility(View.VISIBLE);
        btnSaveChanges.setVisibility(View.INVISIBLE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Edit text
        firstNameET = (EditText) findViewById(R.id.editTextFirstName);
        lastNameET = (EditText) findViewById(R.id.editTextLastName);
        ageET = (EditText) findViewById(R.id.editTextAge);
        // text view
        firstNameTV = (TextView) findViewById(R.id.firstNameTV);
        lastNameTV = (TextView) findViewById(R.id.lastNameTV);
        ageTV = (TextView) findViewById(R.id.ageTV);
        // buttons
        btnSaveChanges = (Button) findViewById((R.id.btnSaveChanges));
        btnHome = (Button) findViewById((R.id.btnHome));
        btnEdit = (Button) findViewById(R.id.btnEdit);

        visibilityWhenLaunch(); // change visibility

        // database stuff
        dbProfile = new DBProfile(this);

        // user_ID from other activity
        user_ID = getIntent().getStringExtra("current_user");
        if(user_ID == null)
            user_ID = "user_ID";

        // if it exists, retrieve and set TVs and ETs to it
        Boolean userDataExists = dbProfile.checkProfileEntry(user_ID);
       if(userDataExists) {
            ProfileEntry userData = new ProfileEntry();
            userData = dbProfile.getProfileEntry(user_ID);
            Log.d("_FIRST_NAME_TAG_", dbProfile.getProfileEntry(user_ID).getFirst_name());

            firstNameTV.setText(dbProfile.getProfileEntry(user_ID).getFirst_name());
            lastNameTV.setText(dbProfile.getProfileEntry(user_ID).getLast_name());
            ageTV.setText(String.valueOf(dbProfile.getProfileEntry(user_ID).getAge())); // THIS!!!!!!

            firstNameET.setText(dbProfile.getProfileEntry(user_ID).getFirst_name());
            lastNameET.setText(dbProfile.getProfileEntry(user_ID).getLast_name());
            ageET.setText(String.valueOf(dbProfile.getProfileEntry(user_ID).getAge()));
       }

        // BUTTON LISTENERS
        // will take you back to the home page
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("current_user", user_ID);
                startActivity(intent);
            }
        });

        // makes edittext visible to edit profile data
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                visibilityWhenEdit(); // change visibility
                Boolean userDataExists = dbProfile.checkProfileEntry(user_ID);
                // if it exists, retrieve and set TVs and ETs to it
                if(userDataExists) {
                    ProfileEntry userData = dbProfile.getProfileEntry(user_ID);
                    //firstNameTV.setText("Raquel");
                    firstNameTV.setText(dbProfile.getProfileEntry(user_ID).getFirst_name());
                    lastNameTV.setText(dbProfile.getProfileEntry(user_ID).getLast_name());
                    ageTV.setText(String.valueOf(dbProfile.getProfileEntry(user_ID).getAge())); // THIS!!!!!!

                    firstNameET.setText(dbProfile.getProfileEntry(user_ID).getFirst_name());
                    lastNameET.setText(dbProfile.getProfileEntry(user_ID).getLast_name());
                    ageET.setText(String.valueOf(dbProfile.getProfileEntry(user_ID).getAge()));
                }
            }
        });

        // once changes have been made, save to database
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = firstNameET.getText().toString();
                String lastName = lastNameET.getText().toString();
                String sAge = ageET.getText().toString();
                int age = Integer.parseInt(sAge);
                ProfileEntry profileEntry = new ProfileEntry(user_ID, firstName, lastName, age);

                // check if entry exists
                Boolean entryExists = dbProfile.checkProfileEntry(user_ID);

                // insert if entry does not exist
                if(!entryExists) {
                    Boolean insert = dbProfile.insertProfileEntry(profileEntry);
                    if(insert) {
                        Toast.makeText(getApplicationContext(), "Profile updated!",
                                Toast.LENGTH_SHORT).show();

                        visibilityAfterSave(); // change visibility
                        Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                        intent.putExtra("current_user", user_ID);
                        startActivity(intent); // takes you to back to updated profile page
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Failed to update profile",
                                Toast.LENGTH_SHORT).show();

                    }
                }

                // does exist, update
                else {
                    Boolean update = dbProfile.updateProfileEntry(profileEntry);
                    if(update) {
                        Toast.makeText(getApplicationContext(), "Profile updated!",
                                Toast.LENGTH_SHORT).show();

                        visibilityAfterSave(); // change visibility
                        Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                        intent.putExtra("current_user", user_ID);
                        startActivity(intent); // takes you to back to updated profile page
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Failed to update profile",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}