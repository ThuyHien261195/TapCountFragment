package com.hasbrain.howfastareyou.Utils;

import android.app.Activity;
import android.os.AsyncTask;

import com.hasbrain.howfastareyou.Model.HighScore;
import com.hasbrain.howfastareyou.OnReadFileListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thuyhien on 9/29/17.
 */

public class ReadFileTask extends AsyncTask<Void, Void, ArrayList<HighScore>> {

    private WeakReference<Activity> weakContext;
    private OnReadFileListener readFileListener;

    public ReadFileTask(Activity activity) {
        weakContext = new WeakReference<>(activity);
        readFileListener = (OnReadFileListener) activity;
    }

    @Override
    protected ArrayList<HighScore> doInBackground(Void... params) {
        return FileProvider.readDataFromInternalFile(weakContext.get());
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<HighScore> highScores) {
        readFileListener.onReadHighScoreSuccess(highScores);
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
