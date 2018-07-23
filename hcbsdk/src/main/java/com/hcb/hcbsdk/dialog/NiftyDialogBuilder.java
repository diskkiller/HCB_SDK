package com.hcb.hcbsdk.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hcb.hcbsdk.R;


/*
 * Copyright 2014 litao
 * https://github.com/sd6352051/NiftyDialogEffects
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class NiftyDialogBuilder extends Dialog implements DialogInterface {

    private final String defTextColor = "#FFFFFFFF";

    private final String defDividerColor = "#11000000";

    private final String defMsgColor = "#FFFFFFFF";

    private final String defDialogColor = "#FFE74C3C";


    private static Context tmpContext;


    private Effectstype type = null;

    private LinearLayout mLinearLayoutView;

    private RelativeLayout mRelativeLayoutView;

    private LinearLayout mLinearLayoutMsgView;

    private LinearLayout mLinearLayoutTopView;

    private FrameLayout mFrameLayoutCustomView;

    private View mDialogView;

    private View mDivider;

    private TextView mTitle;

    private TextView mMessage;

    private ImageView mIcon;

    private Button mButton1;

    private Button mButton2;

    private int mDuration = -1;

    private static int mOrientation = 1;

    private boolean isCancelable = true;

    private static NiftyDialogBuilder instance;

    public NiftyDialogBuilder(Context context) {
        super(context);
        init(context);

    }

    public NiftyDialogBuilder(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

    }

    public static NiftyDialogBuilder getInstance(Context context) {

        return new NiftyDialogBuilder(context, R.style.dialog_untran);

    }

    private void init(Context context) {

        mDialogView = View.inflate(context, R.layout.dialog_layout, null);

        mLinearLayoutView = (LinearLayout) mDialogView.findViewById(R.id.parentPanel);

        setContentView(mDialogView);

        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {

                mLinearLayoutView.setVisibility(View.VISIBLE);
                if (type == null) {
                    type = Effectstype.Fall;
                }
                start(type);


            }
        });
    }


    public NiftyDialogBuilder setCustomView(int resid, Context context) {

        View view = View.inflate(context, resid, null);

        if (mLinearLayoutView.getChildCount() > 0) {
            mLinearLayoutView.removeAllViews();
        }
        mLinearLayoutView.addView(view);
        mLinearLayoutView.setVisibility(View.VISIBLE);
        return this;
    }

    public NiftyDialogBuilder withEffect(Effectstype type) {
        this.type = type;
        return this;
    }


    @Override
    public void show() {
        super.show();
    }

    private void start(Effectstype type) {
        BaseEffects animator = type.getAnimator();
        if (mDuration != -1) {
            animator.setDuration(Math.abs(mDuration));
        }
        animator.start(mDialogView);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
