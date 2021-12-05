package com.example.virtualdarts2;

import java.util.List;

public class ItemClass {

    // Integers assigned to each layout
    // these are declared static so that they can
    // be accessed from the class name itself
    // And final so that they are not modified later
    public static final int LayoutOne = 0; // match
    public static final int LayoutTwo = 1; // not a match

    // This variable ViewType specifies
    // which out of the two layouts
    // is expected in the given item
    private int viewType;

    // String variable to hold the TextView
    // of the first item.
    private String matchMode;
    private String matchDate;
    private String matchYourScore;
    private String matchOtherScore;
    private String matchWinner;

    // public constructor for the first layout
    public ItemClass(int viewType, String matchMode, String matchDate, String matchYourScore,
                     String matchOtherScore, String matchWinner) {
        this.matchMode = matchMode;
        this.matchDate = matchDate;
        this.matchYourScore = matchYourScore;
        this.matchOtherScore = matchOtherScore;
        this.matchWinner = matchWinner;
        this.viewType = viewType;
    }

    // getter and setter methods for the text variables
    public String getMatchMode() {
        return matchMode;
    }

    public String getMatchDate() {
        return matchDate;
    }

    public String getMatchYourScore() {
        return matchYourScore;
    }

    public String getMatchOtherScore() {
        return matchOtherScore;
    }

    public String getMatchWinner() {
        return matchWinner;
    }

    public void setMatchMode(String matchMode) {
        this.matchMode = matchMode;
    }

    public void setMatchDate(String matchDate) {
        this.matchDate = matchDate;
    }

    public void setMatchYourScore(String matchYourScore) {
        this.matchYourScore = matchYourScore;
    }

    public void setMatchOtherScore(String matchOtherScore) {
        this.matchOtherScore = matchOtherScore;
    }

    public void setMatchWinner(String matchWinner) {
        this.matchWinner = matchWinner;
    }

    // Variables for the item of second layout
    private String singleMode, singleDate, singleScore;

    // public constructor for the second layout
    public ItemClass(int viewType, String singleMode, String singleDate, String singleScore) {
        this.singleMode = singleMode;
        this.singleDate = singleDate;
        this.singleScore = singleScore;
        this.viewType = viewType;
    }

    // getter and setter methods for
    // the variables of the second layout


    public String getSingleMode() {
        return singleMode;
    }

    public String getSingleDate() {
        return singleDate;
    }

    public String getSingleScore() {
        return singleScore;
    }

    public void setSingleMode(String singleMode) {
        this.singleMode = singleMode;
    }

    public void setSingleDate(String singleDate) {
        this.singleDate = singleDate;
    }

    public void setSingleScore(String singleScore) {
        this.singleScore = singleScore;
    }

    public int getViewType() { return viewType; }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
