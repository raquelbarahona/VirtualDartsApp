package com.example.virtualdarts2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    Button btnStats;
    ImageButton btnHome;
    String user_ID;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    List<ItemClass> itemClasses;
    GameViewAdapter gameViewAdapter;

    DBGame dbGame;
    List<GameEntry> gameEntryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        btnHome = (ImageButton) findViewById(R.id.btnHomeGame);
        btnStats = (Button) findViewById(R.id.btnStatsGame);

        // user_ID from other activity
        user_ID = getIntent().getStringExtra("current_user");
        if(user_ID == null)
            user_ID = "user_ID";

        // From the MainActivity, find the RecyclerView.
        recyclerView = findViewById(R.id.rvGame);

        // Create and set the layout manager
        // For the RecyclerView.
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // db game stuff
        dbGame = new DBGame(this);
        Boolean entryExists = dbGame.checkGameEntry(user_ID);
        if(entryExists) {
            Log.d("TAG_BOOLEAN", "ENTRY EXISTS");
            gameEntryList = dbGame.getGameEntryOneUser(user_ID);

            /*String singlePlayerMode = "<b>Single player mode</b>";
            String date = "Date ";
            String yourScore = "Your score: ";*/
            SpannableStringBuilder singlePlayerMode = new SpannableStringBuilder(" single player");
            singlePlayerMode.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            SpannableStringBuilder matchMode = new SpannableStringBuilder(" match");
            matchMode.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            SpannableStringBuilder date = new SpannableStringBuilder("Date: ");
            date.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            SpannableStringBuilder yourScore = new SpannableStringBuilder("Your score: ");
            yourScore.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            SpannableStringBuilder rivalScore = new SpannableStringBuilder("Your rival's score: ");
            rivalScore.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            SpannableStringBuilder winner = new SpannableStringBuilder("Winner: ");
            winner.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


            itemClasses = new ArrayList<>();
            for(GameEntry entry: gameEntryList) {
                String mode = findMode(entry);
                if(entry.getMatch() == 0) { // not a match, use layout two
                    itemClasses.add(new ItemClass(ItemClass.LayoutTwo, mode + singlePlayerMode,
                            date + entry.getGame_date(),
                            yourScore+ String.valueOf(entry.getPlayer_1_score())));
                }
                else { // actually a match
                    String rival;
                    if(user_ID.equals(entry.getUser_ID_1())){
                        rival = entry.getUser_ID_2();
                    }
                    else {
                        rival = entry.getUser_ID_1();
                    }
                    itemClasses.add(new ItemClass(ItemClass.LayoutOne, mode + matchMode
                            + " against " + rival,
                            date + entry.getGame_date(),
                            yourScore + String.valueOf(entry.getPlayer_1_score()),
                            rivalScore + String.valueOf(entry.getPlayer_2_score()),
                            winner + entry.getWinner_user_ID()));
                }
            }

            // spaces?
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    layoutManager.getOrientation());
            recyclerView.addItemDecoration(dividerItemDecoration);

            gameViewAdapter = new GameViewAdapter(itemClasses);
            recyclerView.setAdapter(gameViewAdapter);

        }
        // no existing game entries
        else {
            Log.d("TAG_BOOLEAN", "ENTRY DOES NOT EXIST");
            //Toast.makeText(getApplicationContext(), "No game entries yet", Toast.LENGTH_SHORT).show();
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

        // takes you back to stats page
        btnStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), StatisticsActivity.class);
                intent.putExtra("current_user", user_ID);
                startActivity(intent);
            }
        });
    }

    // determines mode
    public String findMode(GameEntry entry) {
        String mode301 = "301";
        String modeRound = "Round the World";
        String modeCricket = "Cricket";
        String modeKiller = "Killer";
        String modeEnglish = "English Cricket";

        String mode = "";

        if(entry.getGame_mode().equals("1")) {
            mode = mode301;
        }
        else if(entry.getGame_mode().equals("2")) {
            mode = modeRound;
        }
        else if(entry.getGame_mode().equals("3")) {
            mode = modeCricket;
        }
        else if(entry.getGame_mode().equals("4")) {
            mode = modeKiller;
        }
        else {
            mode = modeEnglish;
        }

        return mode;
    }
}