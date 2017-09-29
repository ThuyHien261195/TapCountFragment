package com.hasbrain.howfastareyou;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hasbrain.howfastareyou.Model.HighScore;

import java.util.List;

/**
 * Created by thuyhien on 9/26/17.
 */

public class HighScoreAdapter extends RecyclerView.Adapter<HighScoreViewHolder> {

    private List<HighScore> highScoreList;

    public HighScoreAdapter(List<HighScore> highScoreList) {
        this.highScoreList = highScoreList;
    }

    @Override
    public HighScoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_high_score, parent, false);
        return new HighScoreViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(HighScoreViewHolder holder, int position) {
        holder.bindHighScoreView(highScoreList.get(position));
    }

    @Override
    public int getItemCount() {
        return highScoreList.size();
    }
}
