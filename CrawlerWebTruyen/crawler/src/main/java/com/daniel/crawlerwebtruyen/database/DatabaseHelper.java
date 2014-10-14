package com.daniel.crawlerwebtruyen.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.daniel.crawlerwebtruyen.database.table.Book;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private Dao<Book, Integer> mBookDao = null;

    private static final String TAG = DatabaseHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void close() {
        super.close();
        mBookDao = null;
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i(TAG, "create database");
            TableUtils.createTable(connectionSource, Book.class);
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
}
