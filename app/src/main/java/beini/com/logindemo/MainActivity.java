package beini.com.logindemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity implements View.OnClickListener {

    //
    private Button btnQq;

    // QQ登录
    private Tencent mTencent;
   private TextView txt_show_info;


    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        btnQq = (Button) findViewById(R.id.btn_qq);
        Button btnWeixin = (Button) findViewById(R.id.btn_weixin);
        Button btnWeibo = (Button) findViewById(R.id.btn_weibo);
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        txt_show_info= (TextView) findViewById(R.id.txt_show_info);

        btnQq.setOnClickListener(this);
        btnWeixin.setOnClickListener(this);
        btnWeibo.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                loginClick();
                break;
            case R.id.btn_qq:
                qqLoginClick();
                break;
            case R.id.btn_weixin:
                weixinLoginClick();
                break;
            case R.id.btn_weibo:
                weiboLogin();
                break;

        }
    }

    /**
     * 登陆
     */
    private void loginClick() {
        startActivity(new Intent(MainActivity.this, LoginSuccessActivity.class));
    }

    /**
     * 微博登陆
     */
    private void weiboLogin() {

    }

    /**
     * 微信登陆
     */
    private void weixinLoginClick() {

    }

    IUiListener listener;

    /**
     * qq登陆
     */
    private void qqLoginClick() {
        if(mTencent==null){
            mTencent = Tencent.createInstance(getString(R.string.appId_qq), this.getApplicationContext());
        }

        // 登录
        if (!mTencent.isSessionValid()) {
            // 实例化回调接口
            listener = new BaseUiListener();
            mTencent.login(this, "all", listener);
        } else {
            // 注销登录
            mTencent.logout(this);
        }

    }


    /**
     * 调用SDK封装好的借口，需要传入回调的实例 会返回服务器的消息
     */
    private class BaseUiListener implements IUiListener {
        /**
         * 成功
         */
        @Override
        public void onComplete(Object response) {
            doComplete((JSONObject) response);
        }

        /**
         * 处理返回的消息 比如把json转换为对象什么的
         *
         * @param values
         */
        protected void doComplete(JSONObject values) {
            try {
                if (values.getInt("ret")==0) {
                    startActivity(new Intent(MainActivity.this, LoginSuccessActivity.class));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError e) {
            Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(MainActivity.this, "cancel", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, listener);
    }

    @Override
    protected void onDestroy() {
        if (mTencent != null) {
            //注销登录
            mTencent.logout(MainActivity.this);
        }
        super.onDestroy();
    }
}
