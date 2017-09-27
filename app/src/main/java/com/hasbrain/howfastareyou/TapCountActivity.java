package com.hasbrain.howfastareyou;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hasbrain.howfastareyou.SettingsUtils.DEFAULT_RECORD_STATE;
import static com.hasbrain.howfastareyou.SettingsUtils.DEFAULT_TIME_LIMIT;
import static com.hasbrain.howfastareyou.SettingsUtils.PREF_RECORD_SCORE;
import static com.hasbrain.howfastareyou.SettingsUtils.PREF_SETTINGS_FILE;
import static com.hasbrain.howfastareyou.SettingsUtils.PREF_TIME_LIMIT;

public class TapCountActivity extends AppCompatActivity {

    public static final String IS_PAUSE = "IsPause";
    public static final String TIME_WHEN_STOP = "imeWhenStoped";
    public static final String SCORE = "ore";
    public static final String TIME_WHEN_STOP_TEXT = "timeStopText";
    public static final String TAG_TAP_COUNT_RESULT_FRAGMENT = "TapCountResultFragment";
    public static int TIME_COUNT = 10000; //10s

    @BindView(R.id.bt_tap)
    Button btTap;
    @BindView(R.id.bt_start)
    Button btStart;
    @BindView(R.id.tv_time)
    Chronometer tvTime;
    @BindView(R.id.tv_score)
    AppCompatTextView tvScore;

    private TapCountResultFragment tapCountResultFragment;
    private long startTime;
    private int timeWhenStop = 0;
    private int tapCount = 0;
    private boolean isPause = false;
    private boolean isResume = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        ButterKnife.bind(this);

        tvTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                timeWhenStop = (int) (SystemClock.elapsedRealtime() - startTime);
                if (timeWhenStop >= TIME_COUNT) {
                    isPause = false;
                    pauseTapping();
                }
            }
        });
        initViews();
    }

    private void initViews() {
        getSettingsVariable();

        FragmentManager fragmentManger = getSupportFragmentManager();
        tapCountResultFragment = (TapCountResultFragment) fragmentManger.findFragmentByTag(
                TAG_TAP_COUNT_RESULT_FRAGMENT);

        if (tapCountResultFragment == null) {
            tapCountResultFragment = TapCountResultFragment.newInstance();
            FragmentTransaction fragmentTransaction = fragmentManger.beginTransaction();
            fragmentTransaction.add(R.id.fl_result_fragment,
                    tapCountResultFragment,
                    TAG_TAP_COUNT_RESULT_FRAGMENT);
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            isResume = true;
            isPause = savedInstanceState.getBoolean(IS_PAUSE);
            tapCount = savedInstanceState.getInt(SCORE);
            timeWhenStop = savedInstanceState.getInt(TIME_WHEN_STOP);

            tvScore.setText(String.valueOf(tapCount));
            tvTime.setText(savedInstanceState.getString(TIME_WHEN_STOP_TEXT));
            if (isPause) {
                btStart.setText(getString(R.string.bt_resume_text));
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(IS_PAUSE, isPause);
        outState.putInt(TIME_WHEN_STOP, timeWhenStop);
        outState.putInt(SCORE, tapCount);
        outState.putString(TIME_WHEN_STOP_TEXT, tvTime.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timeWhenStop < TIME_COUNT) {
            isPause = true;
            pauseTapping();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_settings) {
            Intent showSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(showSettingsActivity);
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.bt_start)
    public void onStartBtnClicked(View v) {
        if (isPause) {
            resumeTapping();
        } else {
            startTapping();
        }
    }

    @OnClick(R.id.bt_tap)
    public void onTapBtnClicked(View v) {
        setTapCountValue();
    }

    private void startTapping() {
        startTime = SystemClock.elapsedRealtime();
        tvTime.setBase(SystemClock.elapsedRealtime());
        tvTime.start();
        btTap.setEnabled(true);
        btStart.setEnabled(false);

        // Reset tap count value
        tapCount = 0;
        tvScore.setText(String.valueOf(tapCount));
    }

    private void pauseTapping() {
        if (isPause) {
            btStart.setText(getString(R.string.bt_resume_text));
        } else {
            // This case for stop tapping
            if (SettingsUtils.recordState) {
                HighScore highScore = createNewHighScore();
                tapCountResultFragment.addHighScoreIntoList(highScore);
            }
        }

        tvTime.stop();
        btTap.setEnabled(false);
        btStart.setEnabled(true);
    }

    private void resumeTapping() {
        startTime = SystemClock.elapsedRealtime() - timeWhenStop;
        tvTime.setBase(startTime);
        tvTime.start();
        btTap.setEnabled(true);
        btStart.setEnabled(false);
        btStart.setText(getString(R.string.bt_start_text));
        isResume = false;
    }

    private void setTapCountValue() {
        tapCount++;
        tvScore.setText(String.valueOf(tapCount));
    }

    private HighScore createNewHighScore() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String currentTime = simpleDateFormat.format(new Date());
        return new HighScore(currentTime, String.valueOf(tapCount));
    }

    private void getSettingsVariable() {
        SharedPreferences sharedPref = this.getSharedPreferences(PREF_SETTINGS_FILE, MODE_PRIVATE);
        SettingsUtils.timeLimit = sharedPref.getInt(PREF_TIME_LIMIT, DEFAULT_TIME_LIMIT);
        SettingsUtils.recordState = sharedPref.getBoolean(PREF_RECORD_SCORE, DEFAULT_RECORD_STATE);
        TIME_COUNT = SettingsUtils.timeLimit * SettingsUtils.numToConvertSec;
    }
}
