package com.hankkin.library.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Window;

import com.hankkin.library.R;
import com.hankkin.library.widget.view.indicators.AVLoadingIndicatorView;


/**
 * Created by hankkin on 2017/9/30.
 * Blog: http://hankkin.cn
 * Mail: 1019283569@qq.com
 */

public class HLoading extends Dialog {


    private Context context;
    AVLoadingIndicatorView avi;
    private int color;

    public HLoading(@NonNull Context context,int color) {
        super(context, R.style.progress);
        this.context = context;
        this.color = color;
        init();
    }

    private void init(){
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_hloading);

        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        //设置SelectPicPopupWindow弹出窗体的背景
        getWindow().setBackgroundDrawableResource(R.color.transparent);
        avi.setIndicatorColor(color);
        avi.setIndicator("BallSpinFadeLoaderIndicator");
    }


    @Override
    public void onBackPressed() {
        dismiss();
    }
}
