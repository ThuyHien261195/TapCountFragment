package com.hasbrain.howfastareyou;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jupiter (vu.cao.duy@gmail.com) on 10/14/15.
 */
public class TapCountResultFragment extends Fragment {

    @BindView(R.id.recycler_view_high_score)
    RecyclerView recyclerViewHighScore;

    private List<HighScore> highScoreList;
    private HighScoreAdapter highScoreAdapter;
    private OnDataLoadListener highScoreLoadListener;

    public static TapCountResultFragment newInstance() {
        return new TapCountResultFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            highScoreLoadListener = (OnDataLoadListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + getString(R.string.error_implement_on_data_load_listener));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        View view = inflater.inflate(R.layout.fragment_high_score, container, false);

        ButterKnife.bind(this, view);

        if (!getRetainInstance()) {
            highScoreList = FileProvider.readDataInFile(getContext());
            if (highScoreList.size() > 0) {
                highScoreLoadListener.onGetBestHighScoreListener(
                        highScoreList.get(highScoreList.size() - 1).getScore());
            }
        }
        initViews();

        setRetainInstance(true);
        return view;
    }

    private void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewHighScore.setLayoutManager(linearLayoutManager);
        highScoreAdapter = new HighScoreAdapter(highScoreList);
        recyclerViewHighScore.setAdapter(highScoreAdapter);
    }

    public void addHighScoreIntoList(HighScore highScore) {
        FileProvider.writeDataInFile(getContext(), highScore);
        highScoreList.add(highScore);
        highScoreAdapter.notifyDataSetChanged();
    }

    public interface OnDataLoadListener {
        void onGetBestHighScoreListener(int bestHighScore);
    }
}
