package com.example.virtualdarts2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RegisterActivity extends AppCompatActivity {

    EditText username, password, repassword;
    Button signup, signin;
    DBUser DB;
    boolean goodToRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        repassword = (EditText) findViewById(R.id.repassword);
        signup = (Button) findViewById(R.id.btnsignup);
        signin = (Button) findViewById(R.id.btnsignin);
        DB = new DBUser(this);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pas = password.getText().toString();
                String repas = repassword.getText().toString();

                // check strength of password
                String strength = checkStrength(pas);
                if(strength.equals("Weak")) {
                    Toast.makeText(RegisterActivity.this, "Your password is too weak. Try again",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    register(pas, repas, user);
                }
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    public String checkStrength(String input) {
        // Checking lower alphabet in string
        int n = input.length();
        boolean hasLower = false, hasUpper = false,
                hasDigit = false, specialChar = false;
        Set<Character> set = new HashSet<Character>(
                Arrays.asList('!', '@', '#', '$', '%', '^', '&',
                        '*', '(', ')', '-', '+'));
        for (char i : input.toCharArray())
        {
            if (Character.isLowerCase(i))
                hasLower = true;
            if (Character.isUpperCase(i))
                hasUpper = true;
            if (Character.isDigit(i))
                hasDigit = true;
            if (set.contains(i))
                specialChar = true;
        }

        // Strength of password
        System.out.print("Strength of password:- ");
        if (hasDigit && hasLower && hasUpper && specialChar
                && (n >= 8))
            return "Strong";
        else if ((hasLower || hasUpper || specialChar)
                && (n >= 6))
            return "Moderate";
        else
            return "Weak";
    }

    public void register(String pas, String repas, String user) {
        // password is strong enough, do hashing and create entry
        String pass = DB.md5(pas);
        String repass = DB.md5(repas);
        UserIDEntry userIDEntry = new UserIDEntry(user, pass);

        // check validity of input
        if (user.equals("") || pas.equals("") || repas.equals("")) { // missing fields
            Toast.makeText(RegisterActivity.this, "Please enter all requested fields",
                    Toast.LENGTH_SHORT).show();
        }
        else { // check if valid
            if(pass.equals(repass)) {
                Boolean checkUser = DB.checkUsername(user);
                if(!checkUser) { // username does not exist
                    Boolean insert = DB.insertData(userIDEntry);
                    if(insert) {
                        Toast.makeText(RegisterActivity.this, "Registered successfully!",
                                Toast.LENGTH_SHORT).show();

                        // pass data to other activities
                        Intent intent = new Intent(RegisterActivity.this, HomeActivity.class);
                        intent.putExtra("current_user", user);
                        startActivity(intent); // takes you to home page after registering
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "Failed to register",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Existing user: Sign in",
                            Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(RegisterActivity.this, "The passwords do not match",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }
}