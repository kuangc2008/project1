
package com.kc.threadsample;

import android.net.Uri;
import android.provider.BaseColumns;

public class MyDataProviderContract implements BaseColumns {

    public static final Uri PICTUREURL_TABLE_CONTENTURI = null;

    public static final String IMAGE_URL_COLUM = "imageUrl";
    public static final String IMAGE_PICTURENAME_COLUMN = "ImageName";

    public static final String IMAGE_THUMBURL_COLUM = "thumbUrl";
    public static final String IMAGE_THUMBNAME_COLUMN = "ThumbUrlName";

}
