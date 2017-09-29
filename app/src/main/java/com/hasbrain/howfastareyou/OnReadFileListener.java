package com.hasbrain.howfastareyou;

import com.hasbrain.howfastareyou.Model.HighScore;

import java.util.ArrayList;

/**
 * Created by thuyhien on 9/29/17.
 */

public interface OnReadFileListener {
    void onReadHighScoreSuccess(ArrayList<HighScore> highScoreList);
}
