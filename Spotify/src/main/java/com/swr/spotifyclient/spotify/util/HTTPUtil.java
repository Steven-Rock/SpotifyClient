package com.swr.spotifyclient.spotify.util;

import android.graphics.Bitmap;
import android.net.http.AndroidHttpClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Steve on 4/1/2014.
 */
public class HTTPUtil {

    protected final static String TAG = "SpotifyClient";

    public final static synchronized Bitmap getVideoImage(String url, File f){

        //MyLog.d(TAG, "Starting: getVideoImage: url = " + url);

        if(Util.isEmpty(url) || f == null) return null;

        try {

            Bitmap bitmap = null;
            URL imageUrl = new URL(url);

            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);

            InputStream is=conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            FileUtil.CopyInToOutStream(is, os,TAG);
            os.close();

            bitmap = FileUtil.decodeFile(f);

            //bitmap = getRoundedCornerBitmap(bitmap);
            return bitmap;

        } catch (Exception ex){
            ex.printStackTrace();
            MyLog.e(TAG, "Err: " + ex.toString());
        }
        return null;
    }


    /**
     * Parse any JSON reponse to a server
     *
     * @param url
     *            - URL to call
     * @return - The JSON String
     */
    public static String readJSON(String url, String TAG) throws Exception {
        return readJSONWithParams(url, TAG, null);
    }

    /**
            * Parse any JSON reponse to a server
    *
            * @param url
    *            - URL to call
    * @return - The JSON String
    */
    public static String readJSONWithParams(String url, String TAG, ArrayList<NameValuePair> parameters) throws Exception{

        MyLog.i(TAG, "Starting: url = " + url);
        MyLog.i(TAG, "Params: " + parameters);

        return readJSONWithParamsPost(url, TAG, parameters);

    }


    public static String readJSONWithParamsGet(String url, String TAG, ArrayList<NameValuePair> parameters) throws Exception {


        StringBuilder stringBuilder = new StringBuilder();
        HttpGet httpGet = new HttpGet(url);
        BufferedReader reader = null;
        InputStream content;

        HttpClient client = new DefaultHttpClient();

        HttpParams params = client.getParams();
        //MyLog.i(TAG, "User Agent = " +  params.getParameter(CoreProtocolPNames.USER_AGENT));
        // client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
        // client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
        //client.getParams().setRedirecting(client.getParams(), true);

        try {


            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200 || statusCode == 302 ) {

                HttpEntity entity = response.getEntity();
                content = entity.getContent();
                reader = new BufferedReader(new InputStreamReader(content));

                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } else {
                MyLog.e(TAG, "HTTPGet Failed, statusCode: " + statusCode);
            }
        } catch (ClientProtocolException e) {
            MyLog.e(TAG, "ClientProtocolException: " + e.toString());
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            MyLog.e(TAG, "IO: " + e.toString());
            e.printStackTrace();
            throw e;
        }
        catch (Exception e) {
            MyLog.e(TAG, "Exception: " + e.toString());
            e.printStackTrace();
            throw e;
        }
        catch (Throwable e) {
            MyLog.e(TAG, "Throwable: " + e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        finally{

            if(reader != null){

                try{ reader.close(); }
                catch(IOException e){
                    MyLog.e(TAG, "IO2: " + e.toString());
                    e.printStackTrace();
                }
            }

            if(client != null){
                //client.close();
            }
        }

        return stringBuilder.toString();
    }

    public static String readJSONWithParamsPost(String url, String TAG, ArrayList<NameValuePair> parameters)  throws Exception{

        MyLog.i(TAG, "Starting: url = " + url);
        MyLog.i(TAG, "Params: " + parameters);

        StringBuilder stringBuilder = new StringBuilder();
        HttpPost httpPost = new HttpPost(url);
        BufferedReader reader = null;
        InputStream content;


        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);



        //AndroidHttpClient client = AndroidHttpClient.newInstance("Android-" + Util.version);

        HttpParams params = client.getParams();
        //MyLog.i(TAG, "User Agent = " +  params.getParameter(CoreProtocolPNames.USER_AGENT));
        client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
        client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
        //client.getParams().setRedirecting(client.getParams(), true);

        try {

            if (parameters != null) {
                httpPost.setEntity(new UrlEncodedFormEntity(parameters));
            }

            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200 || statusCode == 302 ) {

                HttpEntity entity = response.getEntity();
                content = entity.getContent();
                reader = new BufferedReader(new InputStreamReader(content));

                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
            } else {
                MyLog.e(TAG, "HTTPPost Failed, statusCode: " + statusCode + " url: " + url);
            }
        } catch (ClientProtocolException e) {
            MyLog.e(TAG, "ClientProtocolException: " + e.toString());
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            MyLog.e(TAG, "IO: " + e.toString());
            e.printStackTrace();
            throw e;
        }
        catch (Exception e) {
            MyLog.e(TAG, "Exception: " + e.toString());
            e.printStackTrace();
            throw e;
        }
        catch (Throwable e) {
            MyLog.e(TAG, "Throwable: " + e.toString());
            e.printStackTrace();
            throw new Exception(e);
        }
        finally{

            if(reader != null){

                try{ reader.close(); }
                catch(IOException e){
                    MyLog.e(TAG, "IO2: " + e.toString());
                    e.printStackTrace();
                }
            }

            if(client != null){
              //  client.close();
            }
        }

        return stringBuilder.toString();
    }


}
