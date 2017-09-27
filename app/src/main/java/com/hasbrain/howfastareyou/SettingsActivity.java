package com.hasbrain.howfastareyou;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hasbrain.howfastareyou.SettingsUtils.DEFAULT_RECORD_STATE;
import static com.hasbrain.howfastareyou.SettingsUtils.DEFAULT_TIME_LIMIT;
import static com.hasbrain.howfastareyou.SettingsUtils.MIN_TIME_LIMIT;
import static com.hasbrain.howfastareyou.SettingsUtils.PREF_RECORD_SCORE;
import static com.hasbrain.howfastareyou.SettingsUtils.PREF_SETTINGS_FILE;
import static com.hasbrain.howfastareyou.SettingsUtils.PREF_TIME_LIMIT;

/**
 * Created by Jupiter (vu.cao.duy@gmail.com) on 10/14/15.
 */
public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.seek_bar_time_limit)
    SeekBar seekBarTimeLimit;

    @BindView(R.id.text_time_limit)
    TextView textViewTimeLimit;

    @BindView(R.id.switch_record_score)
    SwitchCompat switchRecordScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(R.string.settings_text);

        ButterKnife.bind(this);
        initViews();
    }

    @Override
    protected void onPause() {
        super.onPause();
        storeSettingsVariable();
    }

    private void storeSettingsVariable() {
        SettingsUtils.recordState = switchRecordScore.isChecked();
        TapCountActivity.TIME_COUNT = SettingsUtils.timeLimit * SettingsUtils.numToConvertSec;

        SharedPreferences sharedPref = this.getSharedPreferences(PREF_SETTINGS_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(SettingsUtils.PREF_TIME_LIMIT, SettingsUtils.timeLimit);
        editor.putBoolean(SettingsUtils.PREF_RECORD_SCORE, SettingsUtils.recordState);
        editor.apply();
    }

    private void initViews() {
        switchRecordScore.setChecked(SettingsUtils.recordState);
        textViewTimeLimit.setText(getString(R.string.title_result_time_limit, SettingsUtils.timeLimit));
        if (SettingsUtils.timeLimit <= MIN_TIME_LIMIT) {
            seekBarTimeLimit.setProgress(0);
        } else {
            seekBarTimeLimit.setProgress(SettingsUtils.timeLimit);
        }
        seekBarTimeLimit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < SettingsUtils.MIN_TIME_LIMIT) {
                    progress = SettingsUtils.MIN_TIME_LIMIT;
                }
                SettingsUtils.timeLimit = progress;
                textViewTimeLimit.setText(getString(R.string.title_result_time_limit, progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
