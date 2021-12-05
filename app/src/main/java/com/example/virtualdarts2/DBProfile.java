package com.example.virtualdarts2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;


public class DBProfile extends DBAdapter {

    public static final String TABLE_NAME = "Profile";
    public static final String COL_USER_ID = "User_ID";
    public static final String COL_FIRST_NAME = "First_Name";
    public static final String COL_LAST_NAME = "Last_Name";
    public static final String COL_AGE = "User_Age";

    // database should already exist
    public DBProfile(Context context) { super(context); }

    // checks whether profile entry exists by checking user_ID
    public Boolean checkProfileEntry(String user_ID) {
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

    // if user_ID entry does not exist, make it
    public Boolean insertProfileEntry(ProfileEntry profileEntry) {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ContentValues contentValues = new ContentValues();

        // setting values
        contentValues.put(COL_USER_ID, profileEntry.getUser_ID());
        contentValues.put(COL_FIRST_NAME, profileEntry.getFirst_name());
        contentValues.put(COL_LAST_NAME, profileEntry.getLast_name());
        contentValues.put(COL_AGE, profileEntry.getAge());

        long result = myDB.insert(TABLE_NAME, null, contentValues);

        // close();

        if(result == -1)
            return false;
        else
            return true;
    }

    // gets a profile entry (for the current user)
    public ProfileEntry getProfileEntry(String user_ID) {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Cursor cursor = myDB.query(TABLE_NAME, new String[]{COL_USER_ID,
                        COL_FIRST_NAME, COL_LAST_NAME, COL_AGE}, COL_USER_ID + "=?",
                new String[]{user_ID}, null, null,
                null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        ProfileEntry profileEntry = new ProfileEntry(cursor.getString(0),
                cursor.getString(1), cursor.getString(2), cursor.getInt(3)); // gets values

        Log.d("_NAME_TAG_", profileEntry.getFirst_name());
        Log.d("_LAST_TAG_", profileEntry.getLast_name());

        return profileEntry;
    }


    // updates profile data
    public Boolean updateProfileEntry(ProfileEntry profileEntry) {
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_FIRST_NAME, profileEntry.getFirst_name());
        contentValues.put(COL_LAST_NAME, profileEntry.getLast_name());
        contentValues.put(COL_AGE, profileEntry.getAge());

        // updating row (updates based on ID taken from profile entry)
        long result = myDB.update(TABLE_NAME, contentValues, COL_USER_ID + " = ?",
                new String[] { String.valueOf(profileEntry.getUser_ID()) });

        // close();

        if(result == -1)
            return false;
        else
            return true;
    }
}