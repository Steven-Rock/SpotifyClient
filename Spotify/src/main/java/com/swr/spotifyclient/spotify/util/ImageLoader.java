package com.swr.spotifyclient.spotify.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Steve on 4/1/2014.
 */
public class ImageLoader {
    protected final static String TAG = "SpotifyClient";

    protected boolean isLocal = false;
    protected FileCache fileCache = null;
    protected ExecutorService executorService = null;
    protected MemoryCache memoryCache=new MemoryCache();

    protected Map<ImageView, String> imageViews= Collections.synchronizedMap(new WeakHashMap<ImageView, String>());


    public ImageLoader(Context context){
        fileCache=new FileCache(context);
        executorService= Executors.newFixedThreadPool(5);
    }

    //final int stub_id = R.drawable.no_image;

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        MyLog.d(TAG, "in sample size = " + inSampleSize);
        return inSampleSize;
    }


    public void DisplayLocalImage(String url, ImageView imageView, Activity act)
    {

        if(Util.isEmpty(url) || imageView == null || act == null) return;

        MyLog.d(TAG, "Starting: url = " + url);
        Uri uri = Uri.parse(url);
        try {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            int imageHeight = options.outHeight;
            int imageWidth = options.outWidth;
            String imageType = options.outMimeType;

            Bitmap bitmap = BitmapFactory.decodeStream(act.getContentResolver().openInputStream(uri), null, options );
            options.inSampleSize = 4; //calculateInSampleSize(options, imageWidth, imageHeight);
            options.inJustDecodeBounds = false;

            bitmap = BitmapFactory.decodeStream(act.getContentResolver().openInputStream(uri), null, options );
            imageView.setImageBitmap(bitmap);

        }
        catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            MyLog.e(TAG, "DisplayLocalImage: " + e.toString());
            e.printStackTrace();
        }

    }

    public void DisplayImage(String url, ImageView imageView, boolean isLocal)
    {

        if(Util.isEmpty(url) || imageView == null) return;

        //MyLog.d(TAG, "Starting: url = " + url);
        List<ImageView> list = new ArrayList<ImageView>();
        list.add(imageView);

        DisplayImages(url, list, isLocal);
    }

    public void DisplayImages(String url, List<ImageView> ivs, boolean isLocal)
    {

        //MyLog.d(TAG, "Starting: url = " + url);

        this.isLocal = isLocal;

        if(Util.isEmpty(url) || Util.isEmpty(ivs)) return;
        else{
            for (ImageView iv : ivs){
                imageViews.put(iv, url);
            }
        }

        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null){
            for (ImageView iv : ivs){
                iv.setImageBitmap(bitmap);
            }

        }
        else{ queuePhoto(url, ivs); }
    }


    protected void queuePhoto(String url, List<ImageView> ivs)
    {
        PhotoToLoad p=new PhotoToLoad(url, ivs);
        executorService.submit(new PhotosLoader(p));
    }

    protected boolean imageViewReused(PhotoToLoad photoToLoad){

        int goodCount = 0;

        if(photoToLoad == null) return false;

        List<ImageView> ivs = photoToLoad.getImageViews();
        if(!Util.isEmpty(ivs)){

            for (ImageView iv : ivs){

                String tag=imageViews.get(iv);

                if(tag==null || !tag.equals(photoToLoad.url)){
                    goodCount++;
                }
            }

            if(goodCount == ivs.size()) return true;
            return false;

        }

        return false;

    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }

    /**
     * Main function to retrieve a new Bitmap from local video, local file, or HTTP  connection
     * @param url - path to the file
     * @return generated Bitmap
     */
    protected Bitmap getBitmap(String url)
    {
        Bitmap b = null;
        // MyLog.d(TAG, "Starting");

        // from video
        if(isLocal){

            b = BitmapFactory.decodeFile(url);
            //createImageThumbnail(url, MediaStore.Images.Thumbnails.MICRO_KIND);
            if(b == null){
                b = ThumbnailUtils.createVideoThumbnail(url, MediaStore.Video.Thumbnails.MICRO_KIND);
            }
            if(b!=null) {
                //MyLog.d(TAG, "isVideo: " + isVideo);
                return b;
            }
        }

        //from SD cache
        File f=fileCache.getFile(url);
        b = FileUtil.decodeFile(f);
        if(b!=null) { return b; }

        //from web
        //MyLog.d(TAG, "Getting file from server");
        b = HTTPUtil.getVideoImage(url, f);
        return b;
    }


    //Task for the queue
    protected class PhotoToLoad
    {
        public String url;
        public List<ImageView> ivs;
        public PhotoToLoad(String u, List<ImageView> ivs){
            url=u;
            this.ivs=ivs;
        }

        public List<ImageView> getImageViews(){
            return ivs;
        }

        @Override
        public String toString() {
            return "PhotoToLoad{" +
                    "url='" + url + '\'' +
                    '}';
        }

        public Context getContext(){

            if(Util.isEmpty(ivs)) return null;
            else return ivs.get(0).getContext();
        }
    }

    protected BitmapDisplayer getBitmapDisplayer(Bitmap b, PhotoToLoad p){

        return new BitmapDisplayer(b, p);

    }
    protected class PhotosLoader implements Runnable {

        PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad=photoToLoad;
        }

        @Override
        public void run() {

            //MyLog.d(TAG, "PhotosLoader: Startign");
            if(imageViewReused(photoToLoad)){
                //MyLog.d(TAG, "imageViewReused: " + photoToLoad.toString());
                return;
            }

            Bitmap bmp=getBitmap(photoToLoad.url);
            // MyLog.d(TAG, "PhotosLoader: Bitmap = " + bmp);
            memoryCache.put(photoToLoad.url, bmp);

            if(imageViewReused(photoToLoad))
                return;

            //MyLog.d(TAG, "PhotosLoader: Bitmap displayer");
            BitmapDisplayer bd= getBitmapDisplayer(bmp, photoToLoad);
            Context ctx = photoToLoad.getContext();
            if(ctx != null && ctx instanceof Activity){
                Activity a=(Activity)ctx;
                a.runOnUiThread(bd);
            }
        }
    }


    //Used to display bitmap in the UI thread
    protected class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){
            bitmap=b;
            photoToLoad=p;
        }
        public void run()
        {
            //MyLog.d(TAG, "BitmapDisplayer: Starting ");

            if(imageViewReused(photoToLoad))
                return;

            //MyLog.d(TAG, "BitmapDisplayer: ...");
            if(bitmap == null) return;
            //MyLog.d(TAG, "BitmapDisplayer: ...");

            List<ImageView> ivs = photoToLoad.getImageViews();
            //MyLog.d(TAG, "BitmapDisplayer: ...");
            if(Util.isEmpty(ivs)) return;


            // MyLog.d(TAG, "BitmapDisplayer: ...");
            for (ImageView iv : ivs){

                if(iv != null){
                    // MyLog.d(TAG, "BitmapDisplayer: Setting bitmap: " + bitmap + ": view " + iv);
                    //if(iv instanceof TouchImageView){

                    iv.setImageBitmap(bitmap);
                    iv.invalidate();
                }
            }

            // MyLog.d(TAG, "BitmapDisplayer: Ending");
        }
    }

}

