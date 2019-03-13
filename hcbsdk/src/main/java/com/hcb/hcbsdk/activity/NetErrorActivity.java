package com.hcb.hcbsdk.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.hcb.hcbsdk.R;
import com.hcb.hcbsdk.manager.SDKManager;
import com.hcb.hcbsdk.okhttp.exception.OkHttpException;
import com.hcb.hcbsdk.okhttp.listener.DisposeDataListener;
import com.hcb.hcbsdk.okhttp.request.RequestCenter;
import com.hcb.hcbsdk.service.TuitaData;
import com.hcb.hcbsdk.service.msgBean.LoginReslut;
import com.hcb.hcbsdk.socketio.listener.IConstants;
import com.hcb.hcbsdk.util.BroadcastUtil;
import com.hcb.hcbsdk.util.DeviceUtil;
import com.hcb.hcbsdk.util.Utils;
import com.hcb.hcbsdk.util.dodo.FileUtil;
import com.hcb.hcbsdk.util.dodo.NetStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.hcb.hcbsdk.util.C.KEY_DIR_NAME;
import static com.hcb.hcbsdk.util.C.KEY_FILE_NAME;


public class NetErrorActivity extends JKCBaseActivity {


    private long mDuration = 700;
    public AnimationSet animationSet;
    private View rl_neterror;
    private View ib_closed;
    private View tv_setting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI(this);
        setContentView(R.layout.sdk_neterror);

        initUI();


        startAnimation();

    }



    private void initUI() {


        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);

        rl_neterror = findViewById(R.id.rl_neterror);
        tv_setting = findViewById(R.id.tv_setting);
        tv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT > 10) {
                    // 3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
                    startActivity(new Intent(
                            Settings.ACTION_WIFI_SETTINGS));
                    overridePendingTransition(R.anim.left_in,
                            R.anim.left_out);
                } else {
                    startActivity(new Intent(
                            android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                    overridePendingTransition(R.anim.left_in,
                            R.anim.left_out);
                }
            }
        });

        ib_closed = findViewById(R.id.ib_closed);
        ib_closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



    private ConnectivityManager connectivityManager;
    private NetworkInfo info;
    /**
     * 监听网络变化广播 做出相应的提示
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                connectivityManager = (ConnectivityManager) getApplication()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();
                if (info != null && info.isAvailable()) {
                    finish();
                }
            }
        }
    };



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus)
            hideSystemUI(this);
        super.onWindowFocusChanged(hasFocus);
    }





    private void startAnimation() {
        animationSet = new AnimationSet(true);
        Animation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(mDuration * 3 / 2);
        Animation scale = new ScaleAnimation(2, 1, 2, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(mDuration);

        animationSet.addAnimation(alpha);
        animationSet.addAnimation(scale);

        animationSet.setFillAfter(true);

        rl_neterror.startAnimation(animationSet);
    }


    @Override
    protected void onDestroy() {
        onDetachedFromWindow();
        if(mReceiver!=null)
            unregisterReceiver(mReceiver);
        super.onDestroy();
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
