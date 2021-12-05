package com.example.virtualdarts2;

public class GameEntry {
    int match;
    String user_ID_1;
    String user_ID_2;
    String game_date;
    float player_1_score;
    float player_2_score;
    String winner_user_ID; // will be calculated
    String game_mode;

    // constructors
    public GameEntry() {};

    public GameEntry(int match, String user_ID_1, String user_ID_2, String game_date,
                     float player_1_score, float player_2_score, String winner_user_ID,
                     String game_mode) {
        this.match = match;
        this.user_ID_1 = user_ID_1;
        this.user_ID_2 = user_ID_2;
        this.game_date = game_date;
        this.player_1_score = player_1_score;
        this.player_2_score = player_2_score;
        this.winner_user_ID = winner_user_ID;
        this.game_mode = game_mode;
    };

    // getters

    public int getMatch() {
        return match;
    }

    public String getUser_ID_1() {
        return user_ID_1;
    }

    public String getUser_ID_2() {
        return user_ID_2;
    }

    public String getGame_date() {
        return game_date;
    }

    public float getPlayer_1_score() {
        return player_1_score;
    }

    public float getPlayer_2_score() {
        return player_2_score;
    }

    public String getWinner_user_ID() {
        return winner_user_ID;
    }

    public String getGame_mode() {
        return game_mode;
    }

    // setters

    public void setMatch(int match) {
        this.match = match;
    }

    public void setUser_ID_1(String user_ID_1) {
        this.user_ID_1 = user_ID_1;
    }

    public void setUser_ID_2(String user_ID_2) {
        this.user_ID_2 = user_ID_2;
    }

    public void setGame_date(String game_date) {
        this.game_date = game_date;
    }

    public void setPlayer_1_score(float player_1_score) {
        this.player_1_score = player_1_score;
    }

    public void setPlayer_2_score(float player_2_score) {
        this.player_2_score = player_2_score;
    }

    public void setWinner_user_ID(String winner_user_ID) {
        this.winner_user_ID = winner_user_ID;
    }

    public void setGame_mode(String game_mode) {
        this.game_mode = game_mode;
    }
}
