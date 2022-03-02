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
    TextView players301, players501, playersCr, playersCU, playersEnC,
            nums301, numsCr, numsCU, numsEnC,
            text301, text501, textCr, textCU, textEnC,
            link;
    String user_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_modes);

        btnHome = (ImageButton) findViewById(R.id.btnHomeModes);

        players301 = (TextView) findViewById(R.id.tvMode301Players);
        players501 = (TextView) findViewById(R.id.tvMode501Players);
        playersCr = (TextView) findViewById(R.id.tvModeCricketPlayers);
        playersCU = (TextView) findViewById(R.id.tvModeCUPlayers);

        nums301 = (TextView) findViewById(R.id.tvMode301Nums);
        numsCr = (TextView) findViewById(R.id.tvModeCricketNums);
        numsCU = (TextView) findViewById(R.id.tvModeCUNums);

        text301 = (TextView) findViewById(R.id.tvMode301Text);
        text501 = (TextView) findViewById(R.id.tvMode501Text);
        textCr = (TextView) findViewById(R.id.tvModeCricketText);
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

        String pC = "Players: Two players or two teams";
        String npC = "Numbers in Play: bullseye, 20,19,18,17,16,15";
        String rulesC = "Rules: The aim is to 'close' these numbers on the board, and get the " +
                "highest point score. The player/team to do so first, wins.\n" +
                "\n" +
                "\n" +
                "Each player/team takes turns throwing three darts in a row (an 'inning'). " +
                "To close an inning, the player/team needs to score three of a number – with " +
                "three singles, a single and a double, or a triple.\n" +
                "\n" +
                "\n" +
                "Once a player/team scores three of a number, they ‘own’ it. Once a player/team " +
                "closes an inning, he/they may score points on that number until the opponent also " +
                "closes that inning. All numerical scores are added together.\n" +
                "\n" +
                "Once both players/teams have scored three of a number, it’s 'closed', and it can’t" +
                " be scored on by either player/team.\n" +
                "\n" +
                "To close the bullseye, the outer bull counts as a single, and the inner bull " +
                "counts as a double. Numbers can be 'owned' or 'closed' in any order. No need to " +
                "call your shot.\n" +
                "\n" +
                "The player/team that closes all the innings first and has the most points, wins. " +
                "If both sides are tied on points, the first player/team to close all innings is " +
                "the winner. If a player/team closes all innings first, but is behind on points, " +
                "they need to keep scoring on any innings that aren’t closed until they make up " +
                "the points or their opponent wins the game.";

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
        playersCr.setText(pC);
        playersCU.setText(pCU);

        nums301.setText(np301);
        numsCr.setText(npC);
        numsCU.setText(npCU);

        text301.setText(rules301);
        text501.setText(rules501);
        textCr.setText(rulesC);
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