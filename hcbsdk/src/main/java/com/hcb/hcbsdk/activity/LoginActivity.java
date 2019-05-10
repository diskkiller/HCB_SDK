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
import android.support.annotation.NonNull;
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
import com.hcb.hcbsdk.util.PermissionUtils;
import com.hcb.hcbsdk.util.StringUtils;
import com.hcb.hcbsdk.util.Utils;
import com.hcb.hcbsdk.util.dodo.FileUtil;
import com.hcb.hcbsdk.util.dodo.NetStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.hcb.hcbsdk.util.C.KEY_DIR_NAME;
import static com.hcb.hcbsdk.util.C.KEY_FILE_NAME;
import static com.hcb.hcbsdk.util.C.key;


public class LoginActivity extends JKCBaseActivity {


    private ImageView sweepIV;
    private View rl_login;
    private long mDuration = 700;
    public AnimationSet animationSet;
    private Button bt_send;
    private EditText et_vercode;
    private EditText et_phone;

    private String phone, vercode;

    private String globalRoaming = "+86";
    private String globalRoamingRegions = "CN";

    private final int TIME = 60;
    //倒计时
    private int recLen = TIME;
    private TextInputLayout tel_input_layout, vecode_input_layout;
    private View weixi_login, tel_login;


    private ParentViewPager pager_login;
    private ArrayList<View> mViewList;
    private RadioGroup radioGroup;
    private View mBtn_login;
    private BroadcastReceiver mRecever;
    private View activity_wechat_capture;
    private TextView tx_tips, link_signup;
    private View ib_closed;

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
                        Utils.showToastCenter(LoginActivity.this, "网络差，二维码加载失败");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Object reasonObj) {
                Utils.showToastCenter(LoginActivity.this, "网络差，二维码加载失败");
            }
        });

    }

    private void initUI() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(IConstants.LOGIN);
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

        tel_login = View.inflate(this, R.layout.sdk_pager_tel_login, null);
        bt_send = (Button) tel_login.findViewById(R.id.bt_send);
        et_vercode = (EditText) tel_login.findViewById(R.id.et_vercode);
        et_phone = (EditText) tel_login.findViewById(R.id.et_phone);
        tx_tips = (TextView) weixi_login.findViewById(R.id.tx_tips);


        mBtn_login = tel_login.findViewById(R.id.btn_login);
        mBtn_login.setClickable(false);
        mBtn_login.setEnabled(false);
        mBtn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!NetStatus.getNetStatus(LoginActivity.this)) {
                    Utils.showToastCenter(LoginActivity.this, "您的网络已断开，请检查网络！");
                    return;
                }
                showProgress("登陆中...");
                attemptLogin();
            }
        });

        link_signup = (TextView) tel_login.findViewById(R.id.link_signup);
        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SDKManager.getInstance().startAboutPage(LoginActivity.this);
            }
        });

        tel_input_layout = (TextInputLayout) tel_login.findViewById(R.id.tel_input_layout);
        vecode_input_layout = (TextInputLayout) tel_login.findViewById(R.id.vecode_input_layout);

        pager_login = (ParentViewPager) findViewById(R.id.pager_login);
        pager_login.setScroll(true);

        mViewList = new ArrayList<View>();

        mViewList.add(tel_login);
        mViewList.add(weixi_login);
        PagerAdapter adapter = new mAdapter(mViewList);

        pager_login.setAdapter(adapter);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.rb_tel) {
                    pager_login.setCurrentItem(0);

                } else if (checkedId == R.id.rb_weixin) {
                    pager_login.setCurrentItem(1);

                }
            }
        });

        radioGroup.check(R.id.rb_weixin);

        SDKManager.getInstance().runLoginScheduledTask(DeviceUtil.getDeviceId2Ipad(this));


        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!NetStatus.getNetStatus(LoginActivity.this)) {
                    Utils.showToastCenter(LoginActivity.this, "您的网络已断开，请检查网络！");
                    return;
                }

                phone = et_phone.getText().toString();
                if (registVerify()) {
                    tel_input_layout.setErrorEnabled(false);
                    et_vercode.setFocusable(true);
                    et_vercode.setFocusableInTouchMode(true);
                    et_vercode.requestFocus();
                    getVercode();

                } else {
                    tel_input_layout.setErrorEnabled(true);
                    tel_input_layout.setError("请输入正确的手机号");
                }
            }
        });

        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                et_phone.setSelection(et_phone.getText().toString().length());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                phone = s.toString();

                if (phone.length() == 11) {
                    if (registVerify()) {
                        tel_input_layout.setErrorEnabled(false);
                    } else {
                        tel_input_layout.setErrorEnabled(true);
                        tel_input_layout.setError("请输入正确的手机号");

                    }
                }
                vercode = et_vercode.getText().toString();
                if (registVerify() && vercode.length() == 6) {
                    mBtn_login.setClickable(true);
                    mBtn_login.setEnabled(true);
                    mBtn_login.setBackgroundResource(R.drawable.bt_confim_bg);
                } else {
                    mBtn_login.setClickable(false);
                    mBtn_login.setEnabled(false);
                    mBtn_login.setBackgroundResource(R.drawable.bt_default_bg);
                }

            }
        });
        et_vercode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                et_vercode.setSelection(et_vercode.getText().toString().length());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String trimStr = s.toString();

                if (s.length() > 6) {
                    vecode_input_layout.setErrorEnabled(true);
                    vecode_input_layout.setError("验证码长度不能超过6个");
                } else {
                    vecode_input_layout.setErrorEnabled(false);
                    vercode = trimStr;
                    phone = et_phone.getText().toString();
                    if (registVerify() && vercode.length() == 6) {
                        mBtn_login.setClickable(true);
                        mBtn_login.setEnabled(true);
                        mBtn_login.setBackgroundResource(R.drawable.bt_confim_bg);
                    } else {
                        mBtn_login.setClickable(false);
                        mBtn_login.setEnabled(false);
                        mBtn_login.setBackgroundResource(R.drawable.bt_default_bg);
                    }

                }

            }
        });


    }

    private void getVercode() {
        RequestCenter.getVercode(phone, "6", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {

                try {

//                    AESUtil.encrypt("AES");

                    int status = ((JSONObject) responseObj).getInt("status");
                    if (status == 1) {
                        startTimer();
                        Utils.showToastCenter(LoginActivity.this, "验证码已发送");
                    } else
                        Utils.showToastCenter(LoginActivity.this, ((JSONObject) responseObj).getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Object reasonObj) {
                Utils.showToastCenter(LoginActivity.this, "验证码发送失败");
            }
        });
    }

    public class MyRecever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (IConstants.LOGIN.equals(intent.getAction())) {
                Utils.showToastCenter(LoginActivity.this, "登录成功");

                /*if(StringUtils.isEmpty(SDKManager.getInstance().getUser().getIdCard())){
                    SDKManager.getInstance().startUserAuPage(LoginActivity.this);
                }*/

                finish();

            }
        }
    }


    private void attemptLogin() {

        RequestCenter.userLogin(phone, et_vercode.getText().toString(), DeviceUtil.getDeviceId2Ipad(this), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {

                dismissProgress();
                try {
                    int status = ((JSONObject) responseObj).getInt("status");
                    String data = ((JSONObject) responseObj).toString();
                    LoginReslut loginReslut = new Gson().fromJson(data, LoginReslut.class);
                    if (status == 1) {

                        FileUtil.writeFile(FileUtil.getSDDir(KEY_DIR_NAME) + KEY_FILE_NAME, data, false);

                        TuitaData.getInstance().setUser(loginReslut.getData());

                        BroadcastUtil.sendBroadcastToUI(LoginActivity.this, IConstants.LOGIN, ((JSONObject) responseObj).toString());
                    } else
                        Utils.showToastCenter(LoginActivity.this, "登录失败");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Object reasonObj) {
                try {

                    dismissProgress();


                    Utils.showToastCenter(LoginActivity.this, ((OkHttpException) reasonObj).getMsg().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus)
            hideSystemUI(this);
        super.onWindowFocusChanged(hasFocus);
    }

    private boolean registVerify() {
        boolean isValid = false;
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber swissNumberProto = phoneUtil.parse(globalRoaming + phone,
                    globalRoamingRegions);
            isValid = phoneUtil.isValidNumber(swissNumberProto); // returns true
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }

        if (phone == null || "".equals(phone) || !isValid) {
            return false;
        }
        return true;
    }

    private void startTimer() {
        recLen = TIME;
        bt_send.setClickable(false);
        bt_send.setEnabled(false);
        Message message = handler.obtainMessage(1);     // Message
        handler.sendMessageDelayed(message, 1000);
    }

    final Handler handler = new Handler() {

        public void handleMessage(Message msg) {         // handle message
            switch (msg.what) {
                case 1:
                    recLen--;
                    bt_send.setText(recLen + "");
                    bt_send.setClickable(false);
                    bt_send.setEnabled(false);
                    if (recLen > 0) {
                        Message message = handler.obtainMessage(1);
                        handler.sendMessageDelayed(message, 1000);      // send message
                    } else {
                        bt_send.setTextColor(Color.WHITE);
                        bt_send.setText(" 再次获取验证码 ");
                        bt_send.setClickable(true);
                        bt_send.setEnabled(true);
                    }
            }

            super.handleMessage(msg);
        }
    };


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
        Utils.closeKeybord(et_vercode, this);
        Utils.closeKeybord(et_phone, this);
        handler.removeCallbacksAndMessages(null);
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
