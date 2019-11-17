package com.example.dipto.newsgoround.livescore;

public class Model {

    String id , matchType , matchStatus , date , team1 , team2 ;

    public Model(String id,  String team1, String team2 , String matchType, String matchStatus, String date) {
        this.id = id;
        this.matchType = matchType;
        this.matchStatus = matchStatus;
        this.date = date;
        this.team1 = team1;
        this.team2 = team2;
    }


    public String getId() {
        return id;
    }

    public String getMatchType() {
        return matchType;
    }

    public String getMatchStatus() {
        return matchStatus;
    }

    public String getDate() {
        return date;
    }

    public String getTeam1() {
        return team1;
    }

    public String getTeam2() {
        return team2;
    }
}
