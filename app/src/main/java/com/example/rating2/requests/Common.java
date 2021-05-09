package com.example.rating2.requests;

public class Common {

    private String status;
    private String error;
    private String opponent;
    private String battleId;
    private String winnerName;

    public Common() {
    }

    public Common(String status, String error, String opponent, String battleId, String winnerName) {
        this.status = status;
        this.error = error;
        this.opponent = opponent;
        this.battleId = battleId;
        this.winnerName=winnerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public String getBattleId() {
        return battleId;
    }

    public void setBattleId(String battleId) {
        this.battleId = battleId;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }
}
