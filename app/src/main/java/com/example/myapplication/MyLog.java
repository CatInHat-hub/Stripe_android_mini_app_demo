package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class MyLog {

    private static final String TAG = "G4";

    private static String getMsg(String appendix){
        return String.format("%50s %s", getMethod(), appendix);
    }
    // verbose
    public static void dump(String appendix) {
        Log.v(TAG, getMsg(appendix));
    }
    public static void funcStart() {
        Log.v(TAG, getMsg(">"));
    }
    // 自由
    public static void debug(String appendix) {
        Log.d(TAG, getMsg(appendix));
    }
    public static void temp(String appendix) {
        int start = 0;
        int end = 0;
        final int length = appendix.length();
        while (end < length) {
            end = start + 200;
            if (end > length) {
                end = length;
            }
            String str = appendix.substring(start, end);
            start = end;
            Log.d(TAG, getMsg("@@@ " + str));
        }
    }

    public static void msg(String appendix, int logLevel) {
        if (logLevel == Log.WARN) Log.w(TAG, getMsg(appendix));
//        else if (logLevel == Log.INFO) Log.i(TAG, getMsg(appendix));
//        else if (logLevel == Log.DEBUG)Log.d(TAG, getMsg(appendix));
        else Log.i(TAG, getMsg(appendix));
    }
    public static void info(String appendix) {
        Log.i(TAG, getMsg(appendix));
    }
    public static void warn(String appendix) {
        Log.w(TAG, getMsg(appendix));
    }
    public static void error(String appendix) {
        Log.e(TAG, getMsg(appendix));
    }
    public static void error(Exception e) {
        Log.e(TAG, getMsg(e.getMessage()));
        e.printStackTrace();
    }
    private static String getMethod() {
        final StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[5];
        return String.format(Locale.JAPANESE, "%s(%s:%d)",
                stackTraceElement.getMethodName(),
                stackTraceElement.getFileName(),
                stackTraceElement.getLineNumber());
    }

    //region log file write

    // テスト用 log.txtファイルに書き込む
    public static void writeLogFile(String str, Context context) {

        MyLog.dump("str=" + str);

        str =  String.format("%s %s %s\n", getDateTime(), getMethod(), str);

        File file = getLogFilePath(context);
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            fw = new FileWriter(file, true);
            bw = new BufferedWriter(fw);
            bw.write(str);
            bw.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != bw)
                    bw.close();
                if (null != fw)
                    fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // log.txtファイルを削除する
    public static void deleteLogFile(Context context) {

        MyLog.dump("");

        File f = getLogFilePath(context);

        if (f.exists() == false) {
            return;
        }
        f.delete();
    }

    private static File getLogFilePath(Context context) {

        MyLog.dump("");

        if (context == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(context.getFilesDir());
        builder.append("/log.txt");

        return new File(builder.toString());
    }

    private static String getDateTime() {

        LocalDateTime date = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");
        return formatter.format(date);
    }



    //endregion
}
