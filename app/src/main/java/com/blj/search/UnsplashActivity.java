package com.blj.search;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.versionedparcelable.VersionedParcelable;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class UnsplashActivity extends AppCompatActivity {

    private WebView mWebView;
    ProgressBar progressBar;
    
    private SwipeRefreshLayout mySwipeRefreshLayout;
    
    NestedScrollView nestedScrollView;

    private String encodedQuery;

    final String url="https://unsplash.com/s/photos/"+ encodedQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsplash);

        PackageManager packageManager = getPackageManager();
        
        viewInit();

        mWebView.loadUrl(url);

        //checkPermission();//storage

        webSettings();

        setMySwipeRefreshLayout();

    }

    void setRTL(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }

    final void setMySwipeRefreshLayout(){
        mySwipeRefreshLayout = findViewById(R.id.swipeContainer);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    final public void onRefresh() {
                        mWebView.reload();
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                }
        );
    }

    final void viewInit(){
        mWebView = findViewById(R.id.web_view);
        progressBar = findViewById(R.id.progressBar);
        nestedScrollView = findViewById(R.id.nested);
    }

    void webSettings(){
        mWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDescription,
                                        String mimetype, long contentLength) {

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(
                        DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                String fileName = URLUtil.guessFileName(url,contentDescription,mimetype);

                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName);

                DownloadManager dManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                dManager.enqueue(request);

                Toast.makeText(getApplicationContext(), R.string.download_info, Toast.LENGTH_SHORT).show();
            }
        });

        mWebView.setWebViewClient(new WebViewClient()
        {

            public void onReceivedError(WebView mWebView, int i, String s, String d1)
            {
                Toast.makeText(getApplicationContext(),"No Internet Connection!",Toast.LENGTH_SHORT).show();
                mWebView.loadUrl("file:///android_asset/net_error.html");
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
                super.onPageFinished(view, url);
                boolean d = false;
                if(d==false){
                    nestedScrollView.scrollTo(0,0);
                    d=true;
                }
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                //opening link external browser
                /*if(!url.contains("android_asset")){
                    view.setWebViewClient(null);
                } else {
                    view.setWebViewClient(new WebViewClient());
                }*/

                if(url.contains("youtube.com") || url.contains("play.google.com") || url.contains("google.com/maps") || url.contains("facebook.com") || url.contains("twitter.com") || url.contains("instagram.com")){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                }

                view.loadUrl(url);
                return true;
            }

        });

        WebSettings webSettings = mWebView.getSettings();

        webSettings.setDomStorageEnabled(true);
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSettings.getSaveFormData();
        webSettings.setDisplayZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSavePassword(true);
       // webSettings.setSupportMultipleWindows(true); //?a href problem
        webSettings.getJavaScriptEnabled();
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setGeolocationEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setLoadsImagesAutomatically(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
       // mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(false);
      //  webSettings.setJavaScriptCanOpenWindowsAutomatically(false); //(popup)
    }

    protected void checkPermission(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                            ActivityCompat.requestPermissions(
                                    MainActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    123
                            );

                }else {
                    // Request permission
                    ActivityCompat.requestPermissions(
                            MainActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            123
                    );
                }
            }else {
                // Permission already granted
            }
        }
    }

    @Override
    public void onBackPressed(){
        if(getSupportActionBar().getTitle().equals("Local Page")){
            setTitle("Free Images");
            mWebView.setVisibility(View.VISIBLE);
            mWebView.loadUrl(url);
        }
        else if(mWebView.canGoBack())
            mWebView.goBack();
        else{
            new AlertDialog.Builder(this)
                    .setTitle("Exit")
                    .setMessage("Are you sure you want to exit the application?")
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        final public void onClick(DialogInterface arg0, int arg1) {
                            MainActivity.super.onBackPressed();
                        }
                    }).create().show();
        }
    }
}