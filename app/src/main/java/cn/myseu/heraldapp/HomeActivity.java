package cn.myseu.heraldapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import cn.myseu.heraldapp.Components.AuthWebView;

import java.util.ArrayList;
import java.util.Stack;

public class HomeActivity extends AppCompatActivity {

    public static String BASE_URL = "http://192.168.1.101:8080";
    private AuthWebView mWebView;
    private LinearLayout mWebViewContainer;
    private ConstraintLayout mTabBar;
    private Toolbar mToolbar;
    private ImageView mBackButton;
    private TextView mNavigationTitle;
    private ImageView mIcon;

    private ArrayList<LinearLayout> mTabButtons = new ArrayList<>();
    private ArrayList<ImageView> mTabImageViews = new ArrayList<>();
    private ArrayList<TextView> mTabTextViews = new ArrayList<>();

    private String mToken;

    private String[] tabPath = { "/home-tab/", "/activity-tab/", "/notification-tab/", "/personal-tab/"};
    private int mTabCurrentIndex = 0;

    private Stack<ArrayList<String>> mRouteHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 设置ToolBar
        mToolbar = (Toolbar) findViewById(R.id.herald_toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        mBackButton = (ImageView) findViewById(R.id.back_button);
        mNavigationTitle = (TextView) findViewById(R.id.navigation_text);
        mIcon = (ImageView) findViewById(R.id.toolbar_icon);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popRoute();
            }
        });

        // 生成WebView
        mWebView = new AuthWebView(HomeActivity.this);
        mWebViewContainer = (LinearLayout) findViewById(R.id.auth_web_view_container);
        mWebViewContainer.addView(mWebView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        mTabBar = (ConstraintLayout) findViewById(R.id.herald_tabbar);

        // 获取Tab元素
        findTabViews();

        // 检查token
        mToken = getToken();
        //String token = "no-token";
        if (!mToken.equals("no-token")) {
            // token存在，加载WebView
            mWebView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView webView, String s) {
                    super.onPageFinished(webView, s);
                    setTabListener();
                    Log.d("webview","页面加载完成");
                    //mWebView.injectToken(mToken);
                }
            });
            setJsInterface();
            mWebView.loadUrl(HomeActivity.BASE_URL + tabPath[0]); // 在创建活动时即加载
        } else {
            authFail();
        }

        // 生成路由栈，初始为空
        mRouteHistory = new Stack<>();
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebViewContainer.removeAllViews();
            if(mWebView != null) {
                mWebView.clearHistory();
                mWebView.clearCache(true);
                mWebView.loadUrl("about:blank"); // clearView() should be changed to loadUrl("about:blank"), since clearView() is deprecated no
                mWebView.destroy(); // Note that mWebView.destroy() and mWebView = null do the exact same thing
            }
        }
        super.onDestroy();
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

    private class JsInterface {
        @JavascriptInterface
        public String getToken() {
            Log.d("jsInterface", "token获取成功");
            return mToken;
        }

        @JavascriptInterface
        public void authFail() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    HomeActivity.this.authFail();
                }
            });
        }

        @JavascriptInterface
        public void pushRoute(final String route, final String title) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    HomeActivity.this.pushRoute(route, title);
                }
            });
        }
    }

    private void setJsInterface() {
        mWebView.addJavascriptInterface(new JsInterface(), "hybrid");
    }

    private void authFail() {
        // token不存在，启动登录界面并销毁当前活动
        Intent intent =  new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void pushRoute(String route, String title){
        Log.d("route", route);
        Log.d("title", title);
        mWebView.pushRoute(route);
        ArrayList<String> history = new ArrayList<>();
        history.add(route);
        history.add(title);
        mRouteHistory.push(history);
        mNavigationTitle.setText(title);
        mNavigationTitle.setVisibility(View.VISIBLE);
        mBackButton.setVisibility(View.VISIBLE);
        mTabBar.setVisibility(View.GONE);
        mIcon.setVisibility(View.GONE);
    }

    private void popRoute(){
        mRouteHistory.pop();
        if(!mRouteHistory.empty()) {
            ArrayList<String> history = mRouteHistory.peek();
            mNavigationTitle.setText(history.get(1));
            mWebView.pushRoute(history.get(0));
        } else {
            mNavigationTitle.setVisibility(View.GONE);
            mBackButton.setVisibility(View.GONE);
            mIcon.setVisibility(View.VISIBLE);
            mTabBar.setVisibility(View.VISIBLE);
            mWebView.pushRoute(tabPath[mTabCurrentIndex]);
        }
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
                        mWebView.pushRoute(tabPath[index]);
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
