package com.hasbrain.howfastareyou;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thuyhien on 9/28/17.
 */

public class FileProvider {
    private static final String highScoreFileName = "HighScoreFile";

    public static boolean checkFileExist(Context context) {
        File file = context.getFileStreamPath(highScoreFileName);
        return !(file == null || !file.exists());
    }

    public static List<HighScore> readDataInFile(Context context) {
        List<HighScore> highScoreList = new ArrayList<>();
        FileInputStream fileInputStream;

        if (checkFileExist(context)) {
            try {
                fileInputStream = context.openFileInput(highScoreFileName);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    highScoreList.add(convertStringToHighScoreModel(line));
                }
            } catch (FileNotFoundException e) {
                Toast.makeText(context,
                        context.getString(R.string.error_file_not_found, highScoreFileName),
                        Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return highScoreList;
    }

    public static void writeDataInFile(Context context, HighScore highScore) {
        FileOutputStream fileOutputStream;
        if (highScore != null) {
            String highScoreString = highScore.getTime() + "\t"
                    + String.valueOf(highScore.getScore()) + "\n";

            try {
                fileOutputStream = context.openFileOutput(highScoreFileName, Context.MODE_APPEND);
                fileOutputStream.write(highScoreString.getBytes());
            } catch (FileNotFoundException e) {
                Toast.makeText(context,
                        context.getString(R.string.error_file_not_found, highScoreFileName),
                        Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static HighScore convertStringToHighScoreModel(String highScoreline) {
        String[] dataSeparated = highScoreline.split("\t");
        return new HighScore(dataSeparated[0], Integer.parseInt(dataSeparated[1]));
    }
}
