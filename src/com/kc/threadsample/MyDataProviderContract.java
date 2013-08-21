
package com.kc.threadsample;

import android.net.Uri;
import android.provider.BaseColumns;

public class MyDataProviderContract implements BaseColumns {
    
    private MyDataProviderContract() {}

    public static final Uri PICTUREURL_TABLE_CONTENTURI = null;

    public static final String ROW_ID = BaseColumns._ID;
    public static final String IMAGE_URL_COLUM = "imageUrl";
    public static final String IMAGE_PICTURENAME_COLUMN = "ImageName";

    public static final String IMAGE_THUMBURL_COLUM = "thumbUrl";
    public static final String IMAGE_THUMBNAME_COLUMN = "ThumbUrlName";


    public static final String SCHEME = "content";
    public static final String AUTHORITY = "com.kc.threadsample";
    public static final Uri CONTENT_URI = Uri.parse(SCHEME + "://" + AUTHORITY);

    
    
    
    public static final String DATABASE_NAME = "pictureDataDB";
    public static final int DATABASE_VERSION = 1;
    
    public static final String PICTUREURL_TABLE_NAME = "PictureUrlData";
    public static final String DATE_TABLE_NAME = "DateMetadatData";
    public static final String DATE_DATE_COLUM = "downloadDate";
}
