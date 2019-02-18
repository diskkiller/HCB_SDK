package com.hcb.hcbsdk.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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


public class HuoDong_Activity extends JKCBaseActivity {


    private long mDuration = 700;
    public AnimationSet animationSet;
    private View ib_closed,rl_bg,ib_lingqu;
    private MyRecever mRecever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI(this);
        setContentView(R.layout.sdk_huodong);


        initRecever();

        initUI();


        startAnimation();

    }


    private void initRecever() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(IConstants.PINTU_GIVE_SUCCESS);
        mRecever = new MyRecever();
        this.registerReceiver(mRecever,filter);

    }
    public class MyRecever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
         if (IConstants.PAY_FAIL.equals(intent.getAction())) {
             dismissProgress();
             Utils.showToastCenter(HuoDong_Activity.this, "订单异常，请重新下单");
         }else if (IConstants.PINTU_GIVE_SUCCESS.equals(intent.getAction())) {
             dismissProgress();
             Utils.showToastCenter(HuoDong_Activity.this, "彩票领取成功！请到个人中心兑奖！");
         }

        }
    }


    private void initUI() {
        rl_bg = findViewById(R.id.rl_bg);
        ib_closed = findViewById(R.id.ib_closed);
        ib_closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ib_lingqu = findViewById(R.id.ib_lingqu);
        ib_lingqu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(null);
                SDKManager.getInstance().give_caipiao("TICKET_TWO","20190115");
            }
        });





    }





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

        rl_bg.startAnimation(animationSet);
    }


    @Override
    protected void onDestroy() {
        this.unregisterReceiver(mRecever);
        dismissProgress();
        onDetachedFromWindow();
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
