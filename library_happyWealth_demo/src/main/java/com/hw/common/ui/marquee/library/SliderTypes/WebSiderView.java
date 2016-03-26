package com.hw.common.ui.marquee.library.SliderTypes;

import com.hw.common.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

public class WebSiderView extends BaseSliderView{

    public WebSiderView(Context context) {
        super(context);
    }

    @Override
    public View getView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.render_type_webview,null);
        WebView target = (WebView)v.findViewById(R.id.daimajia_slider_webview);

        loadWeb(target);
        bindClickEvent(v);

        return v;
    }

}
