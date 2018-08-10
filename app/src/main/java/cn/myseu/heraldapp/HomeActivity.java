package cn.myseu.heraldapp;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebSettings;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;

public class HomeActivity extends AppCompatActivity {

    public static String BASE_URL = "http://192.168.1.101:8080";
    private WebView mWebView;

    private ArrayList<LinearLayout> tabButtons = new ArrayList<>();
    private ArrayList<ImageView> tabImages = new ArrayList<>();
    private ArrayList<TextView> tabTexts = new ArrayList<>();
    private String[] tabPath = { "/home-tab/", "/activity-tab/", "/notification-tab/", "/personal-tab/"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // 设置ToolBar
        Toolbar toolBar = (Toolbar) findViewById(R.id.herald_toolbar);
        toolBar.setTitle("");
        setSupportActionBar(toolBar);

        // 获取WebView
        mWebView = (WebView) findViewById(R.id.auth_web_view);
        mWebView.clearCache(true);
        mWebView.loadUrl(BASE_URL + tabPath[0]);
        // 获取Tab元素
        findTabViews();
        setTabListener();

    }

    private void findTabViews() {
        // 获取TabButtons
        tabButtons.add( (LinearLayout) findViewById(R.id.tab_home_button));
        tabButtons.add( (LinearLayout) findViewById(R.id.tab_activity_button));
        tabButtons.add( (LinearLayout) findViewById(R.id.tab_notification_button));
        tabButtons.add( (LinearLayout) findViewById(R.id.tab_personal_button));
        // 获取TabImages
        tabImages.add( (ImageView) findViewById(R.id.tab_home_image));
        tabImages.add( (ImageView) findViewById(R.id.tab_activity_image));
        tabImages.add( (ImageView) findViewById(R.id.tab_notification_image));
        tabImages.add( (ImageView) findViewById(R.id.tab_personal_image));
        // 获取tabTexts
        tabTexts.add( (TextView) findViewById(R.id.tab_home_text));
        tabTexts.add( (TextView) findViewById(R.id.tab_activity_text));
        tabTexts.add( (TextView) findViewById(R.id.tab_notification_text));
        tabTexts.add( (TextView) findViewById(R.id.tab_personal_text));
    }

    private void setTabListener() {
        for ( LinearLayout button: tabButtons) {
            final int index = tabButtons.indexOf(button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mWebView.loadUrl(HomeActivity.BASE_URL + tabPath[index]);
                }
            });
        }
    }

}
