package com.daniel.crawlerwebtruyen.database.datasource;

/**
 * Created by danielnguyen on 10/14/14.
 */

import android.content.Context;
import android.util.Log;

import com.daniel.crawlerwebtruyen.database.DatabaseHelper;
import com.daniel.crawlerwebtruyen.database.DatabaseManager;
import com.daniel.crawlerwebtruyen.database.table.Book;

/**
 * Created by daniel_nguyen on 6/15/14.
 */
public class BookDataSource extends BaseDataSource {
    private static final String TAG = BookDataSource.class.getSimpleName();

    public static boolean createBook(Context context, Book book) {
        try {
            int affectedRows =
                    DatabaseManager.getInstance(context).getHelper().getBookDao().create(
                            book);
            return getBooleanFromAffectedRows(affectedRows);
        } catch (Exception e) {
            Log.e(TAG, "saveAccount", e);
            return false;
        }
    }

    public static Book getBook(Context context) {
        try {
            DatabaseHelper helper = DatabaseManager.getInstance(context).getHelper();
            Book book = helper.getBookDao()
                    .queryForFirst(helper.getBookDao().queryBuilder().prepare());
            return book;
        } catch (Exception e) {
            Log.e(TAG, "getBook", e);
            return null;
        }
    }
}