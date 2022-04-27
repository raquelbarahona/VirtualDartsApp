package com.example.virtualdarts2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StatsBActivity extends AppCompatActivity {
    TextView helloUserTV, favMode, highScore, lowScore, avgScore, totalGames, totalMatches,
            mWon, mLost, mTied;

    TextView highScoreCU, lowScoreCU, avgScoreCU;

    Button btnGameDetails;
    ImageButton btnHome;

    String user_ID;
    DBStats dbStats;
    DBGame dbGame;

    // variable for our bar chart
    BarChart barChart301;

    // variable for our bar data.
    BarData barData301;

    // variable for our bar data set.
    BarDataSet barDataSet301;

    // array list for storing entries.
    ArrayList<BarEntry> barEntriesArrayList301;


    // variable for our bar chart
    BarChart barChartCU;

    // variable for our bar data.
    BarData barDataCU;

    // variable for our bar data set.
    BarDataSet barDataSetCU;

    // array list for storing entries.
    ArrayList<BarEntry> barEntriesArrayListCU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_bactivity);


        // initialize variables
        helloUserTV = (TextView) findViewById(R.id.helloUserTV_b);
        favMode = (TextView) findViewById(R.id.favGameModeTV_b);
        highScore = (TextView) findViewById(R.id.highScoreTV_b);
        lowScore = (TextView) findViewById(R.id.lowScoreTV_b);
        avgScore = (TextView) findViewById(R.id.gameAvgTV_b);
        totalGames = (TextView) findViewById(R.id.gamesPlayedTV_b);
        totalMatches = (TextView) findViewById(R.id.matchesPlayedTV_b);
        mWon = (TextView) findViewById(R.id.matchesWonTV_b);
        mLost = (TextView) findViewById(R.id.matchesLostTV_b);
        mTied = (TextView) findViewById(R.id.matchesTiedTV_b);

        // count up
        highScoreCU = (TextView) findViewById(R.id.highScoreTV_bCU);
        lowScoreCU = (TextView) findViewById(R.id.lowScoreTV_bCU);
        avgScoreCU = (TextView) findViewById(R.id.gameAvgTV_bCU);

        btnHome = (ImageButton) findViewById(R.id.btnHomeStats_b);
        btnGameDetails = (Button) findViewById(R.id.btnGameDetails_b);

        dbStats = new DBStats(this);
        dbGame = new DBGame(this);

        // initializing variable for bar chart.
        barChart301 = findViewById(R.id.idBarChart301);
        barChartCU = findViewById(R.id.idBarChartCU);

        // user_ID from other activity
        // there are issues with this, debug
        user_ID = getIntent().getStringExtra("current_user");
        if(user_ID == null)
            user_ID = "user_ID";

        helloUserTV.setText("Hello " + user_ID + ",");

        // check if there are Stats entries
        dbStats.checkStatsEntry(user_ID); // find stats for given user
        Boolean entryExists = dbStats.checkStatsEntry(user_ID);
        Boolean gameExists = dbGame.checkGameEntry(user_ID); // find stats for given user
        // entry exists
        if (gameExists) { // if there is game data, you can update and replace statistics
            // update page
            updateAndReplace();
            StatsEntry statsEntry = dbStats.getStatsEntry(user_ID);
            // update chart
            updateChart(statsEntry);
            String mode = findMode(statsEntry.getFave_game_mode());
            favMode.setText(mode);
            List<Float> highLowAvg301 = getScores(1, user_ID);
            highScore.setText(String.valueOf(highLowAvg301.get(0)));
            lowScore.setText(String.valueOf(highLowAvg301.get(1)));
            avgScore.setText(String.valueOf(highLowAvg301.get(2)));

            List<Float> highLowAvgCU = getScores(2, user_ID);
            highScoreCU.setText(String.valueOf(highLowAvgCU.get(0)));
            lowScoreCU.setText(String.valueOf(highLowAvgCU.get(1)));
            avgScoreCU.setText(String.valueOf(highLowAvgCU.get(2)));


            totalGames.setText(String.valueOf(statsEntry.getTotal_games()));
            totalMatches.setText(String.valueOf(statsEntry.getTotal_matches()));
            mWon.setText(String.valueOf(statsEntry.getMatches_won()));
            mLost.setText(String.valueOf(statsEntry.getMatches_lost()));
            mTied.setText(String.valueOf(statsEntry.getMatches_tied()));

            // bar stuff 301 and 501
            // creating a new array list
            barEntriesArrayList301 = new ArrayList<>();

            // calling method to get bar entries.
            getBarEntries(barEntriesArrayList301, 1, dbGame, user_ID);

            // creating a new bar data set.
            barDataSet301 = new BarDataSet(barEntriesArrayList301, "Virtual Darts");

            // creating a new bar data and
            // passing our bar data set.
            barData301 = new BarData(barDataSet301);

            // below line is to set data
            // to our bar chart.
            barChart301.setData(barData301);

            // adding color to our bar data set.
            barDataSet301.setColors(ColorTemplate.JOYFUL_COLORS);

            // setting text color.
            barDataSet301.setValueTextColor(Color.WHITE);

            // setting text size
            barDataSet301.setValueTextSize(16f);
            barChart301.getDescription().setEnabled(false);



            // bar stuff count up
            // creating a new array list
            barEntriesArrayListCU = new ArrayList<>();

            // calling method to get bar entries.
            getBarEntries(barEntriesArrayListCU, 2, dbGame, user_ID);

            // creating a new bar data set.
            barDataSetCU = new BarDataSet(barEntriesArrayListCU, "Virtual Darts");

            // creating a new bar data and
            // passing our bar data set.
            barDataCU = new BarData(barDataSetCU);

            // below line is to set data
            // to our bar chart.
            barChartCU.setData(barDataCU);

            // adding color to our bar data set.
            barDataSetCU.setColors(ColorTemplate.COLORFUL_COLORS);

            // setting text color.
            barDataSetCU.setValueTextColor(Color.WHITE);

            // setting text size
            barDataSetCU.setValueTextSize(16f);
            barChartCU.getDescription().setEnabled(false);



        }
        // If there is no game entry, there is no stats entry; show message, ask to read a file maybe
        else {
            // calling method to get bar entries.
            getBarEntriesEmpty();
            // check if there are Stats entries
            if(gameExists) {
                updateAndReplace();
            }
            Toast.makeText(getApplicationContext(), "No statistics entry yet. Go to updates " +
                    "page", Toast.LENGTH_SHORT).show();
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

        btnGameDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!entryExists)
                    Toast.makeText(getApplicationContext(), "You have no game entries yet", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                intent.putExtra("current_user", user_ID);
                startActivity(intent);
            }
        });
    }

    private List<Float> getScores(int type, String user_ID) { // 1 = 301/501 2 = count up
        List<GameEntry> allGames = dbGame.getGameEntryOneUser(user_ID);
        List<GameEntry> modeGames = new ArrayList<GameEntry>();
        int totalOfThatMode = 0;
        for(GameEntry gameEntry : allGames) {
            if (type == 1) { // add 301 and 501
                if(gameEntry.getGame_mode().equals("1") || gameEntry.getGame_mode().equals("2")) {
                    modeGames.add(gameEntry);
                    totalOfThatMode++;
                }
            }
            else { // count up
                if(!gameEntry.getGame_mode().equals("1") && !gameEntry.getGame_mode().equals("2")) {
                    modeGames.add(gameEntry);
                    totalOfThatMode++;
                }
            }
        }
        List<Float> highLowAvg = highLowAvg(user_ID, modeGames, totalOfThatMode);
        return highLowAvg;
    }

    // list and total are just of one specific type
    public List<Float> highLowAvg(String user_ID, List<GameEntry> gameEntryList, int totalGames) {
        float newAvgScore = 0;
        float totalScore = 0;
        float tempScore;
        float currentLow = 0;
        float currentHigh = 0;


        // first game entry in list
        if(gameEntryList.size() > 0) {
            GameEntry gameEntry = gameEntryList.get(0); // there is at least one game entry
            currentHigh = currentLow = gameEntry.getPlayer_1_score();
            if (gameEntry.getMatch() == 1) { // if match, change data if it's second user
                String user2 = gameEntry.getUser_ID_2();
                // USER 2
                if (user2.equals(user_ID)) {
                    currentHigh = currentLow = gameEntry.getPlayer_2_score(); // score that's being compared is user 2
                }
            }


            // high and low and avg score calculations
            int i = 0;
            for (GameEntry entry : gameEntryList) {
                // check if match first
                // NOT A MATCH
                if (entry.getMatch() == 0) {
                    tempScore = entry.getPlayer_1_score(); // score that's being compared is user 1
                }

                // MATCH, USE USER_ID
                else {
                    String user1 = entry.getUser_ID_1();
                    // USER 1
                    if (user1.equals(user_ID)) {
                        tempScore = entry.getPlayer_1_score(); // score that's being compared is user 1
                    }

                    // USER 2
                    else {
                        tempScore = entry.getPlayer_2_score(); // score that's being compared is user 2
                    }
                }

                if (tempScore > currentHigh) {
                    currentHigh = tempScore;
                } else if (tempScore < currentLow) {
                    currentLow = tempScore;
                }
                // add up scores to find average later
                totalScore += tempScore;
            }

            // find new average score
            newAvgScore = totalScore / totalGames; // new average score
            newAvgScore = BigDecimal.valueOf(newAvgScore)
                    .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                    .floatValue();
        }


        List<Float> list = new ArrayList<>();
        // this might be {0,0,0} if there are no count up entries
        list.add(currentHigh);
        list.add(currentLow);
        list.add(newAvgScore);

        return list;
    }

    private void getBarEntries(ArrayList<BarEntry> barEntries, int type, DBGame dbGame, String user_ID) {
        List<GameEntry> allGames = dbGame.getGameEntryOneUser(user_ID);
        ArrayList<Float> scoresList = new ArrayList<>();
        float score = 0;
        for(int i = allGames.size() - 1; i >= 0; i--) {
            GameEntry gameEntry = allGames.get(i);
            if (scoresList.size() >= 15) {  break; } // break when you have the 25 latest entries in the list

            if (type == 1) { // add 301 and 501
                if (gameEntry.getGame_mode().equals("1") || gameEntry.getGame_mode().equals("2")) {
                    if (gameEntry.getMatch() == 0) { // single mode
                        score = gameEntry.getPlayer_1_score();
                    } else { // match, find appropriate score
                        if (user_ID.equals(gameEntry.getUser_ID_1())) {
                            score = gameEntry.getPlayer_1_score();
                        } else {
                            score = gameEntry.getPlayer_2_score();
                        }
                    }
                    scoresList.add(score);
                }
            }
            //COUNT UP
            if (type == 2) { // add 301 and 501
                if (!gameEntry.getGame_mode().equals("1") && !gameEntry.getGame_mode().equals("2")) {
                    if (gameEntry.getMatch() == 0) { // single mode
                        score = gameEntry.getPlayer_1_score();
                    } else { // match, find appropriate score
                        if (user_ID.equals(gameEntry.getUser_ID_1())) {
                            score = gameEntry.getPlayer_1_score();
                        } else {
                            score = gameEntry.getPlayer_2_score();
                        }
                    }
                    scoresList.add(score);
                }
            }
        }


        /*for(GameEntry gameEntry : allGames) { // only add 301 and 501 entries
            if(gameEntry.getGame_mode().equals("1") || gameEntry.getGame_mode().equals("2")) {
                if (gameEntry.getMatch() == 0) { // single mode
                    score = gameEntry.getPlayer_1_score();
                } else { // match, find appropriate score
                    if (user_ID.equals(gameEntry.getUser_ID_1())) {
                        score = gameEntry.getPlayer_1_score();
                    } else {
                        score = gameEntry.getPlayer_2_score();
                    }
                }
                scoresList1.add(score);
            }
        }*/

        // bar chart of latest 25 scores
        /*int max = 25;
        for(int i = scoresList1.size() - 1; i >= 0; i--) {
            if(i == max) break; // add either the 25 latest or all the ones avaliable in array, reverse order
            scoresList.add(scoresList1.get(i));
            i++;
        }*/

        // list of all scores, before removing duplicates
        /*ArrayList<Float> allScores = new ArrayList<>(scoresList);

        // Remove duplicates and sort
        Set<Float> set = new HashSet<>(scoresList);
        scoresList.clear();
        scoresList.addAll(set);
        Collections.sort(scoresList);

        // make a pair (score, score frequency)
        ArrayList<Pair<Float, Integer>> pairScores = new ArrayList<>();
        // for every possible score s, check how many times that score happens
        for(Float s : scoresList) {
            int freq = 0;
            for(Float as : allScores) { // goes through every score
                if(as == s) freq++;
            }
            pairScores.add(new Pair<Float, Integer>(s, freq)); // add to list of pairs
        }

        // creating a new array list
        barEntriesArrayList301 = new ArrayList<>();

        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
        for(int j = 0; j < pairScores.size(); j++) {
            Pair<Float, Integer> scorePair = pairScores.get(j);
            barEntriesArrayList301.add(new BarEntry(j, scorePair.second)); // not javafx pair!

        }*/

        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
        for(int j = 0; j < scoresList.size(); j++) {
            barEntries.add(new BarEntry(j, scoresList.get(j))); }

    }


    // default chart
    private void getBarEntriesEmpty() {
        // creating a new array list
        barEntriesArrayList301 = new ArrayList<>();

        // adding new entry to our array list with bar
        // entry and passing x and y axis value to it.
        barEntriesArrayList301.add(new BarEntry(1f, 4));
        barEntriesArrayList301.add(new BarEntry(2f, 6));
        barEntriesArrayList301.add(new BarEntry(3f, 8));
        barEntriesArrayList301.add(new BarEntry(4f, 2));
        barEntriesArrayList301.add(new BarEntry(5f, 4));
        barEntriesArrayList301.add(new BarEntry(6f, 1));
    }

    // updates and replaces statistics entry
    public void updateAndReplace() {
        List<GameEntry> entryList = dbGame.getGameEntryOneUser(user_ID); // there has to be at least something inserted

        UpdateStats updateStats = new UpdateStats(entryList);
        String newFavMode = updateStats.newGameMode();
        int newTotG = updateStats.newTotalG();
        int newTotM = updateStats.newTotalM();

        // check if entry exists
        Boolean entryExists = dbStats.checkStatsEntry(user_ID);
        List<Float> newHLAvg;
        if (entryExists) { // entry exist, use right function
            StatsEntry statsEntry = dbStats.getStatsEntry(user_ID);
            newHLAvg = updateStats.newHighLowAvg(user_ID, newTotG, statsEntry);
        }
        else {
            newHLAvg = updateStats.firstNewHighLowAvg(user_ID, newTotG);
        }
        List<Integer> newWLT = updateStats.newWonLostTied(user_ID);

        StatsEntry updatedStatsEntry = new StatsEntry(user_ID, newFavMode, newHLAvg.get(0),
                newHLAvg.get(1), newHLAvg.get(2), newTotG, newTotM, newWLT.get(0),
                newWLT.get(1), newWLT.get(2));

        if(entryExists) { // update stats
            Boolean updated = dbStats.updateStatsEntry(updatedStatsEntry);
            if(updated) {
                Toast.makeText(getApplicationContext(), "Statistics updated!",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Unable to update statistics",
                        Toast.LENGTH_SHORT).show();
            }
        }
        else { // insert stats entry because it does not exist
            Boolean inserted = dbStats.insertStatsEntry(updatedStatsEntry);
            if(inserted) {
                Toast.makeText(getApplicationContext(), "Statistics updated!",
                        Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Unable to update statistics",
                        Toast.LENGTH_SHORT).show();
            }
        }

    }

    // determines mode
    public String findMode(String numberMode) {
        String mode301 = "301";
        String mode501 = "501";
        String modeCU = "Count Up";

        String mode = "";

        if(numberMode.equals("1")) {
            mode = mode301;
        }
        else if(numberMode.equals("2")) {
            mode = mode501;
        }
        else {
            mode = modeCU;
        }

        return mode;
    }

    private void updateChart(StatsEntry statsEntry){
        // get statistics
        int won = statsEntry.getMatches_won();
        int lost = statsEntry.getMatches_lost();
        int total = statsEntry.getTotal_matches();


        // Calculate the slice size and update the pie chart:
        ProgressBar wonPB = findViewById(R.id.stats_progressbar); // matches won progressbar
        ProgressBar lostPB = findViewById(R.id.stats_lostbar); // matches won progressbar

        /*
            10w,4l,6t
            10/20 = 0.50
            4/20 =  0.20
            50 + 20 = 70
         */

        double ll = (double) lost / (double) total;
        int progressL = (int) (ll * 100);
        lostPB.setProgress(progressL);

        double ww = (double) won / (double) total;
        int progressW = (int) (ww * 100) + progressL; // must add these to represent actual value
        wonPB.setProgress(progressW);

    }
}