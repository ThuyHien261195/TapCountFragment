package com.hasbrain.howfastareyou;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hasbrain.howfastareyou.Model.HighScore;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by thuyhien on 9/26/17.
 */

public class HighScoreViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.text_time)
    TextView textViewTime;

    @BindView(R.id.text_score)
    TextView textViewScore;

    public HighScoreViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindHighScoreView(HighScore highScore) {
        textViewTime.setText(highScore.getTime());
        textViewScore.setText(String.valueOf(highScore.getScore()));
    }
}
