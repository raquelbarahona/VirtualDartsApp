package com.example.virtualdarts2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class LeaderboardActivity extends AppCompatActivity {
    ImageButton btnHome;
    TextView first, second, third, fourth, fifth;
    TextView firstScore, secondScore, thirdScore, fourthScore, fifthScore;
    String user_ID;

    //DBLeaderboard dbLeaderboard;
    DBStats dbStats;
    DBGame dbGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // initialize variables
        first = (TextView) findViewById(R.id.tvFirst);
        second = (TextView) findViewById(R.id.tvSecond);
        third = (TextView) findViewById(R.id.tvThird);
        fourth = (TextView) findViewById(R.id.tvFourth);
        fifth = (TextView) findViewById(R.id.tvFifth);

        firstScore = (TextView) findViewById(R.id.tvFirstScore);
        secondScore = (TextView) findViewById(R.id.tvSecondScore);
        thirdScore = (TextView) findViewById(R.id.tvThirdScore);
        fourthScore = (TextView) findViewById(R.id.tvFourthScore);
        fifthScore = (TextView) findViewById(R.id.tvFifthScore);

        // add them to list
        ArrayList<TextView> tvList = new ArrayList<>();
        ArrayList<TextView> tvScoreList = new ArrayList<>();
        Collections.addAll(tvList, first, second, third, fourth, fifth);
        Collections.addAll(tvScoreList, firstScore, secondScore, thirdScore, fourthScore, fifthScore);


        btnHome = (ImageButton) findViewById(R.id.btnHomeLDB);
        //dbLeaderboard = new DBLeaderboard(this);
        dbStats = new DBStats(this);
        dbGame = new DBGame(this);

        // user_ID from other activity
        user_ID = getIntent().getStringExtra("current_user");
        if(user_ID == null)
            user_ID = "user_ID";

        ArrayList<StatsEntry> statsList = dbStats.getAllStatsEntries();
        ArrayList<MyPair> pairList = new ArrayList<>();
        for(StatsEntry statsEntry : statsList) {
            pairList.add(new MyPair(statsEntry.getUser_ID(), statsEntry.getHigh_score()));
        }
        Collections.sort(pairList, Collections.reverseOrder());

        int min = Math.min(tvList.size(), pairList.size());
        for(int i = 0; i < min; i++) {
            tvList.get(i).setText(i + 1 + ". " + pairList.get(i).user_ID);
            tvScoreList.get(i).setText( String.valueOf(pairList.get(i).highScore));
        }

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
class MyPair implements Comparable<MyPair>{
    String user_ID;
    float highScore;

    // Constructor
    public MyPair(String x, float y) {
        user_ID = x;
        highScore = y;
    }

    @Override
    public int compareTo(MyPair mp) {
        return highScore > mp.highScore  ? 1 : highScore < mp.highScore  ? -1 : 0;
    }
}

