package com.example.virtualdarts2;

public class StatsEntry {
    String user_ID;
    String fave_game_mode;
    float high_score;
    float low_score;
    float avg_score;
    int total_games;
    int total_matches;
    int matches_won;
    int matches_lost;
    int matches_tied;

    // constructors
    public StatsEntry() {};

    public StatsEntry(String user_ID, String fave_game_mode, float high_score, float low_score, float avg_score,
                      int total_games, int total_matches, int matches_won, int matches_lost,
                      int matches_tied) {
        this.user_ID = user_ID;
        this.fave_game_mode = fave_game_mode;
        this.high_score = high_score;
        this.low_score = low_score;
        this.avg_score = avg_score;
        this.total_games = total_games;
        this.total_matches = total_matches;
        this.matches_won = matches_won;
        this.matches_lost = matches_lost;
        this.matches_tied = matches_tied;
    };

    // getters
    public String getUser_ID() {
        return this.user_ID;
    }
    public String getFave_game_mode() { return this.fave_game_mode; }
    public float getHigh_score() { return this.high_score; }
    public float getLow_score() { return this.low_score; }
    public float getAvg_score() { return this.avg_score; }
    public int getTotal_games() { return this.total_games; }
    public int getTotal_matches() { return this.total_matches; }
    public int getMatches_won() { return this.matches_won; }
    public int getMatches_lost() { return this.matches_lost; }
    public int getMatches_tied() { return this.matches_tied; }

    // setters
    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }
    public void setFave_game_mode(String fave_game_mode) { this.fave_game_mode = fave_game_mode; }
    public void setHigh_score(float high_score) { this.high_score = high_score; }
    public void setLow_score(float low_score) { this.low_score = low_score; }
    public void setAvg_score(float avg_score) { this.avg_score = avg_score; }
    public void setTotal_games(int total_games) { this.total_games = total_games; }
    public void setTotal_matches(int total_matches) { this.total_matches = total_matches; }
    public void setMatches_won(int matches_won) { this.matches_won = matches_won; }
    public void setMatches_lost(int matches_lost) { this.matches_lost = matches_lost; }
    public void setMatches_tied(int matches_tied) { this.matches_tied = matches_tied; }
}
