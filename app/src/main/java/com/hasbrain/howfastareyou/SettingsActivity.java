package com.hasbrain.howfastareyou;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.widget.SeekBar;
import android.widget.TextView;

import com.hasbrain.howfastareyou.Model.SettingsModel;
import com.hasbrain.howfastareyou.Utils.SettingsUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hasbrain.howfastareyou.Utils.SettingsUtils.MIN_TIME_LIMIT;

/**
 * Created by Jupiter (vu.cao.duy@gmail.com) on 10/14/15.
 */
public class SettingsActivity extends AppCompatActivity {

    public static final int numToConvertSec = 1000;

    @BindView(R.id.seek_bar_time_limit)
    SeekBar seekBarTimeLimit;

    @BindView(R.id.text_time_limit)
    TextView textViewTimeLimit;

    @BindView(R.id.switch_record_score)
    SwitchCompat switchRecordScore;

    private SettingsModel settingsModel;
    private int timeLimit;

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
        if (timeLimit != settingsModel.getTimeLimit() ||
                switchRecordScore.isChecked() != settingsModel.isRecordState()) {
            SettingsUtils.storeSettingsVariable(this, timeLimit, switchRecordScore.isChecked());
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
    }

    private void initViews() {
        switchRecordScore.setChecked(settingsModel.isRecordState());
        timeLimit = settingsModel.getTimeLimit();

        int seekBarProgress = timeLimit / numToConvertSec;
        textViewTimeLimit.setText(getString(R.string.title_result_time_limit, seekBarProgress));

        if (seekBarProgress <= MIN_TIME_LIMIT) {
            seekBarTimeLimit.setProgress(0);
        } else {
            seekBarTimeLimit.setProgress(seekBarProgress);
        }

        seekBarTimeLimit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < SettingsUtils.MIN_TIME_LIMIT) {
                    progress = SettingsUtils.MIN_TIME_LIMIT;
                }
                timeLimit = progress * numToConvertSec;
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
