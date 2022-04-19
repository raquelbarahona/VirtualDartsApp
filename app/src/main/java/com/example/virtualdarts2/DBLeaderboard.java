package com.example.virtualdarts2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

public class DBLeaderboard extends DBAdapter {
    public static final String TABLE_NAME = "Leaderboard";
    public static final String COL_USER_IDS = "User_IDs";
    public static final String COL_SCORES = "Scores";

    public DBLeaderboard(Context context) { super(context); }

    // checks whether stats entry exists by checking user_ID
    public Boolean checkLDBEntry(String user_ID) {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query = "SELECT count(*) FROM " + TABLE_NAME;
        Cursor cursor = myDB.rawQuery(query, null);

        // close();

        if(cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean updateLDB(String userIDs, String scores) {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ContentValues contentValues = new ContentValues();

        // setting values
        contentValues.put(COL_USER_IDS, userIDs);
        contentValues.put(COL_SCORES, scores);

        // updating row
        String query = "UPDATE " + TABLE_NAME +
                        " SET " + COL_USER_IDS + " = " + userIDs +
                               ", " + COL_SCORES + " = " + scores;
        // long result = myDB.update(TABLE_NAME, contentValues, null);
        Cursor cursor = myDB.rawQuery(query, null);

        // close();

        if(cursor.getCount() == 1)
            return true;
        else
            return false;

    }
}
