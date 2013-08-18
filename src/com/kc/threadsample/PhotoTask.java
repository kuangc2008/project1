
package com.kc.threadsample;

import java.lang.ref.WeakReference;
import java.net.URL;

public class PhotoTask {

    private static MyPhotoManager sPhotoManager;
    private Runnable mDownloadRunnable;
    private Runnable mDecodeRunnable;
    private URL mImageURL;
    private WeakReference<PhotoView> mImageWeakRef;
    private boolean mCacheEnabled;
    private int mTargetWidth;
    private int mTargetHeight;

    PhotoTask() {
        mDownloadRunnable = null;

        sPhotoManager = MyPhotoManager.getInstance();
    }

    public void initializeDownloaderTask(MyPhotoManager photoManager, PhotoView photoView,
            boolean cacheFalg) {
        sPhotoManager = photoManager;
        mImageURL = photoView.getLocation();
        mImageWeakRef = new WeakReference<PhotoView>(photoView);
        mCacheEnabled = cacheFalg;
        mTargetWidth = photoView.getWidth();
        mTargetHeight = photoView.getHeight();
    }

    public boolean isCacheEnable() {
        return mCacheEnabled;
    }

    public URL getImageURL() {
        return mImageURL;
    }

    public PhotoView getPhotoView() {
        if (mImageWeakRef != null) {
            return mImageWeakRef.get();
        }
        return null;
    }

    byte[] mImageBuffer;

    public void setByteBuffer(byte[] imageBuffer) {
        mImageBuffer = imageBuffer;
    }

    public byte[] getByteBuffer() {
        return mImageBuffer;
    }

    Runnable getHTTPDownloadRunnable() {
        return mDownloadRunnable;
    }

    Runnable getPhotoDecodeRunnable() {
        return mDecodeRunnable;
    }
}
