package com.example.virtualdarts2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class ProfileActivity extends AppCompatActivity {

    EditText firstNameET, lastNameET, ageET;
    TextView firstNameTV, lastNameTV, ageTV;
    ImageButton btnSaveChanges, btnHome, btnEdit, btnPic;
    ImageView profilePic;
    CardView card;
    DBProfile dbProfile;
    String user_ID;

    private static int RESULT_LOAD_IMAGE = 1; // for image stuff

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
        btnSaveChanges = (ImageButton) findViewById((R.id.btnSaveChanges));
        btnHome = (ImageButton) findViewById((R.id.btnHome));
        btnEdit = (ImageButton) findViewById(R.id.btnEdit);
        btnPic = (ImageButton) findViewById(R.id.btnPic);

        // image
        card = (CardView) findViewById(R.id.picCard);
        profilePic = (ImageView) findViewById(R.id.profilePicture);

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

            checkIfPic();
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

                // check if entry exists
                Boolean entryExists = dbProfile.checkProfileEntry(user_ID);

                // insert if entry does not exist
                if(!entryExists) { // doesn't exist so no image path either!
                    // create it and insert it
                    ProfileEntry profileEntry = new ProfileEntry(user_ID, firstName, lastName,
                            age, null); // pic path is null, checked later to confirm it exists
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

                // does exist, and image exists but might be null
                else {
                    String path = dbProfile.getProfileEntry(user_ID).getPic_path(); // might be null
                    ProfileEntry profileEntry = new ProfileEntry(user_ID, firstName,
                            lastName, age, path);
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


        // for pic stuff
        // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
        // receiver
        ActivityResultLauncher<Intent> profileActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes (why???)
                            Intent data = result.getData();
                            //doSomeOperations();
                            Uri selectedImage = data.getData();
                            String[] filePathColumn = { MediaStore.Images.Media.DATA };
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            String picturePath = cursor.getString(columnIndex);
                            cursor.close();
                            // String picturePath contains the path of selected Image
                            // set image
                            profilePic.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                            // save image in database, check if it exists first
                            if(userDataExists) {
                                Boolean update = dbProfile.updateProfilePic(picturePath, user_ID);
                                if(update) {
                                    Toast.makeText(getApplicationContext(), "Profile picture updated!",
                                            Toast.LENGTH_SHORT).show();

                                    visibilityAfterSave(); // change visibility
                                    Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                                    intent.putExtra("current_user", user_ID);
                                    startActivity(intent); // takes you to back to updated profile page
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Failed to " +
                                                    "update profile picture", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else { // create entry with default names
                                // create it and insert it
                                ProfileEntry profileEntry = new ProfileEntry(user_ID, "First", "Last",
                                        0, picturePath); // picture path
                                Boolean insert = dbProfile.insertProfileEntry(profileEntry);
                                if(insert) {
                                    Toast.makeText(getApplicationContext(), "Profile picture updated!",
                                            Toast.LENGTH_SHORT).show();

                                    visibilityAfterSave(); // change visibility
                                    Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                                    intent.putExtra("current_user", user_ID);
                                    startActivity(intent); // takes you to back to updated profile page
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "Failed to update" +
                                                    "profile picture", Toast.LENGTH_SHORT).show();
                                }
                            }

                            // change text of pic button
                            //btnPic.setText("Edit");

                        }
                    }
                });

        // to edit profile picture
        btnPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // change text of edit pic button
                Boolean userDataExists = dbProfile.checkProfileEntry(user_ID);
                // if it exists, retrieve and set TVs and ETs to it
                if(userDataExists) {
                    ProfileEntry userData = dbProfile.getProfileEntry(user_ID);
                    // display profile picture
                }
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore
                        .Images.Media.EXTERNAL_CONTENT_URI);
                //startActivityForResult(intent, RESULT_LOAD_IMAGE);
                profileActivityResultLauncher.launch(intent);
            }
        });


    }
}