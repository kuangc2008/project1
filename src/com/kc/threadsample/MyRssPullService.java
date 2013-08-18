
package com.kc.threadsample;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;

import org.xmlpull.v1.XmlPullParserException;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class MyRssPullService extends IntentService {

    private String uri;
    private BroadcastNotifier mBroadcastNotifier = new BroadcastNotifier(this);

    public MyRssPullService() {
        super("MyRssPullService");
        Log.v("kc", "MyRssPullService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v("kc", "onHandleIntent");
        uri = intent.getDataString();

        try {
            URL url = new URL(uri);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setConnectTimeout(30 * 1000);
            mBroadcastNotifier.broadcastIntentWithState(Constants.STATE_ACTION_STARTED);
            httpConnection.setRequestProperty("User-Agent", Constants.USER_AGENT);

            mBroadcastNotifier.broadcastIntentWithState(Constants.STATE_ACTION_CONNECTING);
            if (httpConnection.getResponseCode() == 200) {
                long lastModifiedDate = httpConnection.getLastModified();
                Date date = new Date(lastModifiedDate);
                Log.v("kc", "MyRssPullService lastmodiedDate-->" + date);
                mBroadcastNotifier.broadcastIntentWithState(Constants.STATE_ACTION_PARSING);
                InputStream inputStream = httpConnection.getInputStream();
                MyRssPullParser localPicasaPullParser = new MyRssPullParser();
                localPicasaPullParser.parseXml(inputStream, mBroadcastNotifier);

                mBroadcastNotifier.broadcastIntentWithState(Constants.STATE_ACTION_WRITING);

            }
            mBroadcastNotifier.broadcastIntentWithState(Constants.STATE_ACTION_COMPLETE);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

    }
}
