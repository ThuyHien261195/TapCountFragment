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

import com.hasbrain.howfastareyou.Model.HighScore;
import com.hasbrain.howfastareyou.Utils.FileProvider;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jupiter (vu.cao.duy@gmail.com) on 10/14/15.
 */
public class TapCountResultFragment extends Fragment {

    private static final String BUNDLE_HIGH_SCORE_LIST = "BUNDLE_HIGH_SCORE_LIST";

    @BindView(R.id.recycler_view_high_score)
    RecyclerView recyclerViewHighScore;

    private ArrayList<HighScore> highScoreList;
    private HighScoreAdapter highScoreAdapter;

    public static TapCountResultFragment newInstance(ArrayList<HighScore> highScoreList) {
        TapCountResultFragment tapCountResultFragment = new TapCountResultFragment();
        Bundle highScoreBundle = new Bundle();
        highScoreBundle.putSerializable(BUNDLE_HIGH_SCORE_LIST, highScoreList);
        tapCountResultFragment.setArguments(highScoreBundle);
        return tapCountResultFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getHighScoreBundle();

        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        View view = inflater.inflate(R.layout.fragment_high_score, container, false);

        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initViews();
    }

    public void getHighScoreBundle() {
        Serializable highScoreSerializable = getArguments().getSerializable(BUNDLE_HIGH_SCORE_LIST);
        highScoreList = (ArrayList<HighScore>) highScoreSerializable;
        getArguments().remove(BUNDLE_HIGH_SCORE_LIST);
    }

    private void initViews() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewHighScore.setLayoutManager(linearLayoutManager);
        highScoreAdapter = new HighScoreAdapter(highScoreList);
        recyclerViewHighScore.setAdapter(highScoreAdapter);
    }

    public void addHighScoreIntoList(HighScore highScore) {
        FileProvider.writeDataIntoFile(getContext(), highScore);
        highScoreList.add(highScore);
        highScoreAdapter.notifyDataSetChanged();
    }
}
