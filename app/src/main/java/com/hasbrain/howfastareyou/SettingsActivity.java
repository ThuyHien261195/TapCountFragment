package com.hasbrain.howfastareyou;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hasbrain.howfastareyou.SettingsUtils.MIN_TIME_LIMIT;
import static com.hasbrain.howfastareyou.SettingsUtils.PREF_SETTINGS_FILE;

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

    private SettingsModel settingsModel;
    private int realProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(R.string.settings_text);

        ButterKnife.bind(this);
        settingsModel = SettingsUtils.getSettingsModel(this);
        initViews();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        checkSettingsChange();
        super.onBackPressed();
    }

    private void checkSettingsChange() {
        if (realProgress != settingsModel.getTimeLimit() ||
                switchRecordScore.isChecked() != settingsModel.isRecordState()) {
            storeSettingsVariable();
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
    }

    private void storeSettingsVariable() {
        SharedPreferences sharedPref = this.getSharedPreferences(PREF_SETTINGS_FILE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(SettingsUtils.PREF_TIME_LIMIT, realProgress);
        editor.putBoolean(SettingsUtils.PREF_RECORD_SCORE, switchRecordScore.isChecked());
        editor.apply();
    }

    private void initViews() {
        switchRecordScore.setChecked(settingsModel.isRecordState());
        realProgress = settingsModel.getTimeLimit();
        textViewTimeLimit.setText(getString(R.string.title_result_time_limit, realProgress));

        if (settingsModel.getTimeLimit() <= MIN_TIME_LIMIT) {
            seekBarTimeLimit.setProgress(0);
        } else {
            seekBarTimeLimit.setProgress(settingsModel.getTimeLimit());
        }

        seekBarTimeLimit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < SettingsUtils.MIN_TIME_LIMIT) {
                    progress = SettingsUtils.MIN_TIME_LIMIT;
                }
                realProgress = progress;
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
