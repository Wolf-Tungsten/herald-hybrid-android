package cn.myseu.heraldapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tencent.smtt.sdk.WebView;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    public static String BASE_URL = "http://192.168.1.101:8080";
    private WebView mWebView;

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

        // 获取WebView
        mWebView = (WebView) findViewById(R.id.auth_web_view);
        mWebView.clearCache(true);
        mWebView.loadUrl(BASE_URL + tabPath[0]);
        // 获取Tab元素
        findTabViews();
        setTabListener();

        Intent intent =  new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        String token = getToken();
        if (!token.equals("no-token")) {
            // token存在
            // TODO： 注入到WebView中
        } else {
            // token不存在
            Intent intent =  new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
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
                                mTabImageViews.get(index).setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),selectedIcons[index]));
                                mTabTextViews.get(index).setTextColor(getResources().getColor(R.color.colorPrimary));
                            } else {
                                mTabImageViews.get(index).setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),icons[index]));
                                mTabTextViews.get(index).setTextColor(getResources().getColor(R.color.colorUnfocused));
                            }
                        }
                    }
                }
            });
        }
    }

}
