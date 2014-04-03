package com.swr.spotifyclient.spotify.util;

import android.app.Activity;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;

import com.swr.spotifyclient.spotify.R;

/**
 * Created by Steve on 4/1/2014.
 */
public class UIUtil {

    public static final int pixelToDP(int pix, double density){
        double d = pix/density + .5f;
        return (int)Math.round(d);
    }

    public static final int dpToPixel(int dp, double density){
        double d = dp*density + .5f;
        return (int)Math.round(d);
    }

    public static final void setVisible(View view){
        if(view != null && view.getVisibility() != View.VISIBLE) view.setVisibility(View.VISIBLE);
    }
    public static final void setInvisible(View view){
        if(view != null && view.getVisibility() != View.INVISIBLE) view.setVisibility(View.INVISIBLE);
    }
    public static final void setGone(View view){
        if(view != null && view.getVisibility() != View.GONE) view.setVisibility(View.GONE);
    }

    public final static int getWindowWidth(Activity act){
        int[] dim = getWindowSize(act);
        return dim[0];
    }

    public final static int getWindowHeight(Activity act){
        int[] dim = getWindowSize(act);
        return dim[1];
    }

    public final static int[] getWindowSize(Activity act){

        int[] dim = new int[2];
        Display display = act.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        dim[0] = width;
        dim[1] = height;

        return dim;
    }
    public final static  Integer [] initImageIds(int num){

        Integer [] imageIds = null;
        if(num > 0){
            imageIds = new Integer[num];

            for (int i = 0; i < num; i++) {
                imageIds[i] = R.drawable.library_placeholder;
            }
        }
        return imageIds;
    }


    public final static ImageView findImageView(int viewId, View rootView){

        View v = findView(viewId, rootView);
        if(v == null) return null;

        if(v instanceof ImageView){
            return (ImageView)v;
        }
        return null;
    }

    public final static View findView(int viewId, View rootView){

        if(rootView == null || viewId <= 0) return null;
        try{
            View v = rootView.findViewById(viewId);
            return v;
        }
        catch(Exception e) {
            return null;
        }
    }
}
