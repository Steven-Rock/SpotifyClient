package com.swr.spotifyclient.spotify.util;

import android.graphics.Color;

/**
 * Created by Steve on 4/1/2014.
 */
public class C {

    public static final class env {
        public static final int prod = 1;
        public static final int stage = 2;
    }

    public static final class color {
        public static final int cyan_color = Color.argb(255, 17, 219, 237);
        public static final int white_color = Color.argb(255, 231, 236, 242);
        public static final int gray_color = Color.argb(255, 191, 185, 181);
        public static final int light_gray_color = Color.argb(255, 170, 170, 170);
        public static final int light2_gray_color = Color.argb(255, 95, 89, 85);

        public static final int dark_color = Color.argb(255, 42, 40, 42);
        public static final int more_dark_color = Color.argb(255, 33, 31, 33);
        public static final int black_color = Color.argb(255, 20, 19, 19);
        public static final int transparent_color = Color.argb(0, 0, 0, 0);
        public static final int yellow_color = Color.argb(255, 255, 255, 0);
        public static final int orange_color = Color.argb(255, 255, 125, 20);

    }

    public static final class url {

        // staging
        public static final String spotifyTopURL = "http://charts.spotify.com/api/charts/most_shared/us/latest";
        public static final String spotifyStreamedURL = "http://charts.spotify.com/api/charts/most_streamed/us/latest";

    }

}
