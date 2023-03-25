package com.blj.search;

import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

public class MyWebViewFragment extends Fragment {

    private WebView mWebView;
    private ProgressBar mProgressbar;

    ImageButton image1, image2, image3, image4, image5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_webview, container, false);

        mWebView = (WebView) view.findViewById(R.id.webView);
        mProgressbar = (ProgressBar) view.findViewById(R.id.progressbar);

        image1 = image1.findViewById(R.id.image1);
        image2 = image2.findViewById(R.id.image2);
        image3 = image3.findViewById(R.id.image3);
        image4 = image4.findViewById(R.id.image4);
        image5 = image5.findViewById(R.id.image5);

        WebSettings settings = mWebView.getSettings();

        settings.setJavaScriptEnabled(true);

        settings.setPluginState(PluginState.ON_DEMAND);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                mProgressbar.setVisibility(View.VISIBLE);

                ActionBar actionBar = getActivity().getActionBar();
                if (actionBar != null) {
                    actionBar.setSubtitle(url);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {

                mProgressbar.setVisibility(View.GONE);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                mProgressbar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {

                ActionBar actionBar = getActivity().getActionBar();
                if (actionBar != null) {
                    actionBar.setTitle(title);
                }
            }
        });

        if (savedInstanceState == null) {
            mWebView.loadUrl("https://www.google.com/");
        } else {

            mWebView.restoreState(savedInstanceState);
        }
        return view;

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(this, FreeImagesActivity.class);
                startActivity(intent);
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(this, PexelsActivity.class);
                startActivity(intent);
            }
        });

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(this, UnsplashActivity.class);
                startActivity(intent);
            }
        });

        image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(this, StockSnapActivity.class);
                startActivity(intent);
            }
        });

        image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(this, PixabayActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        mWebView.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    public WebView getWebView() {
        return mWebView;
    }

    @Override
    public void onPause() {
        super.onPause();

        mWebView.onPause();
    }

    @Override
    public void onResume() {
        mWebView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {

        if (mWebView != null) {
            mWebView.stopLoading();
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }
}
