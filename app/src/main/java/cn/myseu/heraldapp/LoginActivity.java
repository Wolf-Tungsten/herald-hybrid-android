package cn.myseu.heraldapp;

import android.content.SharedPreferences;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.reactivestreams.Subscription;

import cn.myseu.heraldapp.Services.AuthService;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LoginActivity extends AppCompatActivity {

    private ImageView mLoginFaceImageView;
    private EditText mCardnumEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;

    Disposable mAuthSubscription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginFaceImageView = (ImageView) findViewById(R.id.login_face);
        mCardnumEditText = (EditText) findViewById(R.id.cardnum_edit);
        mPasswordEditText = (EditText) findViewById(R.id.password_edit);
        mLoginButton = (Button) findViewById(R.id.login_button);

        // 模仿Bilibili的变脸效果
        mCardnumEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mLoginFaceImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.icon_normal));
                }
            }
        });
        mPasswordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    mLoginFaceImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.icon_password));
                }
            }
        });

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginButton.setClickable(false);
                mLoginButton.setText("请稍候...");
                login(mCardnumEditText.getText().toString(), mPasswordEditText.getText().toString());
            }
        });


    }

    private void login(String cardnum, String password) {

        Log.d("login-cardnum", cardnum);
        Log.d("login-password", password);
        AuthService authService = new AuthService();
        authService.auth(cardnum, password).subscribe(new Observer<AuthService.AuthResult>() {
            @Override
            public void onSubscribe(Disposable d) {
                mAuthSubscription = d;
            }

            @Override
            public void onNext(AuthService.AuthResult authResult) {
                if (authResult.getSuccess()) {
                    Log.d("token", authResult.getResult());
                    SharedPreferences tokenSharedPreferences = getSharedPreferences("token", MODE_PRIVATE);
                    SharedPreferences.Editor tokenEditor = tokenSharedPreferences.edit();
                    tokenEditor.clear();
                    tokenEditor.putString("token", authResult.getResult());
                    tokenEditor.commit();
                    finish();
                } else {
                    mLoginButton.setClickable(true);
                    mLoginButton.setText("登录");
                    Log.e("login-fail", authResult.getReason());
                    Toast.makeText(LoginActivity.this, authResult.getReason(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    // 拦截返回键
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return false;
        } else {
            return true;
        }
    }
    public boolean onKeyUp(int keyCode, KeyEvent event){
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            return false;
        } else {
            return true;
        }
    }
}
