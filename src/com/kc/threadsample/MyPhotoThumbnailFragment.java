
package com.kc.threadsample;

import java.net.MalformedURLException;
import java.net.URL;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class MyPhotoThumbnailFragment extends Fragment implements AdapterView.OnItemClickListener,
        LoaderCallbacks<Cursor> {

    private static final int URL_LOADER = 0;
    private static final String[] PROJECTION = {
            MyDataProviderContract._ID, MyDataProviderContract.IMAGE_THUMBURL_COLUM,
            MyDataProviderContract.IMAGE_URL_COLUM,
    };

    private GridView mGridView;
    private int mColumnWidth;
    private GridViewAdapter mAdapter;

    private Drawable mEmptyDrawable;
    private Intent sServiceIntent;
    private boolean mIsLoader = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.my_gridlist, container, false);

        mGridView = (GridView) view.findViewById(android.R.id.list);

        int pixelSize = getResources().getDimensionPixelSize(R.dimen.thumbsize);
        int widthScale = Constants.WIDTH_PIXELS / pixelSize;
        mColumnWidth = Constants.WIDTH_PIXELS / widthScale;

        mGridView.setColumnWidth(mColumnWidth);
        mGridView.setNumColumns(-1);

        mAdapter = new GridViewAdapter(getActivity());
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);

        mEmptyDrawable = getResources().getDrawable(R.drawable.ic_launcher);

        // getLoaderManager().initLoader(URL_LOADER, null, this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sServiceIntent = new Intent(getActivity(), MyRssPullService.class).setData(Uri
                .parse(Constants.PICASA_RSS_URL));
        if (savedInstanceState == null) {
            if (!this.mIsLoader) {
                getActivity().startService(sServiceIntent);
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderID, Bundle bundle) {
        switch (loaderID) {
            case URL_LOADER:
                return new CursorLoader(getActivity(),
                        MyDataProviderContract.PICTUREURL_TABLE_CONTENTURI, PROJECTION, null, null,
                        null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> arg0, Cursor returnCursor) {
        mAdapter.changeCursor(returnCursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        mAdapter.changeCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private class GridViewAdapter extends CursorAdapter {
        public GridViewAdapter(Context context) {
            super(context, null, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            PhotoView localImageDownloaderView = (PhotoView) view.getTag();

            try {
                URL localURL = new URL(cursor.getString(IMAGE_THUMBURI_CURSOR_INDEX));
                localImageDownloaderView.setImageURL(localURL, true, mEmptyDrawable);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.gallery_item, null);
            View iv = view.findViewById(R.id.thumbImage);

            view.setLayoutParams(new AbsListView.LayoutParams(
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT, Constants.WIDTH_PIXELS));
            view.setTag(iv);
            return view;
        }
    }

    private static final int IMAGE_THUMBURI_CURSOR_INDEX = 1;
    private static final int IMAGE_URL_CURSOR_INDEX = 2;

}
