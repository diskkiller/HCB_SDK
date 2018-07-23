package com.hcb.hcbsdk.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hcb.hcbsdk.R;
import com.hcb.hcbsdk.manager.SDKManager;
import com.hcb.hcbsdk.okhttp.listener.DisposeDataListener;
import com.hcb.hcbsdk.okhttp.request.RequestCenter;
import com.hcb.hcbsdk.util.DeviceUtil;
import com.hcb.hcbsdk.util.L;
import com.hcb.hcbsdk.util.StringUtils;
import com.hcb.hcbsdk.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author WangGuoWei
 * @time 2018/4/13 16:34
 * @des ${TODO}
 * <p>
 * ┽
 * ┽                            _ooOoo_
 * ┽                           o8888888o
 * ┽                           88" . "88
 * ┽                           (| -_- |)
 * ┽                           O\  =  /O
 * ┽                        ____/`---'\____
 * ┽                      .'  \\|     |//  `.
 * ┽                     /  \\|||  :  |||//  \
 * ┽                    /  _||||| -:- |||||-  \
 * ┽                    |   | \\\  -  /// |   |
 * ┽                    | \_|  ''\---/''  |   |
 * ┽                    \  .-\__  `-`  ___/-. /
 * ┽                  ___`. .'  /--.--\  `. . __
 * ┽               ."" '<  `.___\_<|>_/___.'  >'"".
 * ┽              | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 * ┽              \  \ `-.   \_ __\ /__ _/   .-` /  /
 * ┽         ======`-.____`-.___\_____/___.-`____.-'======
 * ┽                            `=---='
 * ┽         ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * ┽                      佛祖保佑       永无BUG
 * ┽
 * ┽
 * ┽
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class RechargeGoldActivity extends JKCBaseActivity {


    RadioGroup rgChoose;
    EditText etCustom;
    Button btnSure,btnCancle;
    String rechargeNum = "";
    TextView tvNeedMoney;
    private View btnClose;
    private String notify = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        hideSystemUI(this);

        setContentView(R.layout.sdk_activity_recharge_gold);

        if(getIntent()!=null) {
            notify = getIntent().getStringExtra("payId");
        }

        initUI();
        initRadioGroup();

    }

    private void initUI() {

        btnClose = findViewById(R.id.btn_recharge_gold_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tvNeedMoney = (TextView) findViewById(R.id.tv_recharge_gold_need_money);


        rgChoose = (RadioGroup) findViewById(R.id.rg_recharge_gold_choose);

        etCustom = (EditText) findViewById(R.id.et_recharge_gold_custom);
        etCustom.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvNeedMoney.setVisibility(View.INVISIBLE);
                rgChoose.check(-1);
                rechargeNum = "";
            }
        });
        etCustom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                rechargeNum = etCustom.getText().toString().trim();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                rechargeNum = s.toString();

                if (!StringUtils.isEmpty(rechargeNum)) {
                    if (!L.debug&&Integer.parseInt(rechargeNum)<100||Integer.parseInt(rechargeNum)%100!=0) {
                        tvNeedMoney.setVisibility(View.INVISIBLE);
//                        Utils.showToastCenter(RechargeGoldActivity.this,"请输入正确的金额！");
                    }else{
                        tvNeedMoney.setVisibility(View.VISIBLE);
                        tvNeedMoney.setText("需支付 "+(double)(Integer.parseInt(rechargeNum)/100)+" 元");
                    }
                }

            }
        });


        btnSure = (Button) findViewById(R.id.btn_recharge_gold_sure);
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!L.debug&&Integer.parseInt(rechargeNum)<100||Integer.parseInt(rechargeNum)%100!=0) {
                    Utils.showToastCenter(RechargeGoldActivity.this,"请输入正确的金额！");
                }else{

                    charge();

                }
            }
        });
        btnCancle = (Button) findViewById(R.id.btn_charge_gold_cancel);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void charge() {


        if(SDKManager.getInstance().getUser()==null){
            Utils.showToastCenter(RechargeGoldActivity.this,"请先登录！");
            return;
        }


        RequestCenter.userRecharge(SDKManager.getInstance().getUser().getUid(), DeviceUtil.getDeviceId2Ipad(this), rechargeNum,notify, new DisposeDataListener() {


            @Override
            public void onSuccess(Object responseObj) {
                try {
                    String payId = "";
                    int status =((JSONObject)responseObj).getInt("status");
                    if(status == 10000){
                        payId = ((JSONObject)responseObj).getString("data");
                        SDKManager.getInstance().startPayPage(payId);
                        finish();
                    }
                    else{
                        Utils.showToastCenter(RechargeGoldActivity.this,"请求失败！");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Object reasonObj) {
                Utils.showToastCenter(RechargeGoldActivity.this,"请求失败！");
            }
        });
    }


    private void initRadioGroup() {
        rgChoose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton edt = (RadioButton) findViewById(checkedId);

                if(edt!=null){
                    etCustom.setText("");
                    rechargeNum = edt.getText().toString().replace("金豆","").trim();
                    tvNeedMoney.setVisibility(View.VISIBLE);
                    tvNeedMoney.setText("需支付 "+(double)(Integer.parseInt(rechargeNum)/100)+" 元");
                }
            }
        });
        rgChoose.check(R.id.rb_recharge_gold1);
    }
}
