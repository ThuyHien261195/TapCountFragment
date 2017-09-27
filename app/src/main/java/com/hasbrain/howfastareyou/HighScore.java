package com.hasbrain.howfastareyou;

/**
 * Created by thuyhien on 9/26/17.
 */

public class HighScore {
    private String time;
    private String score;

    public HighScore(String time, String score) {
        this.time = time;
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
