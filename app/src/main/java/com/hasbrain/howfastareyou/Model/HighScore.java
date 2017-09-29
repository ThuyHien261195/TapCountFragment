package com.hasbrain.howfastareyou.Model;

import java.io.Serializable;

/**
 * Created by thuyhien on 9/26/17.
 */

public class HighScore {
    private String time;
    private int score;

    public HighScore(String time, int score) {
        this.time = time;
        this.score = score;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
