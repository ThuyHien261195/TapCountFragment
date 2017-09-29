package com.hasbrain.howfastareyou.Model;

/**
 * Created by thuyhien on 9/28/17.
 */

public class SettingsModel {
    private int timeLimit;
    private boolean recordState;

    public SettingsModel(int timeLimit, boolean recordState) {
        this.timeLimit = timeLimit;
        this.recordState = recordState;
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public boolean isRecordState() {
        return recordState;
    }

    public void setRecordState(boolean recordState) {
        this.recordState = recordState;
    }
}
