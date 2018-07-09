package com.hankkin.reading.adapter.base;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bilibili.magicasakura.widgets.TintProgressBar;
import com.bilibili.magicasakura.widgets.TintTextView;
import com.hankkin.reading.R;

public class LoadingMoreFooter extends LinearLayout {

    public final static int STATE_LOADING = 0;
    public final static int STATE_COMPLETE = 1;
    public final static int STATE_NOMORE = 2;
    private TintTextView mText;
    private TintProgressBar mIvProgress;

    public LoadingMoreFooter(Context context) {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public LoadingMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void initView(Context context) {
        LayoutInflater.from(context).inflate(  R.layout.yun_refresh_footer, this);
        mText = (TintTextView) findViewById(  R.id.msg);
        mIvProgress = (TintProgressBar) findViewById(  R.id.progress);
        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setState(int state) {
        switch (state) {
            case STATE_LOADING:
                mIvProgress.setVisibility(View.VISIBLE);
                mText.setText(getContext().getText(  R.string.listview_loading));
                this.setVisibility(View.VISIBLE);
                break;
            case STATE_COMPLETE:
                mText.setText(getContext().getText(  R.string.listview_loading));
                this.setVisibility(View.GONE);
                break;
            case STATE_NOMORE:
                mText.setText(getContext().getText(  R.string.nomore_loading));
                mIvProgress.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void reSet() {
        this.setVisibility(GONE);
    }
}
