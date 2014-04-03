package com.swr.spotifyclient.spotify.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Steve on 4/1/2014.
 */
public class QuickViewImageLoader {

    protected int offset = 0;
    protected float scale = 0;
    protected int newWidth = 0;
    protected int newHeight = 0;
    protected int maxOffset = 0;
    protected int percentOffset = 0;
    protected float xPos = 0;
    protected float density = 0;
    protected float dpHeight = 0;
    protected float dpWidth  = 0;

    protected ImageView iv1 = null;
    protected ImageView iv2 = null;
    protected ProgressBar pb = null;

    protected ImageView bottomStrip = null;
    protected View bottomStrip2 = null;
    protected FrameLayout bottomStrip3 = null;


    protected final static String TAG = "Krowds";

    protected boolean isLocal = false;
    protected Bitmap bitmap = null;

    protected FileCache fileCache = null;
    protected ExecutorService executorService = null;
    protected MemoryCache memoryCache=new MemoryCache();

    protected Map<ImageView, String> imageViews= Collections.synchronizedMap(new WeakHashMap<ImageView, String>());


    public void clear(){
        offset = 0;
        scale = 0;
        newWidth = 0;
        maxOffset = 0;
        percentOffset = 0;
        xPos = 0;
        density = 0;
        dpHeight = 0;
        dpWidth  = 0;
        iv1 = null;
        iv2 = null;
        bottomStrip = null;

    }

    protected void calcScale(){
        if(bitmap == null) return;
        scale = bitmap.getWidth()/bitmap.getHeight();
    }

    public void setImages(){
        setImage(iv1);
        setImage(iv2);
    }
    protected void setImage(ImageView iv){

        if(iv == null) return;
        if(bitmap == null){
            removeImages();
            return;
        }

        iv.setImageBitmap(bitmap);
        iv.setBackgroundColor(C.color.black_color);
        UIUtil.setVisible(iv);
    }

    public void removeImage(ImageView iv){
        if(iv == null) return;
        iv.setImageDrawable(null);
        iv.setBackgroundColor(C.color.transparent_color);
        UIUtil.setInvisible(iv);
    }

    public void removeImages(){
        removeImage(iv1);
        removeImage(iv2);
    }

    public void displayImages(String url)
    {

        MyLog.d(TAG, "Starting: url = " + url);

        if(Util.isEmpty(url) || iv1 == null || iv2 == null) return;

        imageViews.put(iv1, url);
        imageViews.put(iv2, url);

        bitmap=memoryCache.get(url);
        if(bitmap!=null){
            calcScale();
            setImages();
            fixImageStripHeight();
        }
        else{ queuePhoto(url); }
    }

    public QuickViewImageLoader(Context context){
        fileCache=new FileCache(context);
        executorService= Executors.newFixedThreadPool(5);
    }


    protected void queuePhoto(String url)
    {
        PhotoToLoad p=new PhotoToLoad(url, iv1, iv2);
        executorService.submit(new PhotosLoader(p));
    }

    protected boolean imageViewReused(PhotoToLoad photoToLoad){

        int goodCount = 0;

        if(photoToLoad == null) return false;
        if(imageViews == null && imageViews.size() == 0) return false;

        ImageView iv = null;
        String tag = null;

        if(imageViews.size() > 0){

            iv = iv1;
            tag =  imageViews.get(iv);
            if(tag==null || !tag.equals(photoToLoad.url)){
                goodCount++;
            }
        }

        if(imageViews.size() > 1){

            iv = iv1;
            tag =  imageViews.get(iv);
            if(tag==null || !tag.equals(photoToLoad.url)){
                goodCount++;
            }
        }

        if(goodCount == 2) return true;
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
        bitmap = null;

        // from video
        // from video
        if(isLocal){

            bitmap = BitmapFactory.decodeFile(url);
            //createImageThumbnail(url, MediaStore.Images.Thumbnails.MICRO_KIND);
            if(bitmap == null){
                bitmap = ThumbnailUtils.createVideoThumbnail(url, MediaStore.Video.Thumbnails.MICRO_KIND);
            }
            if(bitmap!=null) {
                //MyLog.d(TAG, "isVideo: " + isVideo);
                return bitmap;
            }
        }

        //from SD cache
        File f=fileCache.getFile(url);
        bitmap = FileUtil.decodeFile(f);
        if(bitmap!=null) { calcScale(); return bitmap; }

        //from web
        bitmap = HTTPUtil.getVideoImage(url, f);
        calcScale();
        return bitmap;
    }

    protected BitmapDisplayer2 getBitmapDisplayer(PhotoToLoad p){
        return new BitmapDisplayer2(p);
    }

    //Task for the queue
    protected class PhotoToLoad
    {
        public String url;
        protected ImageView iv1 = null;
        protected ImageView iv2 = null;
        public PhotoToLoad(String u, ImageView iv1, ImageView iv2){
            url=u;
            this.iv1=iv1;
            this.iv2=iv2;
        }

        @Override
        public String toString() {
            return "PhotoToLoad{" +
                    "url='" + url + '\'' +
                    '}';
        }

        public Context getContext(){

            if(iv1 == null) return null;
            else return iv1.getContext();
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public ImageView getIv1() {
            return iv1;
        }

        public void setIv1(ImageView iv1) {
            this.iv1 = iv1;
        }

        public ImageView getIv2() {
            return iv2;
        }

        public void setIv2(ImageView iv2) {
            this.iv2 = iv2;
        }
    }


    public ProgressBar getPb() {
        return pb;
    }

    public void setPb(ProgressBar pb) {
        this.pb = pb;
    }



    protected void fixImageStripHeight(){

        int oldHeight = 0;
        float height = 0;
        boolean adjustHeight = false;

        if(bottomStrip != null && scale != 0 ){

            oldHeight = bottomStrip.getLayoutParams().height;
            height = bottomStrip.getWidth()/scale;

            //MyLog.d(TAG, "***oldHeight = " + oldHeight + ": new Height = " + height);

            if(Math.round(Math.abs(oldHeight - height)) > 10){
                adjustHeight = true;
            }
        }

        if(bottomStrip != null && adjustHeight){
            MyLog.d(TAG, "Animating height " + oldHeight + ": new Height = " + height);
            bottomStrip.getLayoutParams().height = Math.round(height);
            bottomStrip.requestLayout();
            bottomStrip.invalidate();
        }

        if(bottomStrip2 != null && adjustHeight){
            bottomStrip2.getLayoutParams().height = Math.round(height);
            bottomStrip2.requestLayout();
            bottomStrip2.invalidate();
        }

        if(bottomStrip3 != null && adjustHeight ){
            bottomStrip3.getLayoutParams().height = Math.round(height + 15*density);
            bottomStrip3.requestLayout();
            bottomStrip3.invalidate();
        }

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
                // MyLog.d(TAG, "imageViewReused: " + photoToLoad.toString());
                return;
            }

            iv1.setVisibility(View.VISIBLE);
            iv2.setVisibility(View.VISIBLE);

            bitmap = getBitmap(photoToLoad.url);
            calcScale();
            // MyLog.d(TAG, "PhotosLoader: Bitmap = " + bmp);
            memoryCache.put(photoToLoad.url, bitmap);

            if(imageViewReused(photoToLoad))
                return;

            BitmapDisplayer2 bd= getBitmapDisplayer(photoToLoad);
            Context ctx = photoToLoad.getContext();
            if(ctx != null && ctx instanceof Activity){
                Activity a=(Activity)ctx;
                a.runOnUiThread(bd);
            }
        }
    }

    protected final int pixelToDP(int pix){
        return UIUtil.pixelToDP(pix, density);
    }

    protected final int dpToPixel(int dp){
        return UIUtil.dpToPixel(dp, density);
    }

    //Used to display bitmap in the UI thread
    protected class BitmapDisplayer2 implements Runnable
    {

        PhotoToLoad photoToLoad;

        public BitmapDisplayer2(PhotoToLoad p){
            photoToLoad=p;
            calcScale();
            calcWidthHeight();
            iv1.setVisibility(View.VISIBLE);
            iv2.setVisibility(View.VISIBLE);
        }

        protected void calcScale(){
            if(bitmap == null) return;
            scale = bitmap.getWidth()/bitmap.getHeight();
            //MyLog.d(TAG, "Scale = " + scale);

        }

        protected void calcWidthHeight(){

            //double f = dpWidth*2.04*density;
            newHeight = dpToPixel(75); //Math.round(newWidth/scale);
            newWidth = Math.round(newHeight*scale); // (int)Math.round(f)
            //MyLog.d(TAG, "New width = " + newWidth + ": new Height = " + newHeight);
        }

        public void run()
        {
            MyLog.d(TAG, "BitmapDisplayer2: Starting ");

            if(photoToLoad == null) return;

            if(iv1 == null || iv2 == null) return;
            pb.setVisibility(View.INVISIBLE);
            iv1.setVisibility(View.VISIBLE);
            iv2.setVisibility(View.VISIBLE);

            calcScale();
            calcWidthHeight();

            if(imageViewReused(photoToLoad)) return;
            if(bitmap == null) return;

            bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);

            MyLog.d(TAG, "2 Setting bitmsap: scale = " + scale + ": dp width = " + dpWidth);
            setImages();
            videoStripCalculations();
            largeVideoStringCalculations();
            largeVideoCalc();
            fixImageStripHeight();
        }
    }



    protected void videoStripCalculations(){

        if(iv1 == null) return;

        //MyLog.d(TAG, "Starting" );

        maxOffset = Math.round(newWidth - dpWidth);
        offset = (int)Math.round(xPos*2.0);
        if(offset < 0 ) offset = 0;
        MyLog.d(TAG, "Offset = " + -Math.round(xPos) + ":" + offset + ":" + percentOffset + ":" + xPos);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(newWidth, newHeight);
        lp.setMargins(-offset, 2, 0, 2);

        iv1.requestLayout();
        iv1.invalidate();

    }

    protected void largeVideoStringCalculations(){

        if(iv2 == null) return;

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(newWidth, newHeight);
        lp.setMargins(-offset, 2, 0, 2);

        iv2.setLayoutParams(lp);

        iv2.requestLayout();
        iv2.invalidate();

    }

    public void largeVideoCalc(){

        if(iv2 == null) return;

        float currentX = iv2.getX();
        float currentY = iv2.getY();

        percentOffset = 100 - Math.round((dpWidth - (xPos/density))*100/dpWidth);
        offset = Math.round(maxOffset * percentOffset/100);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(newWidth, newHeight);

        offset = (int)Math.round(.96*newWidth*percentOffset/100 );
        if(offset < 0 ) offset = 0;
        MyLog.d(TAG, "Neg xPos = " + -Math.round(xPos) + ": off " + offset + ": %off " + percentOffset + ": xPos " + xPos + ": newWidth " + newWidth + ": density " + density + ": density " + density);

        lp.setMargins(Math.round(-offset), 0, 0, 0);
        iv2.setLayoutParams(lp);


        iv2.invalidate();
        iv2.requestLayout();


    }

    public ImageView getIv1() {
        return iv1;
    }

    public void setIv1(ImageView iv1) {
        this.iv1 = iv1;
    }

    public ImageView getIv2() {
        return iv2;
    }

    public void setIv2(ImageView iv2) {
        this.iv2 = iv2;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public float getDpHeight() {
        return dpHeight;
    }

    public void setDpHeight(float dpHeight) {
        this.dpHeight = dpHeight;
    }

    public float getDpWidth() {
        return dpWidth;
    }

    public void setDpWidth(float dpWidth) {
        this.dpWidth = dpWidth;
    }

    public float getxPos() {
        return xPos;
    }

    public void setxPos(float xPos) {
        this.xPos = xPos;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public int getNewWidth() {
        return newWidth;
    }

    public void setNewWidth(int newWidth) {
        this.newWidth = newWidth;
    }

    public int getMaxOffset() {
        return maxOffset;
    }

    public void setMaxOffset(int maxOffset) {
        this.maxOffset = maxOffset;
    }

    public int getPercentOffset() {
        return percentOffset;
    }

    public void setPercentOffset(int percentOffset) {
        this.percentOffset = percentOffset;
    }

    public ImageView getBottomStrip() {
        return bottomStrip;
    }

    public void setBottomStrip(ImageView bottomStrip) {
        this.bottomStrip = bottomStrip;
    }

    public View getBottomStrip2() {
        return bottomStrip2;
    }

    public void setBottomStrip2(View bottomStrip2) {
        this.bottomStrip2 = bottomStrip2;
    }

    public FrameLayout getBottomStrip3() {
        return bottomStrip3;
    }

    public void setBottomStrip3(FrameLayout bottomStrip3) {
        this.bottomStrip3 = bottomStrip3;
    }
}

