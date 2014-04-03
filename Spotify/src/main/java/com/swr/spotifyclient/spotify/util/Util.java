package com.swr.spotifyclient.spotify.util;

import android.os.AsyncTask;
import android.os.Build;

import java.util.List;

/**
 * Created by Steve on 4/1/2014.
 */
public class Util {

    public static String  version = "SpotifyClient 1.0";  // update for each new build
    public static boolean  doLog = true;  // enable/disable logging
    public static boolean writeLogToFile = true; // creates local log on the device that can be emailed, and enables test buttons on settings page

    public final static String genericErrorMsg = "Oops, an unexpected error occurred, please try again later";

    public static final boolean isEmpty(List list) {
        if(list == null || list.size() == 0) return true;
        return false;
    }

    public static final boolean isEmpty(String str) {
        return str == null || str.length() == 0 || str.trim().length() == 0;
    }

    public static final boolean isEqual(String str, String str2) {
        return !(str == null || str.length() == 0 || str.trim().length() == 0) && !(str2 == null || str2.length() == 0 || str2.trim().length() == 0) && str2.equals(str2);
    }


    public static void runTask(AsyncTask<Void, Void, Boolean> task){
        if(task == null) return;

        if (Build.VERSION.SDK_INT >= 11) {
            //--post GB use serial executor by default --
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            //--GB uses ThreadPoolExecutor by default--
            task.execute();
        }
    }

}
