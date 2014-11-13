package com.daniel.crawlerwebtruyen.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

import com.daniel.crawlerwebtruyen.R;
import com.daniel.crawlerwebtruyen.database.datasource.BookDataSource;
import com.daniel.crawlerwebtruyen.database.datasource.ChapterDataSource;
import com.daniel.crawlerwebtruyen.database.table.Book;
import com.daniel.crawlerwebtruyen.database.table.Chapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MainActivity extends ActionBarActivity {

    private final String TAG = MainActivity.class.getSimpleName();
    private final String BOOK_LINK = "http://webtruyen.com/365-ngay-hon-nhan/";
    private final int TIMEOUT = 100000;
    private final String USER_AGENT = "Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6";
    private final String REFERRER = "http://www.google.com";

    private Book mBook;
    private int mBookId;
    private Document mDocument;
    private boolean mIsDone = false;
    private String mPageLink = "http://webtruyen.com/story/Paging_listbook/";

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
            String name = tdElements.get(3).select("a").first().attr("title");
            String link = tdElements.get(3).select("a").first().attr("href");

            Chapter chapter = new Chapter();
            chapter.setName(name);
            chapter.setLink(link);
            getContentOfChapter(chapter);
        } catch (Exception e) {
            Log.e(TAG, "getChapterFromRow", e);
        }
    }

    private void getChaptersOfPage(String pageLink) {
        try {
//            Log.i(TAG, pageLink);
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

    private void getContentOfChapter(Chapter chapter) {
        try {
            Document chapterDocument = Jsoup.connect(chapter.getLink())
                .userAgent(USER_AGENT)
                .referrer(REFERRER)
                .timeout(TIMEOUT)
                .get();
            Element contentElement = chapterDocument.select("#detailcontent").first();
            contentElement.select("div[align=left]").first().remove();
            String content = contentElement.html();
            content.replace("http://webtruyen.com", "");
            content.replace("webtruyen.com", "");
            content.replace("webtruyen", "");
            chapter.setContent(content);
            ChapterDataSource.createChapter(this, chapter);

//            Log.i(TAG, content.toString());
        } catch (Exception e) {
            Log.e(TAG, "getContentOfChapter", e);
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

//        Log.i(TAG, mBook.toString());
        BookDataSource.createBook(this, mBook);
        Log.i(TAG, "Crawling is done!");
    }

    private void getIdOfBook() {
        Element inputElement = mDocument.select(".input_page").first().select("input").get(1);
        String onclickValue = inputElement.attr("onclick");
        onclickValue = onclickValue.replace("paginglistbook(", "");
        onclickValue = onclickValue.replace(", $('#page').val())", "");
        mBookId = Integer.parseInt(onclickValue.trim());
        Log.i(TAG, "book id: " + mBookId);
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
        getIdOfBook();
        mPageLink = mPageLink + mBookId + "/";
        for (int i = 1; i < 1000; i++) {
            String pageLink = mPageLink + i;
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