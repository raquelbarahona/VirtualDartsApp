package com.example.virtualdarts2;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class DBAdapter {

    // last version update: addition of profile picture to database
    public static final int DATABASE_VERSION = 7; // update every time you add a new table??
    public static final String DATABASE_NAME = "VirtualDarts.db"; // name of the database

    // Creating Table
    private static final String CREATE_USERS_TABLE = "CREATE TABLE "
            + DBUser.TABLE_NAME + "("
            + DBUser.COL_USER_ID + " TEXT PRIMARY KEY,"
            + DBUser.COL_PASSWORD + " TEXT"
            + ")";

    private static final String CREATE_PROFILE_TABLE = "CREATE TABLE "
            + DBProfile.TABLE_NAME + "("
            + DBProfile.COL_USER_ID + " TEXT PRIMARY KEY,"
            + DBProfile.COL_FIRST_NAME +  " TEXT,"
            + DBProfile.COL_LAST_NAME + " TEXT,"
            + DBProfile.COL_AGE + " INT,"
            + DBProfile.COL_PIC_PATH + " TEXT"
            + ")";

    // not creating a primary key, will this make a difference?
    private static final String CREATE_GAME_TABLE = "CREATE TABLE "
            + DBGame.TABLE_NAME + "("
            + DBGame.COL_MATCH + " INT,"
            + DBGame.COL_USER_ID_1 + " TEXT,"
            + DBGame.COL_USER_ID_2 +  " TEXT,"
            + DBGame.COL_GAME_DATE + " TEXT,"
            + DBGame.COL_PLAYER_1_SCORE + " FLOAT,"
            + DBGame.COL_PLAYER_2_SCORE + " FLOAT,"
            + DBGame.COL_WINNER_USER_ID + " TEXT,"
            + DBGame.COL_GAME_MODE + " TEXT"
            + ")";

    private static final String CREATE_STATS_TABLE = "CREATE TABLE "
            + DBStats.TABLE_NAME + "("
            + DBStats.COL_USER_ID + " TEXT PRIMARY KEY,"
            + DBStats.COL_FAVE_GAME_MODE + " TEXT,"
            + DBStats.COL_HIGH_SCORE + " FLOAT,"
            + DBStats.COL_LOW_SCORE + " FLOAT,"
            + DBStats.COL_AVG_SCORE + " FLOAT,"
            + DBStats.COL_TOTAL_GAMES + " INT,"
            + DBStats.COL_TOTAL_MATCHES + " INT,"
            + DBStats.COL_MATCHES_WON + " INT,"
            + DBStats.COL_MATCHES_LOST + " INT,"
            + DBStats.COL_MATCHES_TIED + " INT"
            + ")";

    public static Context context;
    public static DBHelper dbHelper;
    public static SQLiteDatabase myDB;

    // constructor
    public DBAdapter(Context ctx) {
        this.dbHelper = new DBHelper(ctx);
    }

    // SQLite commands
    public class DBHelper extends SQLiteOpenHelper {
        DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase myDB) {
            myDB.execSQL(CREATE_STATS_TABLE);
            myDB.execSQL(CREATE_GAME_TABLE);
            myDB.execSQL(CREATE_PROFILE_TABLE);
            myDB.execSQL(CREATE_USERS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase myDB, int oldVersion, int newVersion) {
            myDB.execSQL("DROP TABLE IF EXISTS " + DBUser.TABLE_NAME);
            myDB.execSQL("DROP TABLE IF EXISTS " + DBProfile.TABLE_NAME);
            myDB.execSQL("DROP TABLE IF EXISTS " + DBGame.TABLE_NAME);
            myDB.execSQL("DROP TABLE IF EXISTS " + DBStats.TABLE_NAME);
            onCreate(myDB);
        }
    }

    // opens database
    public DBAdapter open() throws SQLException {
        this.myDB = this.dbHelper.getWritableDatabase();
        return this;
    }

    // closes database
    public void close() {
        this.dbHelper.close();
    }
}


