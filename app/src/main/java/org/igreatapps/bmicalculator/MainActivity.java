package org.igreatapps.bmicalculator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import org.igreatapps.bmicalculator.R;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private WebSettings webSettings;
    private ProgressBar progressBar;
    private String baseurl = "file:///android_asset/index.html";

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView=findViewById(R.id.webView);
        progressBar=findViewById(R.id.progressBarH);
        imageView=findViewById(R.id.imageView);
        webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        webView.loadUrl(baseurl);
        webView.setVerticalScrollBarEnabled(true);
        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url1)
            {
                if(url1.startsWith(baseurl))
                {
                    progressBar.setVisibility(View.GONE);
                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url1));
                    startActivity(intent);
                    return true;
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url1));
                    startActivity(intent);
                    return false;
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);

            }

            @Override
            public void onPageFinished(WebView view, String url)
            {
                progressBar.setVisibility(View.GONE);
                if(webView.getVisibility()==View.GONE)
                {
                    webView.setVisibility(View.VISIBLE);
                }
                if(imageView.getVisibility()==View.VISIBLE)
                {
                    imageView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Toast.makeText(MainActivity.this,error.getDescription(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                Toast.makeText(MainActivity.this,errorResponse.getReasonPhrase(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                Toast.makeText(MainActivity.this, " initializing... ", Toast.LENGTH_SHORT).show();
            }
        });

        AdView mAdView;
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        webView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress)
            {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
            }
        });

    }

    @Override
    public void onBackPressed()
    {
        if(webView!=null && webView.canGoBack())
        {
            webView.goBack();
        }
        else
        {
            super.onBackPressed();
        }

    }
}