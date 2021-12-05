package com.example.virtualdarts2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBStats extends DBAdapter {
    public static final String TABLE_NAME = "Statistics";
    public static final String COL_USER_ID = "User_ID";
    public static final String COL_FAVE_GAME_MODE = "Favorite_Mode";
    public static final String COL_HIGH_SCORE = "High_Score";
    public static final String COL_LOW_SCORE = "Low_Score";
    public static final String COL_AVG_SCORE = "Avg_Score";
    public static final String COL_TOTAL_GAMES = "Total_Games";
    public static final String COL_TOTAL_MATCHES = "Total_Matches";
    public static final String COL_MATCHES_WON = "Matches_Won";
    public static final String COL_MATCHES_LOST = "Matches_Lost";
    public static final String COL_MATCHES_TIED = "Matches_Tied";

    public DBStats(Context context) { super(context); }

    // checks whether stats entry exists by checking user_ID
    public Boolean checkStatsEntry(String user_ID) {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_USER_ID + " = ?";
        /*Cursor cursor = myDB.query(TABLE_NAME, new String[] { COL_USER_ID,
                COL_FIRST_NAME, COL_LAST_NAME, COL_AGE }, COL_USER_ID + "=?",
                new String[] {user_ID}, null, null,
                null, null);*/
        Cursor cursor = myDB.rawQuery(query, new String[] {user_ID});

        // close();

        if(cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    // inserts a statistics entry into the table
    public Boolean insertStatsEntry(StatsEntry statsEntry) {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ContentValues contentValues = new ContentValues();

        // setting values
        contentValues.put(COL_USER_ID, statsEntry.getUser_ID());
        contentValues.put(COL_FAVE_GAME_MODE, statsEntry.getFave_game_mode());
        contentValues.put(COL_HIGH_SCORE, statsEntry.getHigh_score());
        contentValues.put(COL_LOW_SCORE, statsEntry.getLow_score());
        contentValues.put(COL_AVG_SCORE, statsEntry.getAvg_score());
        contentValues.put(COL_TOTAL_GAMES, statsEntry.getTotal_games());
        contentValues.put(COL_TOTAL_MATCHES, statsEntry.getTotal_matches());
        contentValues.put(COL_MATCHES_WON, statsEntry.getMatches_won());
        contentValues.put(COL_MATCHES_LOST, statsEntry.getMatches_lost());
        contentValues.put(COL_MATCHES_TIED, statsEntry.getMatches_tied());

        long result = myDB.insert(TABLE_NAME, null, contentValues);

        // close();

        if(result == -1)
            return false;
        else
            return true;
    }

    // gets a stats entry given a user_ID
    public StatsEntry getStatsEntry(String user_ID) {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Cursor cursor = myDB.query(TABLE_NAME, new String[]{COL_USER_ID, COL_FAVE_GAME_MODE,
                        COL_HIGH_SCORE, COL_LOW_SCORE, COL_AVG_SCORE, COL_TOTAL_GAMES,
                        COL_TOTAL_MATCHES, COL_MATCHES_WON, COL_MATCHES_LOST, COL_MATCHES_TIED},
                COL_USER_ID + "=?",
                new String[]{user_ID}, null, null,
                null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        StatsEntry statsEntry = new StatsEntry(cursor.getString(0), cursor.getString(1),
                cursor.getFloat(2), cursor.getFloat(3), cursor.getFloat(4),
                cursor.getInt(5), cursor.getInt(6), cursor.getInt(7),
                cursor.getInt(8), cursor.getInt(9)); // gets values

        //Log.d("_NAME_TAG_", statsEntry.getHigh_score());

        return statsEntry;
    }

    // updates user stats
    public Boolean updateStatsEntry(StatsEntry statsEntry) {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ContentValues contentValues = new ContentValues();

        // setting values
        contentValues.put(COL_USER_ID, statsEntry.getUser_ID());
        contentValues.put(COL_FAVE_GAME_MODE, statsEntry.getFave_game_mode());
        contentValues.put(COL_HIGH_SCORE, statsEntry.getHigh_score());
        contentValues.put(COL_LOW_SCORE, statsEntry.getLow_score());
        contentValues.put(COL_AVG_SCORE, statsEntry.getAvg_score());
        contentValues.put(COL_TOTAL_GAMES, statsEntry.getTotal_games());
        contentValues.put(COL_TOTAL_MATCHES, statsEntry.getTotal_matches());
        contentValues.put(COL_MATCHES_WON, statsEntry.getMatches_won());
        contentValues.put(COL_MATCHES_LOST, statsEntry.getMatches_lost());
        contentValues.put(COL_MATCHES_TIED, statsEntry.getMatches_tied());

        // updating row (updates based on ID taken from stats entry)
        long result = myDB.update(TABLE_NAME, contentValues, COL_USER_ID + " = ?",
                new String[] { String.valueOf( statsEntry.getUser_ID()) });

        // close();

        if(result == -1)
            return false;
        else
            return true;
    }

}
