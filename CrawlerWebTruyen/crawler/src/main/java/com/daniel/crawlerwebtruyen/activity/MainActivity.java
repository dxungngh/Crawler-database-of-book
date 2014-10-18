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
import org.jsoup.select.Elements;

public class MainActivity extends ActionBarActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private final String BOOK_LINK = "http://webtruyen.com/buong-tay-toi-khong-lay-chong/";
    private final int TIMEOUT = 100000;
    private final String USER_AGENT = "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6";
    private final String REFERRER = "http://www.google.com";
    private final String PAGE_LINK = "http://webtruyen.com/story/Paging_listbook/5588/";

    private Book mBook;
    private Document mDocument;
    private boolean mIsDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBook = new Book();
        new CrawlBookTask().execute();
    }

    private void getChapterFromRow(Element rowElement) {
        try {
            Elements tdElements = rowElement.select("td");
            String name = tdElements.get(3).text();
            String link = tdElements.get(3).select("a").first().attr("href");
            Log.i(TAG, name + " -- " + link);
        } catch (Exception e) {
            Log.e(TAG, "getChapterFromRow", e);
        }
    }

    private void getChaptersOfPage(String pageLink) {
        try {
            Log.i(TAG, pageLink);
            Document pageDocument = Jsoup.connect(pageLink)
                .userAgent(USER_AGENT)
                .referrer(REFERRER)
                .timeout(TIMEOUT)
                .get();
            if (pageDocument == null) {
                mIsDone = true;
                return;
            }
            Elements rowElements = pageDocument.select(".gridlistchapter").first().select("tbody").first().select("tr");
            if (rowElements == null || rowElements.size() <= 1) {
                mIsDone = true;
                return;
            }
            for (int i = 1; i < rowElements.size(); i++) {
                Element rowElement = rowElements.get(i);
                getChapterFromRow(rowElement);
            }
        } catch (Exception e) {
            mIsDone = true;
            Log.e(TAG, "getChaptersOfPage", e);
        }
    }

    private void getDocument() {
        try {
            mDocument = Jsoup
                .connect(BOOK_LINK)
                .userAgent(USER_AGENT)
                .referrer(REFERRER)
                .timeout(TIMEOUT)
                .get();
        } catch (Exception e) {
            Log.e(TAG, "getDocument", e);
            getDocument();
        }
    }

    private void getDetailOfBook() {
        getInformationOfBook();
        getOverviewOfBook();
        getListOfChapters();

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

    private void getListOfChapters() {
        for (int i = 0; i < 1000; i++) {
            String pageLink = PAGE_LINK + i;
            getChaptersOfPage(pageLink);
            if (mIsDone) {
                break;
            }
        }
    }

    private void getOverviewOfBook() {
        Element element = mDocument.select(".mota").first();
        mBook.setOverview(element.html());
    }

    private class CrawlBookTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... unusedParams) {
            getDocument();
            getDetailOfBook();
            return null;
        }

        @Override
        protected void onPostExecute(Void unusedParam) {
        }
    }
}