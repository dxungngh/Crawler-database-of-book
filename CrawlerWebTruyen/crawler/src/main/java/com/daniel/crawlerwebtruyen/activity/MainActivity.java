package com.daniel.crawlerwebtruyen.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.daniel.crawlerwebtruyen.R;
import com.daniel.crawlerwebtruyen.database.table.Book;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MainActivity extends ActionBarActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private final String BOOK_LINK = "http://webtruyen.com/buong-tay-toi-khong-lay-chong/";
    private final int TIMEOUT = 100000;

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
            mDocument = Jsoup.connect(BOOK_LINK).timeout(TIMEOUT).get();
        } catch (Exception e) {
            Log.e(TAG, "getDocument", e);
            getDocument();
        }
    }

    private void getDetailOfBook() {
        getInformationOfBook();
        getOverviewOfBook();

        Log.i(TAG, mBook.toString());
    }

    private void getInformationOfBook() {
        Element informationElement = mDocument.select(".contdetail").first();

        Element titleElement = informationElement.select(".title").first();
        mBook.setName(titleElement.select("[title]").first().text().trim());

        Element authorElement = informationElement.select(".author").first();
        String author = authorElement.text();
        mBook.setAuthor(author.split(":")[1].trim());

        Element typeElement = informationElement.select(".type").first().select("a").first();
        String type = typeElement.text();
        mBook.setType(type.trim());

        Element statusElement = informationElement.select(".status").first().select("a").first();
        String status = statusElement.text();
        mBook.setStatus(status.trim());
    }

    private void getOverviewOfBook() {
        Element element = mDocument.select(".mota").first();
        mBook.setOverview(element.html());
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