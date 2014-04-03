package com.swr.spotifyclient.spotify.util;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Steve on 4/1/2014.
 */
public class MyLog {

    private static final int STACK_TRACE_LEVELS_UP = 5;
    private static final String FILENAME = "log.txt";
    private static final String TAG = "SpotifyClient";

    private static void writeToFile(String msg){
        if(Util.writeLogToFile){
            appendLog(msg);
        }
    }
    public static void d(String tag, String msg) {

        if (Util.doLog){
            Log.d(tag, getClassNameMethodNameAndLineNumber() + msg);
            writeToFile(getClassNameMethodNameAndLineNumber() + msg);
        }
    }

    public static void i(String tag, String msg) {

        if (Util.doLog){
            Log.i(tag, getClassNameMethodNameAndLineNumber() + msg);
            writeToFile(getClassNameMethodNameAndLineNumber() + msg);
        }
    }

    public static void e(String tag, String msg) {

        if (Util.doLog){
            Log.e(tag, getClassNameMethodNameAndLineNumber() + msg);
            writeToFile(getClassNameMethodNameAndLineNumber() + msg);
        }
    }

    public static void w(String tag, String msg) {

        if (Util.doLog){
            Log.w(tag, getClassNameMethodNameAndLineNumber() + msg);
            writeToFile(getClassNameMethodNameAndLineNumber() + msg);
        }
    }


    /**
     * Get the current line number. Note, this will only work as called from
     * this class as it has to go a predetermined number of steps up the stack
     * trace. In this case 5.
     *
     * @author kvarela
     * @return int - Current line number.
     */
    private static int getLineNumber(){
        return Thread.currentThread().getStackTrace()[STACK_TRACE_LEVELS_UP].getLineNumber();
    }

    /**
     * Get the current class name. Note, this will only work as called from this
     * class as it has to go a predetermined number of steps up the stack trace.
     * In this case 5.
     *
     * @author kvarela
     * @return String - Current line number.
     */
    private static String getClassName(){
        String fileName = Thread.currentThread().getStackTrace()[STACK_TRACE_LEVELS_UP].getFileName();
        return fileName.substring(0, fileName.length() - 5);
    }

    /**
     * Get the current method name. Note, this will only work as called from
     * this class as it has to go a predetermined number of steps up the stack
     * trace. In this case 5.
     *
     * @author kvarela
     * @return String - Current line number.
     */
    private static String getMethodName(){
        return Thread.currentThread().getStackTrace()[STACK_TRACE_LEVELS_UP].getMethodName();
    }

    /**
     * Returns the class name, method name, and line number from the currently
     * executing log call in the form <class_name>.<method_name>()-<line_number>
     *
     * @author kvarela
     * @return String - String representing class name, method name, and line
     *         number.
     */
    private static String getClassNameMethodNameAndLineNumber(){
        return "[" + getClassName() + "." + getMethodName() + "()-" + getLineNumber() + "]: ";
    }

    public synchronized static void clearLog(){

        if(Util.doLog) {
            Log.d(TAG, "Starting");
        }

        File logFile = new File("sdcard/log.txt");
        try {
            //Log.d(TAG, "Deleting the file");
            logFile.delete();
        }
        catch (Exception e){
            if(Util.doLog) {
                Log.e(TAG, "Failed to empty the file " + e.toString());
            }
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if(Util.doLog) {
            Log.d(TAG, "Ending");
        }

    }

    public synchronized static void appendLog(String text){

        File logFile = new File("sdcard/log.txt");
        if (!logFile.exists())
        {
            //Log.d(TAG, "Creating the file");

            try { logFile.createNewFile(); }
            catch (IOException e){
                // Log.e(TAG, "Failed to create the file " + e.toString());
                e.printStackTrace();
                return;
            }
        }
        try{
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e){
            //  Log.e(TAG, "Failed to write to the file " + e.toString());
            e.printStackTrace();
            return;
        }
    }

}

