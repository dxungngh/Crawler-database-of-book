package com.daniel.crawlerwebtruyen.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.daniel.crawlerwebtruyen.R;
import com.daniel.crawlerwebtruyen.database.table.Book;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MainActivity extends ActionBarActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private final String BOOK_LINK = "http://webtruyen.com/buong-tay-toi-khong-lay-chong/";

    private Book mBook;
    private Document mDocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBook = new Book();
        getDocument();
        getDetailOfBook();
    }

    private void getDocument() {
        try {
            mDocument = Jsoup.connect(BOOK_LINK).get();
        } catch (Exception e) {
            Log.e(TAG, "getDocument", e);
            getDocument();
        }
    }

    private void getDetailOfBook() {

    }
}