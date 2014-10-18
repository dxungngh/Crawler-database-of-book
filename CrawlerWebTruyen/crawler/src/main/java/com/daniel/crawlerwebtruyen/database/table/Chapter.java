package com.daniel.crawlerwebtruyen.database.table;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by danielnguyen on 10/14/14.
 */
@DatabaseTable(tableName = "chapter")
public class Chapter implements Serializable {
    private static final String TAG = Chapter.class.getSimpleName();

    public static class Fields {
        public static final String ID = "id";
        public static final String CONTENT = "content";
        public static final String LINK = "link";
        public static final String NAME = "name";
    }

    @DatabaseField(id = true, canBeNull = false, columnName = Fields.ID)
    private long mId;

    @DatabaseField(columnName = Fields.CONTENT)
    private String mContent;

    @DatabaseField(columnName = Fields.LINK)
    private String mLink;

    @DatabaseField(columnName = Fields.NAME)
    private String mName;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String toString() {
        return "[" + mId + "--" + mName + "--" + mLink + "--" + mContent + "]";
    }
}
