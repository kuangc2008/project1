
package com.kc.threadsample;

import java.net.URL;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.view.inputmethod.InputMethod;

public class MyPhotoManager {
    public static final int DOWNLOAD_FAILED = -1;
    public static final int DOWNLOAD_STARTED = 1;
    public static final int DOWNLOAD_COMPLETE = 2;
    public static final int DECODE_STARTED = 3;
    public static final int TASK_COMPLETE = 4;

    private static final int IAMGE_CACHE_SIZE = 1024 * 1024 * 4;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    private static MyPhotoManager sInstance = null;

    static {
        sInstance = new MyPhotoManager();
    }

    private final Queue<PhotoTask> mPhotoTaskWorkQueue;
    private final LruCache<URL, byte[]> mPhotoCache;
    private final ThreadPoolExecutor mDownloadThreadPool;
    private final ThreadPoolExecutor mDecodeThreadPool;
    private static final int CORE_POOL_SIZE = 8;
    private static final int MAXINUM_POOL_SIZE = 8;
    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private static final int KEEP_ALIVE_TIME = 1;

    private final BlockingQueue<Runnable> mDownloadWorkQueue;
    private final BlockingQueue<Runnable> mDecodeWorkQueue;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(android.os.Message msg) {
            PhotoTask photoTask = (PhotoTask) msg.obj;
            PhotoView localView = photoTask.getPhotoView();
            if (localView != null) {

            }
        }
    };

    private MyPhotoManager() {
        mPhotoTaskWorkQueue = new LinkedBlockingQueue<PhotoTask>();
        mPhotoCache = new LruCache<URL, byte[]>(IAMGE_CACHE_SIZE) {
            @Override
            protected int sizeOf(URL key, byte[] value) {
                return value.length;
            }
        };
        mDownloadWorkQueue = new LinkedBlockingQueue<Runnable>();
        mDecodeWorkQueue = new LinkedBlockingQueue<Runnable>();
        mDecodeThreadPool = new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES,
                KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, mDecodeWorkQueue);
        mDownloadThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXINUM_POOL_SIZE,
                KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, mDownloadWorkQueue);
    }

    public static MyPhotoManager getInstance() {
        return sInstance;
    }

    public static PhotoTask startDownload(PhotoView imageView, boolean cacheFalg) {
        PhotoTask downloadTask = sInstance.mPhotoTaskWorkQueue.poll();
        if (downloadTask == null) {
            downloadTask = new PhotoTask();
        }
        downloadTask.initializeDownloaderTask(MyPhotoManager.sInstance, imageView, cacheFalg);
        downloadTask.setByteBuffer(sInstance.mPhotoCache.get(downloadTask.getImageURL()));

        if (null == downloadTask.getByteBuffer()) {
            sInstance.mDownloadThreadPool.execute(downloadTask.getHTTPDownloadRunnable());
            imageView.setStatusResource(R.drawable.ic_launcher);
        } else {
            sInstance.handleState(downloadTask, DOWNLOAD_COMPLETE);
        }
        return downloadTask;
    }

    public void handleState(PhotoTask photoTask, int state) {
        switch (state) {
            case DOWNLOAD_COMPLETE:
                mDecodeThreadPool.execute(photoTask.getHTTPDownloadRunnable());
                break;
            case TASK_COMPLETE:
                if (photoTask.isCacheEnable()) {
                    mPhotoCache.put(photoTask.getImageURL(), photoTask.getByteBuffer());
                }
                Message completeMessage = mHandler.obtainMessage(state, photoTask);
                completeMessage.sendToTarget();
            default:
                mHandler.obtainMessage(state, photoTask).sendToTarget();
                break;
        }
    }
}
