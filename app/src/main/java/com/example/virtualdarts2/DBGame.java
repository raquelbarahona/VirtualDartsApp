package com.example.virtualdarts2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DBGame extends DBAdapter {
    public static final String TABLE_NAME = "Game";
    public static final String COL_MATCH = "Match";
    public static final String COL_USER_ID_1 = "User_ID_1";
    public static final String COL_USER_ID_2 = "User_ID_2";
    public static final String COL_GAME_DATE = "Game_Date";
    public static final String COL_PLAYER_1_SCORE = "Player_1_Score";
    public static final String COL_PLAYER_2_SCORE = "Player_2_Score";
    public static final String COL_WINNER_USER_ID = "Winner_User_ID";
    public static final String COL_GAME_MODE = "Game_Mode";

    public DBGame(Context context) {
        super(context);
    }

    // checks whether game entry exists by checking user_ID
    // figure this out!!!!!!!
    public Boolean checkGameEntry(String user_ID) {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query1 = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_USER_ID_1 + " = ?";
        String query2 = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_USER_ID_2 + " = ?";
        /*Cursor cursor = myDB.query(TABLE_NAME, new String[] { COL_USER_ID,
                COL_FIRST_NAME, COL_LAST_NAME, COL_AGE }, COL_USER_ID + "=?",
                new String[] {user_ID}, null, null,
                null, null);*/
        Cursor cursor1 = myDB.rawQuery(query1, new String[] {user_ID});
        Cursor cursor2 = myDB.rawQuery(query2, new String[] {user_ID});

        // close();

        int count1 = 0;
        int count2 = 0;
        if (cursor1 != null)
            count1 = cursor1.getCount();
        if (cursor2 != null)
            count2 = cursor2.getCount();

        if(count1 > 0  || count2 > 0) // either user 1 or 2
            return true;

        else return false;
    }

    // inserts a game entry into the table
    public Boolean insertGameEntry(GameEntry gameEntry) {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ContentValues contentValues = new ContentValues();

        // setting values
        contentValues.put(COL_MATCH, gameEntry.getMatch());
        contentValues.put(COL_USER_ID_1, gameEntry.getUser_ID_1());
        contentValues.put(COL_USER_ID_2, gameEntry.getUser_ID_2());
        contentValues.put(COL_GAME_DATE, gameEntry.getGame_date());
        contentValues.put(COL_PLAYER_1_SCORE, gameEntry.getPlayer_1_score());
        contentValues.put(COL_PLAYER_2_SCORE, gameEntry.getPlayer_2_score());
        contentValues.put(COL_WINNER_USER_ID, gameEntry.getWinner_user_ID());
        contentValues.put(COL_GAME_MODE, gameEntry.getGame_mode());


        long result = myDB.insert(TABLE_NAME, null, contentValues);

        // close();

        if (result == -1)
            return false;
        else
            return true;
    }


    // gets a game entry given a two user_IDs
    public List<GameEntry> getGameEntryTwoUsers(String user_ID_1, String user_ID_2) {
        List<GameEntry> gameEntryList = new ArrayList<GameEntry>();
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query = "SELECT * from " + TABLE_NAME + " WHERE " + COL_USER_ID_1 +  "= ? and " +
                COL_USER_ID_2 + " = ?";
        /*Cursor cursor = myDB.query(TABLE_NAME, new String[]{COL_USER_ID_1, COL_USER_ID_2,
                        COL_GAME_DATE, COL_PLAYER_1_SCORE, COL_PLAYER_2_SCORE, COL_WINNER_USER_ID,
                        COL_GAME_MODE },
                COL_USER_ID + "=?",
                new String[]{user_ID}, null, null,
                null, null);*/
        Cursor cursor = myDB.rawQuery(query, new String[]{ user_ID_1, user_ID_2 });

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                GameEntry gameEntry = new GameEntry(cursor.getInt(0), cursor.getString(1),
                        cursor.getString(2), cursor.getString(3), cursor.getFloat(4),
                        cursor.getFloat(5), cursor.getString(6), cursor.getString(7));
                gameEntryList.add(gameEntry);
                cursor.moveToNext();
            }
        }

        //Log.d("_NAME_TAG_", gameEntry.getGame_mode());

        return gameEntryList;
    }

    // returns every game entry a given user appears
    public List<GameEntry> getGameEntryOneUser(String user_ID) {
        List<GameEntry> gameEntryList = new ArrayList<GameEntry>();
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query1 = "SELECT * from " + TABLE_NAME + " WHERE " + COL_USER_ID_1 +  "= ?";
        String query2 = "SELECT * from " + TABLE_NAME + " WHERE " + COL_USER_ID_2 +  "= ?";

        /*Cursor cursor = myDB.query(TABLE_NAME, new String[]{COL_USER_ID_1, COL_USER_ID_2,
                        COL_GAME_DATE, COL_PLAYER_1_SCORE, COL_PLAYER_2_SCORE, COL_WINNER_USER_ID,
                        COL_GAME_MODE },
                COL_USER_ID + "=?",
                new String[]{user_ID}, null, null,
                null, null);*/
        Cursor cursor1 = myDB.rawQuery(query1, new String[]{ user_ID });
        Cursor cursor2 = myDB.rawQuery(query2, new String[]{ user_ID });

        if (cursor1 != null) {
            cursor1.moveToFirst();
            while (!cursor1.isAfterLast()) {
                GameEntry gameEntry = new GameEntry(cursor1.getInt(0), cursor1.getString(1),
                        cursor1.getString(2), cursor1.getString(3), cursor1.getFloat(4),
                        cursor1.getFloat(5), cursor1.getString(6), cursor1.getString(7));
                gameEntryList.add(gameEntry);
                cursor1.moveToNext();
            }
        }

        if (cursor2 != null) {
            cursor2.moveToFirst();
            while (!cursor2.isAfterLast()) {
                GameEntry gameEntry = new GameEntry(cursor2.getInt(0), cursor2.getString(1),
                        cursor2.getString(2), cursor2.getString(3), cursor2.getFloat(4),
                        cursor2.getFloat(5), cursor2.getString(6), cursor2.getString(7));
                gameEntryList.add(gameEntry);
                cursor2.moveToNext();
            }
        }

        //Log.d("_NAME_TAG_", gameEntry.getGame_mode());

        return gameEntryList;
    }
}