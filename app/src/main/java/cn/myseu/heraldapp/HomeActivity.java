package cn.myseu.heraldapp;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolBar = findViewById(R.id.my_toolbar);
        toolBar.setTitle("");
        setSupportActionBar(toolBar);

    }
}
