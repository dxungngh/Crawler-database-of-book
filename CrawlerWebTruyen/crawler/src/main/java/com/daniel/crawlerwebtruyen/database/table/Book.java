package com.daniel.crawlerwebtruyen.database.table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by danielnguyen on 10/14/14.
 */
@DatabaseTable(tableName = "book")
public class Book implements Serializable {
    private static final String TAG = Book.class.getSimpleName();

    public static class Fields {
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String AUTHOR = "author";
        public static final String TYPE = "type";
        public static final String STATUS = "status";
        public static final String OVERVIEW = "overview";
    }

    @DatabaseField(allowGeneratedIdInsert = true, canBeNull = false, columnName = Fields.ID, generatedId = true)
    private long mId;

    @DatabaseField(columnName = Fields.NAME)
    private String mName;

    @DatabaseField(columnName = Fields.AUTHOR)
    private String mAuthor;

    @DatabaseField(columnName = Fields.TYPE)
    private String mType;

    @DatabaseField(columnName = Fields.STATUS)
    private String mStatus;

    @DatabaseField(columnName = Fields.OVERVIEW)
    private String mOverview;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        mOverview = overview;
    }

    public String toString() {
        return "[" + mId + "--" + mName + "--" + mAuthor + "--" + mType + "--" + mStatus + "--" + mOverview + "]";
    }
}
