
package com.kc.threadsample;

import java.util.Locale;

import android.support.v4.content.LocalBroadcastManager;

public class Constants {

    public static final String THUMBNAIL_FRAGMENT_TAG = "com.kc.threadsample.THUMBNAIL_FRAGMENT_TAG";

    public static int WIDTH_PIXELS;

    public static int HEIGHT_PIXELS;

    public static final String PICASA_RSS_URL = "http://picasaweb.google.com/data/feed/base/featured?"
            + "alt=rss&kind=photo&access=public&slabel=featured&hl=en_US&imgmax=1600";

    public static final String BROADCAST_ACTION = "com.example.android.threadsample.BROADCAST";
    public static final String EXTENDED_DATA_STATUS = "com.example.android.threadsample.STATUS";
    public static final String EXTENDED_STATUS_LOG = "com.example.android.threadsample.LOG";

    public static final String USER_AGENT = "Mozilla/5.0 (Linux; U; Android "
            + android.os.Build.VERSION.RELEASE + ";" + Locale.getDefault().toString() + "; "
            + android.os.Build.DEVICE + "/" + android.os.Build.ID + ")";

    public static final int STATE_ACTION_STARTED = 0;
    public static final int STATE_ACTION_CONNECTING = 1;
    public static final int STATE_ACTION_PARSING = 2;
    public static final int STATE_ACTION_WRITING = 3;
    public static final int STATE_ACTION_COMPLETE = 4;
}
