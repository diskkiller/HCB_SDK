package com.hcb.hcbsdk.activity.widget.dialog;//package cn.com.ujoin.ui.activity.view.dialog;
//
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.view.LayoutInflater;
//import android.view.View;
//
//import cn.com.jaaaja.R;
//import LoadingView;
//import fr.tvbarthel.lib.blurdialogfragment.BlurDialogFragment;
//
///**
// * Simple fragment with blur effect behind.
// */
//@TargetApi(Build.VERSION_CODES.HONEYCOMB)
//public class SampleDialogFragment extends BlurDialogFragment {
//
//    /**
//     * Bundle key used to start the blur dialog with a given scale factor (float).
//     */
//    private static final String BUNDLE_KEY_DOWN_SCALE_FACTOR = "bundle_key_down_scale_factor";
//
//    /**
//     * Bundle key used to start the blur dialog with a given blur radius (int).
//     */
//    private static final String BUNDLE_KEY_BLUR_RADIUS = "bundle_key_blur_radius";
//
//    /**
//     * Bundle key used to start the blur dialog with a given dimming effect policy.
//     */
//    private static final String BUNDLE_KEY_DIMMING = "bundle_key_dimming_effect";
//
//    /**
//     * Bundle key used to start the blur dialog with a given debug policy.
//     */
//    private static final String BUNDLE_KEY_DEBUG = "bundle_key_debug_effect";
//
//    private int mRadius;
//    private float mDownScaleFactor;
//    private boolean mDimming;
//    private boolean mDebug;
//
//    /**
//     * Retrieve a new instance of the sample fragment.
//     *
//     * @param radius          blur radius.
//     * @param downScaleFactor down scale factor.
//     * @param dimming         dimming effect.
//     * @param debug           debug policy.
//     * @return well instantiated fragment.
//     */
//    public static SampleDialogFragment newInstance(int radius,
//                                                   float downScaleFactor,
//                                                   boolean dimming,
//                                                   boolean debug) {
//        SampleDialogFragment fragment = new SampleDialogFragment();
//        Bundle args = new Bundle();
//        args.putInt(
//                BUNDLE_KEY_BLUR_RADIUS,
//                radius
//        );
//        args.putFloat(
//                BUNDLE_KEY_DOWN_SCALE_FACTOR,
//                downScaleFactor
//        );
//        args.putBoolean(
//                BUNDLE_KEY_DIMMING,
//                dimming
//        );
//        args.putBoolean(
//                BUNDLE_KEY_DEBUG,
//                debug
//        );
//
//        fragment.setArguments(args);
//
//        return fragment;
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//
//        Bundle args = getArguments();
//        mRadius = args.getInt(BUNDLE_KEY_BLUR_RADIUS);
//        mDownScaleFactor = args.getFloat(BUNDLE_KEY_DOWN_SCALE_FACTOR);
//        mDimming = args.getBoolean(BUNDLE_KEY_DIMMING);
//        mDebug = args.getBoolean(BUNDLE_KEY_DEBUG);
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        this.setCancelable(false);// 设置点击屏幕Dialog不消失
//        super.onCreate(savedInstanceState);
//    }
//    private Dialog mDialog;
//    private LoadingView mLoadingView;
//    private View mDialogContentView;
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        mDialog = new Dialog(getActivity(), R.style.custom_dialog);
//        mDialogContentView= LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog,null);
//
//
//        mLoadingView= (LoadingView) mDialogContentView.findViewById(R.id.loadView);
//        mDialog.setContentView(mDialogContentView);
//        mDialog.setCanceledOnTouchOutside(false);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        View view = getActivity().getLayoutInflater().inflate(R.layout.layout_dialog, null);
//        mLoadingView= (LoadingView) mDialogContentView.findViewById(R.id.loadView);
//        builder.setView(view);
//        builder.setCancelable(false);
//        return builder.create();
//
//
////        return mDialog;
//    }
//
//    public void setLoadingText(CharSequence charSequence){
//        mLoadingView.setLoadingText(charSequence);
//    }
//
//    @Override
//    protected boolean isDebugEnable() {
//        return mDebug;
//    }
//
//    @Override
//    protected boolean isDimmingEnable() {
//        return mDimming;
//    }
//
//    @Override
//    protected boolean isActionBarBlurred() {
//        return true;
//    }
//
//    @Override
//    protected float getDownScaleFactor() {
//        return mDownScaleFactor;
//    }
//
//    @Override
//    protected int getBlurRadius() {
//        return mRadius;
//    }
//}
