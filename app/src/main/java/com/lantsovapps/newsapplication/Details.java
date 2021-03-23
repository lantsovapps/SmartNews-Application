package com.lantsovapps.newsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Details extends AppCompatActivity {

    private TextView tvTitle, tvSource, tvDate, tvDesc;
    private WebView webView;
    private ImageView imageView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvTitle = (TextView) findViewById(R.id.textViewHeader);
        tvSource = (TextView) findViewById(R.id.textViewSource);
        tvDate = (TextView) findViewById(R.id.textViewDate);
        tvDesc = (TextView) findViewById(R.id.textViewDesc);

        progressBar = (ProgressBar) findViewById(R.id.progressBarWeb);
        progressBar.setVisibility(View.VISIBLE);

        imageView = (ImageView) findViewById(R.id.imageDetailArticle);

        webView = (WebView) findViewById(R.id.webView);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String source = intent.getStringExtra("source");
        String publishedAt = intent.getStringExtra("publishedAt");
        String urlToImage = intent.getStringExtra("urlToImage");
        String description = intent.getStringExtra("description");
        String url = intent.getStringExtra("url");

        tvTitle.setText(title);
        tvSource.setText(source);
        tvDate.setText(publishedAt);
        tvDesc.setText(description);

        Picasso.with(Details.this).load(urlToImage).into(imageView);

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
        if(webView.isShown()){
            progressBar.setVisibility(View.INVISIBLE);
        }


    }
}
