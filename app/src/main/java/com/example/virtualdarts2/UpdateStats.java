package com.example.virtualdarts2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

// you need a new stats entry!
// take all game entries and recalculate stats
// this is not efficient; try to find a new method

public class UpdateStats {
    List<GameEntry> gameEntryList;

    public UpdateStats() {}

    public UpdateStats(List<GameEntry> gameEntryList) {
        this.gameEntryList = gameEntryList;
    }


    // game mode calculations
    public String newGameMode() {
        String newFavGameMode = "";

        // game modes
        int mode301 = 0;
        int modeRound = 0;
        int modeCricket = 0;
        int modeKiller = 0;
        int modeEnglish = 0;

        // find total of each game player
        // counts how many of each mode there is and finds the most played
        for(GameEntry entry : this.gameEntryList) {
            if(entry.getGame_mode().equals("1")) {
                mode301 += 1;
            }
            else if(entry.getGame_mode().equals("2")) {
                modeRound += 1;
            }
            else if(entry.getGame_mode().equals("3")) {
                modeCricket += 1;
            }
            else if(entry.getGame_mode().equals("4")) {
                modeKiller += 1;
            }
            else {
                modeEnglish += 1;
            }
        }

        // game modes 1, 2, 3, 4, 5
        List<Integer> list = Arrays.asList(mode301, modeRound, modeCricket, modeKiller, modeEnglish);
        Integer mostPlayed = Collections.max(list);

        int i = 0;
        for(Integer val : list) {
            if(val == mostPlayed) {
                newFavGameMode = Integer.toString(i + 1);
            }
            i++;
        }

        return newFavGameMode;
    }

    // find total games
    public int newTotalG() {
        int newTotalGames = 0;

        for(GameEntry entry : this.gameEntryList) {
            newTotalGames++;
        }

        return newTotalGames;
    }

    // finds total matches
    public int newTotalM() {
        int newTotalMatches = 0;
        for(GameEntry entry : this.gameEntryList) {
            if(entry.getMatch() == 1) { // a match, update match total
                newTotalMatches++;
            }
        }

        return newTotalMatches;
    }

    // finds new high, low, avg scores
    public List<Float> newHighLowAvg(String user_ID, int newTotalGames, StatsEntry statsEntry) {
        float oldHighScore = statsEntry.getHigh_score();
        float oldLowScore = statsEntry.getLow_score();
        float newHighScore = 0;
        float newLowScore = 0;
        float newAvgScore = 0;
        float totalScore = 0;
        float tempScore; // if you set it to 0 if might change the low

        // high and low and avg score calculations
        for(GameEntry entry : this.gameEntryList) {
            // check if match first
            // NOT A MATCH
            if(entry.getMatch() == 0) {
                tempScore = entry.getPlayer_1_score();
            }

            // MATCH, USE USER_ID
            else {
                String user1 = entry.getUser_ID_1();
                // USER 1
                if(user1.equals(user_ID)) {
                    tempScore = entry.getPlayer_1_score();
                }

                // USER 2
                else {
                    tempScore = entry.getPlayer_2_score();
                }
            }

            // this is not the first statistics entry, so you must compare the new data being
            // calculated to the old one
            if (tempScore > oldHighScore) {
                oldHighScore = tempScore;
            } else if (tempScore < oldLowScore) {
                oldLowScore = tempScore;
            }

            // add up scores to find average later
            totalScore += tempScore;
        }

        // find new average score
        newAvgScore = totalScore / newTotalGames; // new average score
        newAvgScore = BigDecimal.valueOf(newAvgScore)
                .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                .floatValue();

        newHighScore = oldHighScore;
        newLowScore = oldLowScore;

        List<Float> list = new ArrayList<Float>();
        list.add(newHighScore);
        list.add(newLowScore);
        list.add(newAvgScore);

        return list;
    }

    // for a non-existent stats entry
    // how to write this more efficiently?
    public List<Float> firstNewHighLowAvg(String user_ID, int newTotalGames) {
        float newAvgScore = 0;
        float totalScore = 0;
        float tempScore;

        // first game entry in list
        float currentLow;
        float currentHigh;
        GameEntry gameEntry = this.gameEntryList.get(0);
        currentHigh = currentLow = gameEntry.getPlayer_1_score();
        if(gameEntry.getMatch() == 1) { // if match, change data if it's second user
            String user2 = gameEntry.getUser_ID_2();
            // USER 2
            if(user2.equals(user_ID)) {
                currentHigh = currentLow = gameEntry.getPlayer_2_score(); // score that's being compared is user 2
            }
        }

        // high and low and avg score calculations
        for(GameEntry entry : this.gameEntryList) {
            // check if match first
            // NOT A MATCH
            if(entry.getMatch() == 0) {
                tempScore = entry.getPlayer_1_score(); // score that's being compared is user 1
            }

            // MATCH, USE USER_ID
            else {
                String user1 = entry.getUser_ID_1();
                // USER 1
                if(user1.equals(user_ID)) {
                    tempScore = entry.getPlayer_1_score(); // score that's being compared is user 1
                }

                // USER 2
                else {
                    tempScore = entry.getPlayer_2_score(); // score that's being compared is user 2
                }
            }

            if (tempScore > currentHigh) {
                currentHigh = tempScore;
            } else if (tempScore < currentLow) {
                currentLow = tempScore;
            }
            // add up scores to find average later
            totalScore += tempScore;
        }

        // find new average score
        newAvgScore = totalScore / newTotalGames; // new average score
        newAvgScore = BigDecimal.valueOf(newAvgScore)
                .setScale(2, BigDecimal.ROUND_HALF_DOWN)
                .floatValue();


        List<Float> list = new ArrayList<Float>();
        list.add(currentHigh);
        list.add(currentLow);
        list.add(newAvgScore);

        return list;
    }

    // finds new matches won, lost, tied
    public List<Integer> newWonLostTied(String user_ID) {

        int newMatchesWon = 0;
        int newMatchesLost = 0;
        int newMatchesTied = 0;

        // high and low and avg score calculations
        for(GameEntry entry : this.gameEntryList) {
            String m = String.valueOf(entry.getMatch());
            if(m.equals("1")) { // only if it's a match
                if (entry.getWinner_user_ID().equals(user_ID)) { // this user won
                    newMatchesWon++;
                } else if (entry.getWinner_user_ID().equals("tie")) {
                    newMatchesTied++;
                } else {
                    newMatchesLost++;
                }
            }
        }

        List<Integer> list = new ArrayList<Integer>();
        list.add(newMatchesWon);
        list.add(newMatchesLost);
        list.add(newMatchesTied);

        return list;
    }
}
