package com.swr.spotifyclient.spotify.task;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;

import com.swr.spotifyclient.spotify.AbstractActivity;
import com.swr.spotifyclient.spotify.model.SpotifyAbstractModel;
import com.swr.spotifyclient.spotify.util.HTTPUtil;
import com.swr.spotifyclient.spotify.util.MyLog;
import com.swr.spotifyclient.spotify.util.ProgressDialogRunner;
import com.swr.spotifyclient.spotify.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steve on 4/1/2014.
 */
public abstract class AbstrackTask  extends AsyncTask<Void, Void, Boolean> {

    protected final static String TAG = "SpotifyClient";

    final List data = new ArrayList();
    protected String jsonResponse = null;
    protected AbstractActivity activity = null;
    protected static int ERROR_MSG = 1;

    abstract protected SpotifyAbstractModel getItem(JSONObject json_data) throws JSONException;
    abstract protected boolean addItem(SpotifyAbstractModel item);
    abstract protected String getUrl();

    protected boolean processJSON(String json) throws JSONException {

        //MyLog.d(TAG, json);


        int count = 0;

        if(!Util.isEmpty(json)){

            JSONObject obj = new JSONObject(json);

            JSONArray jsonArr = obj.getJSONArray("tracks");


            MyLog.d(TAG, "JSON Length = " + jsonArr.length());
            for(int i=0;i<jsonArr.length();i++){

                JSONObject json_data = jsonArr.getJSONObject(i);

                Object item = getItem(json_data);
                data.add(item);

            }
        }
        MyLog.d(TAG, "JSON Num = " + count);


        if(activity != null && activity instanceof AbstractActivity){
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    ((AbstractActivity) activity).setMedia(data);
                }
            });
        }

        return true;
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        String url = getUrl();
        MyLog.d(TAG, "Starting: url = " + url);

        try {
            jsonResponse = HTTPUtil.readJSON(url, TAG);
            return processJSON(jsonResponse);
        } catch (JSONException e) {
            MyLog.e(TAG, e.toString());
            //displayGenericErrorMessage(ERROR_MSG);
            return true;
        }
        catch(Exception e){
            MyLog.e(TAG, e.toString());
            displayGenericErrorMessage(ERROR_MSG);
        }
        return false;
    }


    @Override
    protected void onPostExecute(final Boolean success) {

        if (success) {
            //finishActivity();
        } else if(activity != null){

            // TODO add more error messages
           // activity.errorMessage(activity.getString(R.string.error_incorrect_password));
        }
    }

    public AbstractActivity getActivity() { return activity; }

    public void setActivity(AbstractActivity activity) {
        this.activity = activity;
    }

    protected final void displayGenericErrorMessage(final int code){


        if(activity != null){

            activity.runOnUiThread(new Runnable() {
                public void run() {
                    if(activity instanceof ProgressDialogRunner){
                        ((ProgressDialogRunner)activity).dismissProgressDialog();
                    }
                    Toast.makeText(activity, Util.genericErrorMsg + " (" + code + ")", Toast.LENGTH_LONG).show();
                }
            });


        }
    }
}
