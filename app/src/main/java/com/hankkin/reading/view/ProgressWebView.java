package com.hankkin.reading.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.hankkin.reading.R;


/**
 * Created by hankkin on 2017/5/11.
 */

public class ProgressWebView extends WebView {

    private  ProgressBar mProgressBar;

    public void hideProgress() {
        mProgressBar.setVisibility(GONE);
    }


    public static final String TAG = ProgressWebView.class.getSimpleName();


    public ProgressWebView(Context context) {
        super(context);
    }

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mProgressBar = new ProgressBar(context, null,
                android.R.attr.progressBarStyleHorizontal);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, 8);
        mProgressBar.setLayoutParams(layoutParams);

        Drawable drawable = context.getResources().getDrawable(
                R.drawable.web_progress_bar_states);
        mProgressBar.setProgressDrawable(drawable);
        addView(mProgressBar);
        setWebChromeClient(new WebChromeClient(mProgressBar));
        setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                loadUrl(url);
                return true;
            }
        });
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        invalidate();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public static class WebChromeClient extends android.webkit.WebChromeClient {

        private ProgressBar progressBar;

        public WebChromeClient(ProgressBar progressBar) {
            this.progressBar = progressBar;
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressBar.setVisibility(GONE);
            } else {
                if (progressBar.getVisibility() == GONE)
                    progressBar.setVisibility(VISIBLE);
                progressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) mProgressBar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        mProgressBar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }

}
