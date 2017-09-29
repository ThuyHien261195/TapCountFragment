package com.hasbrain.howfastareyou.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.hasbrain.howfastareyou.Model.SettingsModel;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by thuyhien on 9/27/17.
 */

public class SettingsUtils {
    public static final String PREF_SETTINGS_FILE_NAME = "SettingsPrefFile";
    public static final String PREF_TIME_LIMIT = "PREF_TIME_LIMIT";
    public static final String PREF_RECORD_SCORE = "PREF_RECORD_SCORE";
    public static final int MIN_TIME_LIMIT = 5;
    public static final int DEFAULT_TIME_LIMIT = 10000;
    public static final boolean DEFAULT_RECORD_STATE = true;

    public static SettingsModel getSettingsModel(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREF_SETTINGS_FILE_NAME, MODE_PRIVATE);
        int timeLimit = sharedPref.getInt(PREF_TIME_LIMIT, DEFAULT_TIME_LIMIT);
        boolean recordState = sharedPref.getBoolean(PREF_RECORD_SCORE, DEFAULT_RECORD_STATE);
        return new SettingsModel(timeLimit, recordState);
    }
}
