package cn.myseu.heraldapp.Components;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebSettings;
import android.widget.LinearLayout;

import java.text.AttributedCharacterIterator;

import cn.myseu.heraldapp.R;

public class AuthWebView extends WebView {

    private  WebSettings webSettings;
    public AuthWebView(Context context, AttributeSet attrs) {

        super(context, attrs);
        webSettings = getSettings();
        // 根据需求进行配置
        webSettings.setJavaScriptEnabled(true);
        loadUrl("http://192.168.1.101:8080/");
    }



}
