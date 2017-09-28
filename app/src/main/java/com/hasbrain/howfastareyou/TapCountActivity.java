package com.hasbrain.howfastareyou;

import android.content.Intent;
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
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TapCountActivity extends AppCompatActivity
        implements TapCountResultFragment.OnDataLoadListener {

    public static final String CLOCK_STATE = "ClockState";
    public static final String TIME_WHEN_STOP = "TimeWhenStopped";
    public static final String SCORE = "Score";
    public static final String BEST_HIGH_SCORE = "BestHighScore";
    public static final String TAG_TAP_COUNT_RESULT_FRAGMENT = "TapCountResultFragment";
    public static final int REQUEST_CODE_SETTINGS_ACTIVITY = 1;

    @BindView(R.id.bt_tap)
    Button btTap;
    @BindView(R.id.bt_start)
    Button btStart;
    @BindView(R.id.tv_time)
    Chronometer tvTime;
    @BindView(R.id.tv_score)
    AppCompatTextView tvScore;

    private TapCountResultFragment tapCountResultFragment;
    public SettingsModel settingsModel;

    private int timeWhenStop = 0;
    private int tapCount = 0;
    private int bestHighScore = 0;

    public static final int CLOCK_RESUMED = 0;
    public static final int CLOCK_PAUSED = 1;
    public static final int CLOCK_STOPPED = 2;
    private int clockState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        ButterKnife.bind(this);

        settingsModel = SettingsUtils.getSettingsModel(this);
        initViews();
    }

    private void initViews() {
        tvTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if (SystemClock.elapsedRealtime() - tvTime.getBase() >= settingsModel.getTimeLimit()) {
                    setClockState(CLOCK_STOPPED);
                }
                timeWhenStop = (int) (SystemClock.elapsedRealtime() - tvTime.getBase());
            }
        });

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
            tapCount = savedInstanceState.getInt(SCORE);
            timeWhenStop = savedInstanceState.getInt(TIME_WHEN_STOP);
            bestHighScore = savedInstanceState.getInt(BEST_HIGH_SCORE);

            tvScore.setText(String.valueOf(tapCount));
            tvTime.setBase(SystemClock.elapsedRealtime() - timeWhenStop);
            setClockState(savedInstanceState.getInt(CLOCK_STATE));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(CLOCK_STATE, clockState);
        outState.putInt(TIME_WHEN_STOP, timeWhenStop);
        outState.putInt(SCORE, tapCount);
        outState.putInt(BEST_HIGH_SCORE, bestHighScore);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (clockState != CLOCK_STOPPED) {
            setClockState(CLOCK_PAUSED);
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
            startActivityForResult(showSettingsActivity, REQUEST_CODE_SETTINGS_ACTIVITY);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SETTINGS_ACTIVITY && resultCode == RESULT_OK) {
            int oldTimeLimit = settingsModel.getTimeLimit();
            settingsModel = SettingsUtils.getSettingsModel(this);
            if (oldTimeLimit != settingsModel.getTimeLimit()) {
                setClockState(CLOCK_STOPPED);
            }
        }
    }

    @OnClick(R.id.bt_start)
    public void onStartBtnClicked(View v) {
        setClockState(CLOCK_RESUMED);
    }

    @OnClick(R.id.bt_tap)
    public void onTapBtnClicked(View v) {
        setTapCountValue(++tapCount);
    }

    @Override
    public void onGetBestHighScoreListener(int bestHighScore) {
        this.bestHighScore = bestHighScore;
    }

    private void setClockState(int state) {
        switch (state) {
            case CLOCK_RESUMED:
                if (clockState == CLOCK_PAUSED) {
                    startTapping(timeWhenStop);
                } else {
                    setTapCountValue(0);
                    startTapping(0);
                }
                break;
            case CLOCK_PAUSED:
                btStart.setText(getString(R.string.bt_resume_text));
                setCommonUIWhenPause();
                break;
            case CLOCK_STOPPED:
                btStart.setText(getString(R.string.bt_start_text));
                if (settingsModel.isRecordState()) {
                    saveHighScore();
                }
                setCommonUIWhenPause();
                break;
            default:
                break;
        }
        clockState = state;
    }

    private void startTapping(int timeWhenStop) {
        long startTime = SystemClock.elapsedRealtime() - timeWhenStop;
        tvTime.setBase(startTime);
        tvTime.start();
        btTap.setEnabled(true);
        btStart.setEnabled(false);
        btStart.setText(getString(R.string.bt_start_text));
    }

    private void setCommonUIWhenPause() {
        tvTime.stop();
        btTap.setEnabled(false);
        btStart.setEnabled(true);
    }

    private void setTapCountValue(int count) {
        tapCount = count;
        tvScore.setText(String.valueOf(tapCount));
    }

    private HighScore createNewHighScore() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
        String currentTime = simpleDateFormat.format(new Date());
        return new HighScore(currentTime, tapCount);
    }

    private void saveHighScore() {
        if (bestHighScore == 0 || tapCount > bestHighScore) {
            HighScore highScore = createNewHighScore();
            tapCountResultFragment.addHighScoreIntoList(highScore);
            bestHighScore = tapCount;
        }
    }
}
