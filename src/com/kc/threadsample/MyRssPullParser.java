
package com.kc.threadsample;

import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;
import java.util.Vector;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.util.Log;

public class MyRssPullParser {

    private Vector<ContentValues> mImages;
    private static ContentValues mImage;

    private static final int VECTOR_INITAL_SIZE = 500;
    private static final String ITEM = "item";
    private static final String CONTENT = "media:content";
    private static final String THUMBNAIL = "media:thumbnail";

    public void parseXml(InputStream inputStream, BroadcastNotifier progressNotifier)
            throws XmlPullParserException, IOException {

        XmlPullParserFactory localXMlParserFactory = XmlPullParserFactory.newInstance();

        localXMlParserFactory.setNamespaceAware(false);

        XmlPullParser localXmlPullParser = localXMlParserFactory.newPullParser();

        localXmlPullParser.setInput(inputStream, null);

        int eventType = localXmlPullParser.getEventType();

        int imageCount = 1;

        if (eventType != XmlPullParser.START_DOCUMENT) {
            throw new XmlPullParserException("Invalid RSS");
        }

        mImages = new Vector<ContentValues>(VECTOR_INITAL_SIZE);

        while (true) {
            int nextEvent = localXmlPullParser.next();

            if (Thread.currentThread().isInterrupted()) {
                throw new XmlPullParserException("Cancled");
            } else if (nextEvent == XmlPullParser.END_DOCUMENT) {
                break;
            } else if (nextEvent == XmlPullParser.START_DOCUMENT) {
                continue;
            } else if (nextEvent == XmlPullParser.START_TAG) {
                String eventName = localXmlPullParser.getName();
                if (eventName.equalsIgnoreCase(ITEM)) {
                    mImage = new ContentValues();
                } else {
                    String imageUrlKey;
                    String imageNameKey;
                    String fileName;

                    if (eventName.equalsIgnoreCase(CONTENT)) {
                        imageUrlKey = MyDataProviderContract.IMAGE_URL_COLUM;
                        imageNameKey = MyDataProviderContract.IMAGE_PICTURENAME_COLUMN;
                    } else if (eventName.equalsIgnoreCase(THUMBNAIL)) {
                        imageUrlKey = MyDataProviderContract.IMAGE_THUMBURL_COLUM;
                        imageNameKey = MyDataProviderContract.IMAGE_THUMBNAME_COLUMN;
                    } else {
                        continue;
                    }

                    String urlValue = localXmlPullParser.getAttributeValue(null, "url");
                    if (urlValue == null) {
                        break;
                    }
                    mImage.put(imageUrlKey, urlValue);
                    fileName = Uri.parse(urlValue).getLastPathSegment();
                    mImage.put(imageNameKey, fileName);
                }
            } else if (nextEvent == XmlPullParser.END_TAG
                    && localXmlPullParser.getName().equalsIgnoreCase(ITEM) && (mImage != null)) {
                mImages.add(mImage);
                progressNotifier.notifyProgress("Parsed Image[" + imageCount + "]:"
                        + mImage.getAsString(MyDataProviderContract.IMAGE_URL_COLUM));
                mImage = null;
                imageCount++;
            }
        }
    }

    public Vector<ContentValues> getImages() {
        return mImages;
    }

}
