package com.blj.search;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class MainActivity extends AppCompatActivity {

    private MyWebViewFragment mWebFragment;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getFragmentManager();

        mWebFragment = (MyWebViewFragment) fm.findFragmentById(android.R.id.content);

        if (mWebFragment == null) {
            mWebFragment = new MyWebViewFragment();
            fm.beginTransaction().add(android.R.id.content, mWebFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        WebView webView = mWebFragment.getWebView();
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);

        final MenuItem searchMenu = menu.findItem(R.id.menu_search);
        mSearchView = (SearchView) searchMenu.getActionView();

        mSearchView.setIconifiedByDefault(true);

        mSearchView.setSubmitButtonEnabled(false);

        mSearchView.setOnSearchClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                WebView webView = mWebFragment.getWebView();
                mSearchView.setQuery(webView.getUrl(), false);
            }
        });

        mSearchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String url;
                if (query.startsWith("http://") || query.startsWith("https://")) {
                    url = query;
                } else {

                    String encodedQuery = Uri.encode(query);
                    url = "https://www.google.com/search?q=" + encodedQuery;
                }

                mWebFragment.getWebView().loadUrl(url);

                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);

                searchMenu.collapseActionView();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }
}