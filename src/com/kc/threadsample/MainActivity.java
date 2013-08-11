
package com.kc.threadsample;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Menu;

public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initConstants();
        
        FragmentManager fragmentManager = getSupportFragmentManager();
        if(savedInstanceState == null) {
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.add(R.id.fragment_host, new MyPhotoThumbnailFragment(), Constants.THUMBNAIL_FRAGMENT_TAG);
            ft.commit();
        }
    }
    
    private void  initConstants() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Constants.WIDTH_PIXELS = displayMetrics.widthPixels;
        Constants.HEIGHT_PIXELS = displayMetrics.heightPixels;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
