package cn.myseu.heraldapp.Components;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebViewClient;
import android.widget.LinearLayout;

import java.text.AttributedCharacterIterator;
import io.reactivex.Observable;

import cn.myseu.heraldapp.R;

public class AuthWebView extends WebView {

    private  WebSettings webSettings;
    private String token;

    public AuthWebView(Context context) {

        super(context);
        webSettings = getSettings();
        // 根据需求进行配置
        webSettings.setJavaScriptEnabled(true);
        loadUrl("http://192.168.1.101:8080/");
    }

    public void setToken(String token){
        this.token = token;
    }

    public void injectToken(String token) {
        evaluateJavascript("window.injectToken('"+token+"')", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) { }
        });
    }




}
