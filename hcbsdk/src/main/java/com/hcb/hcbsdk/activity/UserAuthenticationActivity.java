package com.hcb.hcbsdk.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.hcb.hcbsdk.R;
import com.hcb.hcbsdk.okhttp.exception.OkHttpException;
import com.hcb.hcbsdk.okhttp.listener.DisposeDataListener;
import com.hcb.hcbsdk.okhttp.request.RequestCenter;
import com.hcb.hcbsdk.service.msgBean.LoginReslut;
import com.hcb.hcbsdk.util.CheckIdCard;
import com.hcb.hcbsdk.util.L;
import com.hcb.hcbsdk.util.PermissionUtils;
import com.hcb.hcbsdk.util.StringUtils;
import com.hcb.hcbsdk.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UserAuthenticationActivity extends JKCBaseActivity {

    private ImageButton ib_closed;
    private EditText et_vercode, et_name;
    private Button rb_cancel, rb_confim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI(this);
        setContentView(R.layout.sdk_user_authentication);

        initUI();

    }


    private void initUI() {
        et_vercode = (EditText) findViewById(R.id.et_vercode);
        et_vercode.setKeyListener(new NumberKeyListener() {
            @Override
            public int getInputType() {
                return android.text.InputType.TYPE_CLASS_PHONE;
            }

            @Override
            protected char[] getAcceptedChars() {
                char[] numberChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'X'};
                return numberChars;
            }
        });
        et_name = (EditText) findViewById(R.id.et_name);
        rb_cancel = (Button) findViewById(R.id.rb_cancel);
        rb_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rb_confim = (Button) findViewById(R.id.rb_confim);
        rb_confim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = et_name.getText().toString();
                String code = et_vercode.getText().toString();

                if (!Utils.matchRegular(name)) {
                    Utils.showToastCenter(UserAuthenticationActivity.this, "姓名存在非法字符！！");
                    return;
                }

                if (StringUtils.isEmpty(name)) {
                    Utils.showToastCenter(UserAuthenticationActivity.this, "请填写真实姓名！！");
                    return;
                }
                if (StringUtils.isEmpty(code)) {
                    Utils.showToastCenter(UserAuthenticationActivity.this, "请填写身份证号！！");
                    return;
                } else {
                    if (!CheckIdCard.IDCardValidate(code)) {
                        Utils.showToastCenter(UserAuthenticationActivity.this, CheckIdCard.errorInfo + "！");
                        return;
                    }
                }
                showProgress("认证中...");
                auUser(name, code);
            }
        });


    }

    private void auUser(String name, String code) {

        RequestCenter.auUser(name, code, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {

                dismissProgress();

                int status = 0;
                try {
                    status = ((JSONObject) responseObj).getInt("status");
                    if (status == 1) {
                        Utils.showToastCenter(UserAuthenticationActivity.this, "认证成功");
                        finish();
                    } else
                        Utils.showToastCenter(UserAuthenticationActivity.this, "认证失败");
                } catch (JSONException e) {


                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                dismissProgress();
                Utils.showToastCenter(UserAuthenticationActivity.this, ((OkHttpException) reasonObj).getMsg().toString());
            }
        });

    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus)
            hideSystemUI(this);
        super.onWindowFocusChanged(hasFocus);
    }


    public void hideSystemUI(Activity activity) {
        final View decorView = activity.getWindow().getDecorView();
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(flags);
    }


}
