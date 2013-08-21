package com.kc.threadsample;

import java.nio.channels.UnsupportedAddressTypeException;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;
import android.util.SparseArray;

public class MyDataProvider extends ContentProvider{
    private SQLiteOpenHelper mHelper;
    
    
    private static final UriMatcher sUriMatcher;
    private static final SparseArray<String> sMimeTypes;
    public static final int IMAGE_URL_QUERY = 1;
    public static final int DATE_URL_QUERY = 2;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(MyDataProviderContract.AUTHORITY, MyDataProviderContract.PICTUREURL_TABLE_NAME, IMAGE_URL_QUERY);
        sUriMatcher.addURI(MyDataProviderContract.AUTHORITY, MyDataProviderContract.DATE_TABLE_NAME, DATE_URL_QUERY);

        sMimeTypes = new SparseArray<String>();
        sMimeTypes.put(IMAGE_URL_QUERY, 
                "vnd.android.cursor.dir/vnd." + MyDataProviderContract.AUTHORITY + "." + MyDataProviderContract.PICTUREURL_TABLE_NAME);
        sMimeTypes.put(DATE_URL_QUERY, 
                "vnd.android.cursor.item/vnd." + MyDataProviderContract.AUTHORITY + "." + MyDataProviderContract.DATE_TABLE_NAME);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        SQLiteDatabase db = mHelper.getReadableDatabase();
        
    }

    @Override
    public String getType(Uri uri) {
        int code = sUriMatcher.match(uri);
        return sMimeTypes.get(code);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        switch (sUriMatcher.match(uri)) {
            case DATE_URL_QUERY:
                SQLiteDatabase localSQLiteDatabase = mHelper.getWritableDatabase();
                long id = localSQLiteDatabase.insert(MyDataProviderContract.DATE_TABLE_NAME, MyDataProviderContract.DATE_TABLE_NAME, values);
                if(id == -1) {
                    throw new SQLiteException("Insert error:" + uri);
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return Uri.withAppendedPath(uri, Long.toString(id));

            case IMAGE_URL_QUERY:
                throw new IllegalArgumentException("Insert: Invalid URI" + uri);
            default:
                break;
        }
        return null;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        switch (sUriMatcher.match(uri)) {
            case IMAGE_URL_QUERY:
                SQLiteDatabase localSQLiteDatabase = mHelper.getWritableDatabase();
                localSQLiteDatabase.beginTransaction();
                localSQLiteDatabase.delete(MyDataProviderContract.PICTUREURL_TABLE_NAME, null, null);
                int numImages = values.length;
                for(int i=0; i < numImages; i++) {
                    localSQLiteDatabase.insert(MyDataProviderContract.PICTUREURL_TABLE_NAME, 
                            MyDataProviderContract.IMAGE_URL_COLUM, values[i]);
                }
                localSQLiteDatabase.setTransactionSuccessful();
                localSQLiteDatabase.endTransaction();
                localSQLiteDatabase.close();
                
                getContext().getContentResolver().notifyChange(uri, null);
                return numImages;
            case DATE_URL_QUERY:
                return super.bulkInsert(uri, values);
            default:
                break;
        }
        return -1;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Delete --- unsupported operation" + uri);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case IMAGE_URL_QUERY:
                throw new IllegalArgumentException("update: Invalid Uri" + uri);

            case DATE_URL_QUERY:
                SQLiteDatabase localDB = mHelper.getWritableDatabase();
                int rows = localDB.update(MyDataProviderContract.DATE_TABLE_NAME, values, selection, selectionArgs);
                if(rows != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                    return rows;
                } else {
                    throw new SQLiteException("update error" + uri);
                }
            default:
                break;
        }
        return -1;
    }

    /**
     * close the SQlite databae helper class, to avoid memory leaks.
     */
    public void close() {
        mHelper.close();
    }
    
    private class MyDataProviderHelper extends SQLiteOpenHelper {
        private static final String PRIMARY_KEY_TYPE = "INTEGER PRIMARY KEY";
        private static final String TEXT_TYPE = "TEXT";
        private static final String INTEGER_TYPE = "INTEGER";
        private static final String CREATE_PICTURE_TABLE_SQL = "CREATE TABLE " + 
                 MyDataProviderContract.PICTUREURL_TABLE_NAME + " (" + 
                 MyDataProviderContract.ROW_ID + " " + PRIMARY_KEY_TYPE + " ," +
                 MyDataProviderContract.IMAGE_URL_COLUM + " "  + TEXT_TYPE + " ," + 
                 MyDataProviderContract.IMAGE_PICTURENAME_COLUMN + " " + TEXT_TYPE + " ," +
                 MyDataProviderContract.IMAGE_THUMBURL_COLUM +  " " + TEXT_TYPE + " ," + 
                 MyDataProviderContract.IMAGE_THUMBNAME_COLUMN + " " + TEXT_TYPE + 
                ")";
        
        private static final String CREATE_DATE_TABLE_SQL = "CREATE TABLE " + 
                MyDataProviderContract.DATE_TABLE_NAME + " (" +
                MyDataProviderContract.ROW_ID + " " + PRIMARY_KEY_TYPE + " ," + 
                MyDataProviderContract.DATE_DATE_COLUM + " " + INTEGER_TYPE + 
                ")";

        public MyDataProviderHelper(Context context) {
            super(context, MyDataProviderContract.DATABASE_NAME, null, MyDataProviderContract.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_PICTURE_TABLE_SQL);
            db.execSQL(CREATE_DATE_TABLE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.v("kc", "Upgrading database from version " + oldVersion + " to " + newVersion);
            dropTables(db);
            onCreate(db);
        }

        private void dropTables(SQLiteDatabase db) {
            db.execSQL("DROP TABLE IF EXISTS" + MyDataProviderContract.PICTUREURL_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS" + MyDataProviderContract.DATE_TABLE_NAME);   
        }
    }
}
