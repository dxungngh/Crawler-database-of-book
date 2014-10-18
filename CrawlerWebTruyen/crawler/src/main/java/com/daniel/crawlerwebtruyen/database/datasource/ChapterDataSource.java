package com.daniel.crawlerwebtruyen.database.datasource;

/**
 * Created by danielnguyen on 10/14/14.
 */

import android.content.Context;
import android.util.Log;

import com.daniel.crawlerwebtruyen.database.DatabaseManager;
import com.daniel.crawlerwebtruyen.database.table.Chapter;

/**
 * Created by daniel_nguyen on 6/15/14.
 */
public class ChapterDataSource extends BaseDataSource {
    private static final String TAG = ChapterDataSource.class.getSimpleName();

    public static boolean createChapter(Context context, Chapter chapter) {
        try {
            int affectedRows =
                DatabaseManager.getInstance(context).getHelper().getChapterDao().create(chapter);
            return getBooleanFromAffectedRows(affectedRows);
        } catch (Exception e) {
            Log.e(TAG, "createChapter", e);
            return false;
        }
    }
}