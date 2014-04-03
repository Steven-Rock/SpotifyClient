package com.swr.spotifyclient.spotify;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import com.swr.spotifyclient.spotify.util.ImageLoader;
import com.swr.spotifyclient.spotify.util.QuickViewImageLoader;

import java.util.List;

/**
 * Created by Steve on 4/1/2014.
 */
public abstract class AbstractActivity extends Activity{

    protected ProgressDialog dialog = null;
    protected final static String TAG = "SpotifyClient";

    protected Integer[] imageIDs = {};
    protected ImageLoader imageLoader = null;
    protected QuickViewImageLoader quickVideomageLoader = null;


    public abstract void setMedia(List media);

    protected void setActionBarText(String msg) {

        int actionBarTitleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        TextView actionBarTextView = (TextView) findViewById(actionBarTitleId);

        if(actionBarTextView != null){
            actionBarTextView.setText(msg);
        }

    }

    public void dismissProgressDialog() {
        if (dialog != null) {
            try {
                dialog.dismiss();
            } catch (Exception e) {
            }
            ;
        }
        dialog = null;
    }


    public final void createCancelProgressDialog(String title, String message, String buttonText) {

        try {
            dismissProgressDialog();

            dialog = new ProgressDialog(this, R.style.AppTheme);
            dialog.setCancelable(true);
            dialog.setMessage(message);
            dialog.setButton(buttonText, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    // Use either finish() or return() to either close the activity
                    // or just the dialog


                    //cancelRunningTasks();

                    return;
                }
            });
            dialog.show();
        } catch (Exception e) {
            // expected error - happens when trying to dismiss dialog but already on new screen
            //android.view.WindowManager$BadTokenException: Unable to add window  token android.os.BinderProxy@423280c8 is not valid; is your activity running?
        }
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;

        if (true)
            return;

        if (dialog != null) {
            WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
            wmlp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
            wmlp.y = calcVerticalForDialog(height);
            dialog.getWindow().setAttributes(wmlp);
        }
    }

    int offset = 266;
    int top = -1;

    protected final int calcVerticalForDialog(int screenHeight) {

        boolean showBelow = true;
        if (screenHeight - 100 - top < 350) {
            showBelow = false;
        }

        int result = 0;
        if (showBelow) {
            result = top + offset;
        } else {
            result = top + offset - 265;
        }
        return result;
    }

}
