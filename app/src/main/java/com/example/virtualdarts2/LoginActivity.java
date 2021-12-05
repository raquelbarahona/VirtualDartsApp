package com.example.virtualdarts2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText username, password;
    Button btnlogin, btnsignup;
    DBUser DB;
    String user_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username1);
        password = (EditText) findViewById((R.id.password1));
        btnlogin = (Button) findViewById((R.id.btnsignin1));
        btnsignup = (Button) findViewById((R.id.btn_signup));
        DB = new DBUser(this);

        // listener for login button
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                user_string = user;
                String pas = password.getText().toString();

                // do hashing and create entry
                String pass = DB.md5(pas);
                UserIDEntry userIDEntry = new UserIDEntry(user_string, pass);

                if(user.equals("") || pas.equals("") || pas.equals("")) { // missing fields
                    Toast.makeText(LoginActivity.this, "Please enter all requested fields",
                            Toast.LENGTH_SHORT).show();
                }
                else { // check if valid
                    Boolean checkUserPass = DB.checkUsernamePassword(userIDEntry);
                    if(checkUserPass) { // username and password exist
                        Toast.makeText(LoginActivity.this, "Welcome!",
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.putExtra("current_user", user_string);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Invalid credentials",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // listener for sign up/ create an account button
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // takes you to create an account
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}