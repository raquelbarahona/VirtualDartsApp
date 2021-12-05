package com.example.virtualdarts2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DBUser extends DBAdapter {
    public static final String TABLE_NAME = "User";
    public static final String COL_USER_ID = "User_ID";
    public static final String COL_PASSWORD = "Password";

    public DBUser(Context context) { super(context); }

    // inserts data from user input
    public Boolean insertData(UserIDEntry userIDEntry) {
        // open database
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USER_ID, userIDEntry.getUser_ID());
        contentValues.put(COL_PASSWORD, userIDEntry.getPassword());
        long result = myDB.insert(TABLE_NAME, null, contentValues);

        // close database
        // close();

        if(result == -1)
            return false;
        else
            return true;
    }

    // encrypt a password
    public String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    // checks whether username exists -- should work!
    public Boolean checkUsername(String user_ID) {
        // open database
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_USER_ID + " = ?";
        Cursor cursor = myDB.query(TABLE_NAME, new String[] { COL_USER_ID,
                        COL_PASSWORD}, COL_USER_ID + "=?",
                new String[] { String.valueOf(user_ID) }, null, null,
                null, null);

        // close database
        // close();

        if(cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean checkUsernamePassword(UserIDEntry userIDEntry) {
        // open database
        try {
            open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query = "SELECT * from " + TABLE_NAME + " WHERE " + COL_USER_ID +  "= ? and " +
                COL_PASSWORD + " = ?";
        Cursor cursor = myDB.rawQuery(query, new String[]{userIDEntry.getUser_ID(),
                userIDEntry.getPassword()});

        // close database
        // close();

        if(cursor.getCount() > 0)
            return true;
        else
            return false;
    }
}
