package cn.myseu.heraldapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import cn.myseu.heraldapp.Components.AuthWebView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    public static String BASE_URL = "http://192.168.1.101:8080";
    private AuthWebView mWebView;
    private LinearLayout mWebViewContainer;

    private ArrayList<LinearLayout> mTabButtons = new ArrayList<>();
    private ArrayList<ImageView> mTabImageViews = new ArrayList<>();
    private ArrayList<TextView> mTabTextViews = new ArrayList<>();
    private String[] tabPath = { "/home-tab/", "/activity-tab/", "/notification-tab/", "/personal-tab/"};
    private int mTabCurrentIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 设置ToolBar
        Toolbar toolBar = (Toolbar) findViewById(R.id.herald_toolbar);
        toolBar.setTitle("");
        setSupportActionBar(toolBar);

        // 生成WebView
        mWebView = new AuthWebView(HomeActivity.this);
        mWebViewContainer = (LinearLayout) findViewById(R.id.auth_web_view_container);
        mWebViewContainer.addView(mWebView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        // 获取Tab元素
        findTabViews();

        // 检查token
        final String token = getToken();
        //String token = "no-token";
        if (!token.equals("no-token")) {
            // token存在，加载WebView
            mWebView.setToken(token);
            mWebView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView webView, String s) {
                    super.onPageFinished(webView, s);
                    setTabListener();
                    Log.d("webview","页面加载完成");
                    mWebView.injectToken(token);
                }
            });
            mWebView.loadUrl(HomeActivity.BASE_URL + tabPath[0]); // 在创建活动时即加载
        } else {
            // token不存在，启动登录界面并销毁当前活动
            Intent intent =  new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        // 用于调试
        toolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =  new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebViewContainer.removeAllViews();
            if(mWebView != null) {
                mWebView.clearHistory();
                mWebView.clearCache(true);
                mWebView.loadUrl("about:blank"); // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated now
                mWebView.pauseTimers();
                mWebView = null; // Note that mWebView.destroy() and mWebView = null do the exact same thing
            }
        }
    }

    private String getToken() {
        SharedPreferences tokenSharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
        return tokenSharedPreferences.getString("token", "no-token");
    }

    private void findTabViews() {
        // 获取TabButtons
        mTabButtons.add( (LinearLayout) findViewById(R.id.tab_home_button));
        mTabButtons.add( (LinearLayout) findViewById(R.id.tab_activity_button));
        mTabButtons.add( (LinearLayout) findViewById(R.id.tab_notification_button));
        mTabButtons.add( (LinearLayout) findViewById(R.id.tab_personal_button));
        // 获取TabImages
        mTabImageViews.add( (ImageView) findViewById(R.id.tab_home_image));
        mTabImageViews.add( (ImageView) findViewById(R.id.tab_activity_image));
        mTabImageViews.add( (ImageView) findViewById(R.id.tab_notification_image));
        mTabImageViews.add( (ImageView) findViewById(R.id.tab_personal_image));
        // 获取tabTexts
        mTabTextViews.add( (TextView) findViewById(R.id.tab_home_text));
        mTabTextViews.add( (TextView) findViewById(R.id.tab_activity_text));
        mTabTextViews.add( (TextView) findViewById(R.id.tab_notification_text));
        mTabTextViews.add( (TextView) findViewById(R.id.tab_personal_text));
    }

    private void setTabListener() {
        for ( LinearLayout button: mTabButtons) {
            final int index = mTabButtons.indexOf(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (index != mTabCurrentIndex) {
                        mTabCurrentIndex = index;
                        int[] selectedIcons = {R.drawable.home_tab_icon_selected,
                                R.drawable.activity_tab_icon_selected,
                                R.drawable.notification_tab_icon_selected,
                                R.drawable.personal_tab_icon_selected};

                        int[] icons = {R.drawable.home_tab_icon,
                                R.drawable.activity_tab_icon,
                                R.drawable.notification_tab_icon,
                                R.drawable.personal_tab_icon};
                        mWebView.loadUrl(HomeActivity.BASE_URL + tabPath[index]);
                        for (int buttonIndex = 0; buttonIndex < mTabButtons.size(); buttonIndex++) {
                            if(buttonIndex == index){
                                mTabImageViews.get(buttonIndex).setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),selectedIcons[buttonIndex]));
                                mTabTextViews.get(buttonIndex).setTextColor(getResources().getColor(R.color.colorPrimary));
                            } else {
                                mTabImageViews.get(buttonIndex).setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),icons[buttonIndex]));
                                mTabTextViews.get(buttonIndex).setTextColor(getResources().getColor(R.color.colorUnfocused));
                            }
                        }
                    }
                }
            });
        }
    }

}
