package com.example.virtualdarts2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameModesActivity extends AppCompatActivity {
    Button btnHome;
    TextView players301, playersRW, playersCr, playersKill, playersEnC,
            nums301, numsCr, numsKill, numsEnC,
            text301, textRW, textCr, textKill, textEnC,
            link;
    String user_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_modes);

        btnHome = (Button) findViewById(R.id.btnHomeModes);

        players301 = (TextView) findViewById(R.id.tvMode301Players);
        playersRW = (TextView) findViewById(R.id.tvModeRWPlayers);
        playersCr = (TextView) findViewById(R.id.tvModeCricketPlayers);
        playersKill = (TextView) findViewById(R.id.tvModeKillerPlayers);
        playersEnC = (TextView) findViewById(R.id.tvModeEnCricketPlayers);

        nums301 = (TextView) findViewById(R.id.tvMode301Nums);
        numsCr = (TextView) findViewById(R.id.tvModeCricketNums);
        numsKill = (TextView) findViewById(R.id.tvModeKillerNums);
        numsEnC = (TextView) findViewById(R.id.tvModeEnCricketNums);

        text301 = (TextView) findViewById(R.id.tvMode301Text);
        textRW = (TextView) findViewById(R.id.tvModeRWText);
        textCr = (TextView) findViewById(R.id.tvModeCricketText);
        textKill = (TextView) findViewById(R.id.tvModeKillerText);
        textEnC = (TextView) findViewById(R.id.tvModeEnCricketText);

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

        String pRW = "Players: Two";
        String rulesRW = "Rules: The object is to be the first player to hit every number on the board " +
                "in sequence from 1-20. Hitting any part of the number – single, double or triple " +
                "– counts, and numbers must be hit in order to advance to the next. Players " +
                "alternate after three throws. The first player to hit a 20 is the winner.";

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

        String pKill = "Players: Any, but three or more players is more fun";
        String npKill = "Numbers in Play: The numbers used are determined by the players.  " +
                "Each player throws a dart with their opposite hand to randomly choose their " +
                "number. If you miss the board or hit a number that’s already claimed, " +
                "you’ll need to throw again.";
        String rulesKill = "Rules: Using three throws in a turn, each player first tries to hit " +
                "the double of his or her own number – they’re then called a ‘killer’ and a K " +
                "is placed next to their name on the scoreboard.\n" +
                "\n" +
                "Once a player is a killer, they aim for doubles of opponents' numbers. Each " +
                "player has three lives and when a killer hits an opponent’s double the opponent " +
                "loses a life. If a killer hits their own double by mistake, they lose one life. " +
                "It’s possible to completely kill an opponent in one turn by throwing three " +
                "doubles. The last player standing is the winner";

        String pEnC = "Players: Two players or two teams";
        String npEnC = "Numbers in Play: All numbers, but as each score must be higher than 40, " +
                "the 20 is pretty popular.";
        String rulesEnC = "Rules: Ten stripes are marked on the scoreboard as wickets. One " +
                "player bats and the other bowls. The batter goes first.\n" +
                "\n" +
                "The bowler's job is to erase these wickets by hitting bullseyes. Each single " +
                "bullseye erases one wicket, and each double bullseye wipes out two. The batter " +
                "needs to score as many points (runs) as possible while their wickets remain. " +
                "The tricky bit is that only scores over 40 count. E.g. scoring 37 = no runs. " +
                "Scoring 45 = 5 runs etc.\n" +
                "\n" +
                "Scoring stops when all 10 wickets are taken out by the bowler. The batter " +
                "records their final score, and then they swap roles. The winner is the player " +
                "with the most points, or runs, from their round as batter.";

        players301.setText(p301);
        playersRW.setText(pRW);
        playersCr.setText(pC);
        playersKill.setText(pKill);
        playersEnC.setText(pEnC);

        nums301.setText(np301);
        numsCr.setText(npC);
        numsKill.setText(npKill);
        numsEnC.setText(npEnC);

        text301.setText(rules301);
        textRW.setText(rulesRW);
        textCr.setText(rulesC);
        textKill.setText(rulesKill);
        textEnC.setText(rulesEnC);

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