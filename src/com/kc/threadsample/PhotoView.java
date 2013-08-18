
package com.kc.threadsample;

import java.lang.ref.WeakReference;
import java.net.URL;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class PhotoView extends ImageView {

    private int mHideShowResId;

    public PhotoView(Context context) {
        super(context);
    }

    public PhotoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttributes(attrs);
    }

    public PhotoView(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);
        getAttributes(attrs);
    }

    private void getAttributes(AttributeSet attrs) {
        TypedArray attributes = getContext().obtainStyledAttributes(attrs,
                R.styleable.ImageDownloaderView);
        mHideShowResId = attributes.getResourceId(R.styleable.ImageDownloaderView_hideShowSibling,
                -1);
        attributes.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private URL mImageUrl;

    public void setImageURL(URL pictureURL, boolean cacheFlag, Drawable defaultDrawable) {
        if (mImageUrl != null) {
            if (!mImageUrl.equals(pictureURL)) {
                // remove download
            } else {
                return;
            }
        }

        setImageDrawable(defaultDrawable);
        mImageUrl = pictureURL;

    }

    public URL getLocation() {
        return mImageUrl;
    }

    private WeakReference<View> mThisView;

    public void setStatusDrawable(Drawable drawable) {
        if (mThisView == null) {
            setImageDrawable(drawable);
        }
    }

    public void setStatusResource(int resId) {
        if (mThisView == null) {
            setImageResource(resId);
        }
    }
}
