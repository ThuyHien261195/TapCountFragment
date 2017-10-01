package com.hasbrain.howfastareyou.Utils;

import android.content.Context;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hasbrain.howfastareyou.Model.HighScore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by thuyhien on 9/28/17.
 */

public class FileProvider {
    private static final String HIGH_SCORE_FILE_NAME = "HighScoreFile";
    private static final String HIGH_SCORE_FOLDER = "HighScoreFolder";
    private static final String TAG_TIME = "time";
    private static final String TAG_SCORE = "score";

    public static boolean checkFileInternalExist(Context context) {
        File file = context.getFileStreamPath(HIGH_SCORE_FILE_NAME);
        return !(file == null || !file.exists());
    }

    public static ArrayList<HighScore> readDataFromFile(Context context) {
        ArrayList<HighScore> highScoreList = new ArrayList<>();

        try {
            FileInputStream fileInputStream = createFileInputStream(context);
            if (fileInputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    HighScore highScore = convertGsonToHighScoreModel(line);
                    if (highScore != null) {
                        highScoreList.add(highScore);
                    }
                }
                fileInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return highScoreList;
    }

    public static void writeDataIntoFile(Context context, HighScore highScore) {
        if (highScore != null) {
            try {
                FileOutputStream fileOutputStream = createFileOutputStream(context);
                if (fileOutputStream != null) {
                    String gsonHighScore = createHighScoreToGson(highScore);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                    BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
                    bufferedWriter.write(gsonHighScore);
                    bufferedWriter.newLine();
                    bufferedWriter.close();
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static FileOutputStream createFileOutputStream(Context context) {
        FileOutputStream fileOutputStream = null;

        try {
            if (isExternalStorageWritable()) {
                File file = createHighScoreFileExternalStorage(context);
                if (file != null) {
                    fileOutputStream = new FileOutputStream(file, true);
                    return fileOutputStream;
                }
            }
            fileOutputStream = context.openFileOutput(HIGH_SCORE_FILE_NAME, Context.MODE_APPEND);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fileOutputStream;
    }

    private static FileInputStream createFileInputStream(Context context) {
        FileInputStream fileInputStream = null;

        try {
            if (isExternalStorageWritable()) {
                File file = createHighScoreFileExternalStorage(context);
                if (file != null) {
                    fileInputStream = new FileInputStream(file);
                    return fileInputStream;
                }
            }
            if (checkFileInternalExist(context)) {
                fileInputStream = context.openFileInput(HIGH_SCORE_FILE_NAME);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fileInputStream;
    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    private static File createHighScoreFileExternalStorage(Context context) {
        File dir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),
                HIGH_SCORE_FOLDER);
        if (!dir.exists() && !dir.mkdirs()) {
            return null;
        }
        return new File(dir, HIGH_SCORE_FILE_NAME);
    }

    private static HighScore convertJSONToHighScoreModel(String highScoreline) {
        try {
            JSONObject jsonObject = new JSONObject(highScoreline);
            String time = jsonObject.getString(TAG_TIME);
            int score = jsonObject.getInt(TAG_SCORE);
            return new HighScore(time, score);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static JSONObject createHighScoreToJSON(HighScore highScore) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(TAG_TIME, highScore.getTime());
            jsonObject.put(TAG_SCORE, highScore.getScore());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private static String createHighScoreToGson(HighScore highScore) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.toJson(highScore);
    }

    private static HighScore convertGsonToHighScoreModel(String highScoreLine) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.fromJson(highScoreLine, HighScore.class);
    }
}
