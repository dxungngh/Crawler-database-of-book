package com.daniel.crawlerwebtruyen.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.daniel.crawlerwebtruyen.database.table.Book;
import com.daniel.crawlerwebtruyen.database.table.Chapter;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.io.File;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private Dao<Book, Integer> mBookDao = null;
    private Dao<Chapter, Integer> mChapterDao = null;

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = Environment.getExternalStorageState() + File.separator + "mydatabase.db";
    private static final int DATABASE_VERSION = 6;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void close() {
        super.close();
        mBookDao = null;
        mChapterDao = null;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i(TAG, "create database");
            TableUtils.createTable(connectionSource, Book.class);
            TableUtils.createTable(connectionSource, Chapter.class);
        } catch (SQLException e) {
            Log.e(TAG, "onCreate", e);
            throw new RuntimeException(e);
        } catch (java.sql.SQLException e) {
            Log.e(TAG, "onCreate", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion,
                          int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Book.class, true);
            TableUtils.dropTable(connectionSource, Chapter.class, true);
            onCreate(db);
        } catch (Exception e) {
            Log.e(TAG, "onUpgrade", e);
        }
    }

    public Dao<Book, Integer> getBookDao() {
        if (null == mBookDao) {
            try {
                mBookDao = getDao(Book.class);
            } catch (java.sql.SQLException e) {
                Log.e(TAG, "getBookDao", e);
            }
        }
        return mBookDao;
    }

    public Dao<Chapter, Integer> getChapterDao() {
        if (null == mChapterDao) {
            try {
                mChapterDao = getDao(Chapter.class);
            } catch (java.sql.SQLException e) {
                Log.e(TAG, "getChapterDao", e);
            }
        }
        return mChapterDao;
    }
}
