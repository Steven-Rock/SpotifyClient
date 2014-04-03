package com.swr.spotifyclient.spotify.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Steve on 4/1/2014.
 */
public class FileUtil {

    public static final int BUFFER = 1024;

    //decodes image and scales it to reduce memory consumption
    public static final Bitmap decodeFile(File f) {
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 70;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 > REQUIRED_SIZE)
                    break;
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }

    public static void CopyInToOutStream(InputStream inStream, OutputStream outStream, String TAG) {

        try {
            byte[] bytes = new byte[BUFFER];

            boolean ok = true;
            int num;

            while (ok) {
                num = inStream.read(bytes, 0, BUFFER);
                if (num < 1) {
                    ok = false;
                } else {
                    outStream.write(bytes, 0, num);
                }
            }
        } catch (Exception e) {
            MyLog.e(TAG, "Exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
