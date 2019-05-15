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


public class Login_weichat_alipay_Activity extends JKCBaseActivity {


    private ImageView sweepIV;
    private View rl_login;
    private long mDuration = 700;
    public AnimationSet animationSet;

    private String phone, vercode;

    private String globalRoaming = "+86";
    private String globalRoamingRegions = "CN";

    private final int TIME = 60;
    //倒计时
    private int recLen = TIME;
    private View weixi_login;


    private ParentViewPager pager_login;
    private ArrayList<View> mViewList;
    private RadioGroup radioGroup;
    private BroadcastReceiver mRecever;
    private View activity_wechat_capture;
    private TextView tx_tips;
    private View ib_closed;
    private View ali_login;
    private ImageView ali_sweepIV;
    private TextView ali_tx_tips;
    private String queryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI(this);
        setContentView(R.layout.sdk_login);

        initUI();

        getAuthorize();
        getAliAuthorize();

        startAnimation();

    }


    private void getAuthorize() {

        if (!NetStatus.getNetStatus(this)) {
            Utils.showToastCenter(this, "您的网络已断开，请检查网络！");
            tx_tips.setText("网络差，二维码加载失败");
            return;
        }

        RequestCenter.getAuthorize(DeviceUtil.getDeviceId2Ipad(this), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {

                try {
                    int status = ((JSONObject) responseObj).getInt("status");
                    if (status == 1) {
                        String authorizeUrl = ((JSONObject) responseObj).getString("data");
                        Bitmap bitmap = Utils.createQRImage(authorizeUrl);
                        if (bitmap != null) {
                            tx_tips.setText("请用微信扫码登录");
                            sweepIV.setImageBitmap(bitmap);
                        }
                    } else
                        Utils.showToastCenter(Login_weichat_alipay_Activity.this, "网络差，二维码加载失败");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Object reasonObj) {
                Utils.showToastCenter(Login_weichat_alipay_Activity.this, "网络差，二维码加载失败");
            }
        });

    }

    private void getAliAuthorize() {

        if (!NetStatus.getNetStatus(this)) {
            Utils.showToastCenter(this, "您的网络已断开，请检查网络！");
            ali_tx_tips.setText("网络差，二维码加载失败");
            return;
        }

        RequestCenter.getAliAuthorize( new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {

                try {
                    int status = ((JSONObject) responseObj).getInt("status");
                    if (status == 1) {

                        JSONObject body = ((JSONObject) responseObj).getJSONObject("data");

                        queryCode = body.getString("queryCode");

                        SDKManager.getInstance().runAliLoginScheduledTask(queryCode);

                        String authorizeUrl = body.getString("qrcode");
                        Bitmap bitmap = Utils.createQRImage(authorizeUrl);
                        if (bitmap != null) {
                            ali_tx_tips.setText("请用支付宝扫码登录");
                            ali_sweepIV.setImageBitmap(bitmap);
                        }
                    } else
                        Utils.showToastCenter(Login_weichat_alipay_Activity.this, "网络差，二维码加载失败");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Object reasonObj) {
                Utils.showToastCenter(Login_weichat_alipay_Activity.this, "网络差，二维码加载失败");
            }
        });

    }

    private void initUI() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(IConstants.LOGIN);
        filter.addAction(IConstants.LOGIN_BIND_TEL);
        mRecever = new MyRecever();
        this.registerReceiver(mRecever, filter);


        activity_wechat_capture = findViewById(R.id.activity_wechat_capture);
        activity_wechat_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
            }
        });

        rl_login = findViewById(R.id.rl_login);
        ib_closed = findViewById(R.id.ib_closed);
        ib_closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        weixi_login = View.inflate(this, R.layout.sdk_pager_weixin_login, null);
        sweepIV = (ImageView) weixi_login.findViewById(R.id.sweepIV);
        tx_tips = (TextView) weixi_login.findViewById(R.id.tx_tips);

        ali_login = View.inflate(this, R.layout.sdk_pager_alipay_login, null);
        ali_sweepIV = (ImageView) ali_login.findViewById(R.id.ali_sweepIV);
        ali_tx_tips = (TextView) ali_login.findViewById(R.id.ali_tx_tips);





        pager_login = (ParentViewPager) findViewById(R.id.pager_login);
        pager_login.setScroll(true);

        mViewList = new ArrayList<View>();

        mViewList.add(ali_login);
        mViewList.add(weixi_login);
        PagerAdapter adapter = new mAdapter(mViewList);

        pager_login.setAdapter(adapter);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rb_ali) {
                    pager_login.setCurrentItem(0);

                } else if (checkedId == R.id.rb_weixin) {
                    pager_login.setCurrentItem(1);

                }
            }
        });

        radioGroup.check(R.id.rb_weixin);

//        SDKManager.getInstance().runLoginScheduledTask(DeviceUtil.getDeviceId2Ipad(this));


    }


    public class MyRecever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (IConstants.LOGIN.equals(intent.getAction())) {
                Utils.showToastCenter(Login_weichat_alipay_Activity.this, "登录成功");

                finish();

            }else if(IConstants.LOGIN_BIND_TEL.equals(intent.getAction())){
                String openId = intent.getStringExtra("openId");
                String token = intent.getStringExtra("token");
                SDKManager.getInstance().startBindPage(token,openId);
                finish();
            }
        }
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

        rl_login.startAnimation(animationSet);
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(mRecever);
        SDKManager.getInstance().cancleScheduledTask();
        dismissProgress();
        onDetachedFromWindow();
        super.onDestroy();
    }


    class mAdapter extends PagerAdapter {

        ArrayList<View> mViewList;

        public mAdapter(ArrayList<View> mViewList) {
            this.mViewList = mViewList;
        }

        @Override
        public int getCount() {
            if (mViewList != null) {
                return mViewList.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mViewList.get(position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO
            container.removeView((View) object);
        }
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
