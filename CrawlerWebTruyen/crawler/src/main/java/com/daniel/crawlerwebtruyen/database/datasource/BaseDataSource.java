package com.daniel.crawlerwebtruyen.database.datasource;

/**
 * Created by danielnguyen on 10/14/14.
 */
public class BaseDataSource {
    private static final String TAG = BaseDataSource.class.getSimpleName();

    protected static boolean getBooleanFromAffectedRows(int affectedRows) {
        if (affectedRows > 0) {
            return true;
        }
        return false;
    }
}
