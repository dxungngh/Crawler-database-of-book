package com.daniel.crawlerwebtruyen.activity;

import android.os.AsyncTask;
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
        new CrawlBookTask().execute();
    }

    private void getDocument() {
        try {
            mDocument = Jsoup.connect(BOOK_LINK).timeout(100000).get();
        } catch (Exception e) {
            Log.e(TAG, "getDocument", e);
            getDocument();
        }
    }

    private void getDetailOfBook() {

    }

    private class CrawlBookTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... unusedParams) {
            getDocument();
            return null;
        }

        @Override
        protected void onPostExecute(Void unusedParam) {
            getDetailOfBook();
        }
    }
}