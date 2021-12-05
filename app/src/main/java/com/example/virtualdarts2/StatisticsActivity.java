package com.example.virtualdarts2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class StatisticsActivity extends AppCompatActivity {

    TextView helloUserTV, favMode, highScore, lowScore, avgScore, totalGames, totalMatches,
            mWon, mLost, mTied;
    Button btnHome, btnGameDetails;
    String user_ID;
    DBStats dbStats;
    DBGame dbGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        // initialize variables
        helloUserTV = (TextView) findViewById(R.id.helloUserTV);
        favMode = (TextView) findViewById(R.id.favGameModeTV);
        highScore = (TextView) findViewById(R.id.highScoreTV);
        lowScore = (TextView) findViewById(R.id.lowScoreTV);
        avgScore = (TextView) findViewById(R.id.gameAvgTV);
        totalGames = (TextView) findViewById(R.id.gamesPlayedTV);
        totalMatches = (TextView) findViewById(R.id.matchesPlayedTV);
        mWon = (TextView) findViewById(R.id.matchesWonTV);
        mLost = (TextView) findViewById(R.id.matchesLostTV);
        mTied = (TextView) findViewById(R.id.matchesTiedTV);

        btnHome = (Button) findViewById(R.id.btnHomeStats);
        btnGameDetails = (Button) findViewById(R.id.btnGameDetails);

        dbStats = new DBStats(this);
        dbGame = new DBGame(this);

        // user_ID from other activity
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
            String mode = findMode(statsEntry.getFave_game_mode());
            favMode.setText(mode);
            highScore.setText(String.valueOf(statsEntry.getHigh_score()));
            lowScore.setText(String.valueOf(statsEntry.getLow_score()));
            avgScore.setText(String.valueOf(statsEntry.getAvg_score()));
            totalGames.setText(String.valueOf(statsEntry.getTotal_games()));
            totalMatches.setText(String.valueOf(statsEntry.getTotal_matches()));
            mWon.setText(String.valueOf(statsEntry.getMatches_won()));
            mLost.setText(String.valueOf(statsEntry.getMatches_lost()));
            mTied.setText(String.valueOf(statsEntry.getMatches_tied()));
        }
        // If there is no game entry, there is no stats entry; show message, ask to read a file maybe
        else {
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
        String modeRound = "Round the World";
        String modeCricket = "Cricket";
        String modeKiller = "Killer";
        String modeEnglish = "English Cricket";

        String mode = "";

        if(numberMode.equals("1")) {
            mode = mode301;
        }
        else if(numberMode.equals("2")) {
            mode = modeRound;
        }
        else if(numberMode.equals("3")) {
            mode = modeCricket;
        }
        else if(numberMode.equals("4")) {
            mode = modeKiller;
        }
        else {
            mode = modeEnglish;
        }

        return mode;
    }


}