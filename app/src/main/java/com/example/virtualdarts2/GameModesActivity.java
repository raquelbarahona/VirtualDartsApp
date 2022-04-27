package com.example.virtualdarts2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class GameModesActivity extends AppCompatActivity {
    ImageButton btnHome;
    TextView players301, players501, playersCU,
            nums301, numsCU,
            text301, text501, textCU,
            link;
    String user_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_modes);

        btnHome = (ImageButton) findViewById(R.id.btnHomeModes);

        players301 = (TextView) findViewById(R.id.tvMode301Players);
        players501 = (TextView) findViewById(R.id.tvMode501Players);
        playersCU = (TextView) findViewById(R.id.tvModeCUPlayers);

        nums301 = (TextView) findViewById(R.id.tvMode301Nums);
        numsCU = (TextView) findViewById(R.id.tvModeCUNums);

        text301 = (TextView) findViewById(R.id.tvMode301Text);
        text501 = (TextView) findViewById(R.id.tvMode501Text);
        textCU = (TextView) findViewById(R.id.tvModeCUText);

        // link
        link = (TextView) findViewById(R.id.tvLink);
        link.setClickable(true);
        link.setMovementMethod(LinkMovementMethod.getInstance());

        String p301 = "Players: Any, but usually two players or two teams";
        String np301 = "Numbers in Play: All the numbers are in play, but 19 and 20 will quickly " +
                "get you to zero like a hero";
        String rules301 = "Rules: Each player/team starts with 301 points. The goal is to reach zero, " +
                "exactly, by subtracting the amount you score in a turn from the number you " +
                "have left." +
                "\n" +
                "\n" +
                "Before you start subtracting though, each player/team has to ‘double in’ " +
                "(hit any one of 21 possible doubles including the double bull). To end the game," +
                " players also need to double out (eg. if you’re on 28, you’ll need to throw a  " +
                "double 14 to reach zero, and if you hit a single 14, your next target is a " +
                "double 7). Hitting more pints than you have left to get to zero will get you " +
                "‘busted’ (this is not what you want). That means the turn is over and next " +
                "time it’s your turn you’ll start again from your previous score.";

        String p501 = "Each player will throw three darts per turn but you don’t have to throw " +
                "all three darts. Like in the case where you have a small number left, it is" +
                " likely to use just one dart to score.";
        String rules501 = "Rules: To play 501 darts the rules are simple, both players or teams " +
                "start with a score of 501 points. Each player then takes alternating turns at " +
                "throwing their darts at the dartboard. The points scored are removed from the " +
                "total, and then the opposing player/team does the same. The first to reach zero " +
                "wins the game.\n" +
                "\n" +
                "However before you can start subtracting from your score, you must double-in, " +
                "and to complete the game you must double-out (both will be explained below). This" +
                " adds a bit more skill to the game as well as strategy when it comes to planning" +
                " a finish.";

        String pCU = "Players: Count Up darts can have 1 – 4 people for solo play or 2 – 8 for doubles.";
        String npCU = "The objective of Count Up darts is to score the highest number of points " +
                "possible with just 24 darts (8 rounds/throws). The player with the highest score " +
                "at the end of the game will be declared the winner.";
        String rulesCU = "Starting from 0, players hit numbers and add the points.\n" +
                "Three throws are counted as one round, and players compete for the highest score" +
                " in a total of 8 rounds.\n" +
                "COUNT-UP is perfect for solo practice!\n" +
                "The game is simple, just add the points from 24 throws.";


        players301.setText(p301);
        players501.setText(p501);
        playersCU.setText(pCU);

        nums301.setText(np301);
        numsCU.setText(npCU);

        text301.setText(rules301);
        text501.setText(rules501);
        textCU.setText(rulesCU);


        // user_ID from other activity
        user_ID = getIntent().getStringExtra("current_user");
        if(user_ID == null)
            user_ID = "user_ID";


        // takes you to home page
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.putExtra("current_user", user_ID);
                startActivity(intent);
            }
        });
    }
}