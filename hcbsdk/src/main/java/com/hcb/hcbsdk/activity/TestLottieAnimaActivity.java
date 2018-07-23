package com.hcb.hcbsdk.activity;

import android.animation.Animator;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hcb.hcbsdk.R;
import com.hcb.hcbsdk.activity.widget.dialog.NormalDialog;
import com.hcb.hcbsdk.util.L;
import com.hcb.hcbsdk.util.StringUtils;
import com.hcb.hcbsdk.util.Utils;

/**
 * Create by WangGuoWei on 2018/7/4
 */
public class TestLottieAnimaActivity extends JKCBaseActivity {
    private View rl_weixin_pay,ib_closed;
    private AnimationSet animationSet;
    private View typeLayOut;
    private NormalDialog dialog;
    private RadioGroup rgChoose;
    private RadioButton edt;
    private EditText etCustom;
    private String rechargeNum;
    private TextInputLayout gold_input_layout;
    private Button btnSure;
    private Button btnCancle;

//    private LottieAnimationView lottieView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sdk_recharge_gold);
        dialog = new NormalDialog(this);
        rl_weixin_pay = findViewById(R.id.recharge_gold);
        rgChoose = (RadioGroup) findViewById(R.id.rg_recharge_gold_choose);




//        ib_closed = findViewById(R.id.ib_closed);
        startAnimation();
        initRadioGroup();

    }



    private void initRadioGroup() {
        rgChoose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(edt!=null)
                    startNormalAnimation(edt);

                 edt = (RadioButton) findViewById(checkedId);

                if (edt != null) {
                    startChooseAnimation(edt);

                    if(checkedId == R.id.rb_recharge_gold5){

                        showDialog();

                    }

                }
            }
        });
    }


    private void showDialog() {
        typeLayOut = View.inflate(this, R.layout
                .sdk_dialog_recharge_gold, null);
        typeLayOut.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                .MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));


        gold_input_layout = (TextInputLayout)typeLayOut.findViewById(R.id.gold_input_layout);
        etCustom = (EditText) typeLayOut.findViewById(R.id.et_recharge_gold_custom);
        etCustom.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                    if (!L.debug || Integer.parseInt(rechargeNum) % 100 != 0) {
                        gold_input_layout.setErrorEnabled(true);
                        gold_input_layout.setError("请输入正确的金额!");
                    } else {
                        gold_input_layout.setErrorEnabled(false);
                    }
                }

            }
        });


        btnSure = (Button) typeLayOut.findViewById(R.id.btn_recharge_gold_sure);
        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rechargeNum.equals("") || !L.debug && Integer.parseInt(rechargeNum) < 100 || Integer.parseInt(rechargeNum) % 100 != 0) {
                    Utils.showToastCenter(TestLottieAnimaActivity.this, "请输入正确的金额！");
                } else {
                    if(edt!=null)
                        startNormalAnimation(edt);
                    rgChoose.check(-1);
                    rechargeNum = "";
                    dialog.dismiss();

                }
            }
        });
        btnCancle = (Button) typeLayOut.findViewById(R.id.btn_charge_gold_cancel);
        btnCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt!=null)
                    startNormalAnimation(edt);
                rgChoose.check(-1);
                rechargeNum = "";
                dialog.dismiss();
            }
        });


        dialog.contentView = typeLayOut;
        dialog.widthScale(0.5f);
        dialog.isKongBai = false;
        dialog.btnNum(0)
                .cornerRadius(15)
                .isTitleShow(false)
                .show();
    }

    private long mDuration = 1000;

    private void startAnimation() {
        animationSet = new AnimationSet(true);
        Animation alpha = new AlphaAnimation( 0, 1);
        alpha.setDuration(mDuration * 3 / 2);
        Animation scale = new ScaleAnimation((float) 0.5, 1, (float) 0.5,1,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(mDuration);

        animationSet.addAnimation(alpha);
        animationSet.addAnimation(scale);

        animationSet.setFillAfter(true);

        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//                ib_closed.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        rl_weixin_pay.startAnimation(animationSet);
    }



    private void startChooseAnimation(View view) {
        animationSet = new AnimationSet(true);
        Animation scale = new ScaleAnimation(1, (float) 1.1, 1,(float) 1.1,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(200);

        animationSet.addAnimation(scale);

        animationSet.setFillAfter(true);


        view.startAnimation(animationSet);
    }
    private void startNormalAnimation(View view) {
        animationSet = new AnimationSet(true);
        Animation scale = new ScaleAnimation((float) 1.1, 1, (float) 1.1,1,Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        scale.setDuration(200);

        animationSet.addAnimation(scale);

        animationSet.setFillAfter(true);


        view.startAnimation(animationSet);
    }

}
