package com.hcb.hcbsdk.constant;

public class SDKConstant {



    //毫秒单位
    public static int MILLION_UNIT = 1000;

    //自动播放阈值
    public static int VIDEO_SCREEN_PERCENT = 50;

    public static float VIDEO_HEIGHT_PERCENT = 9 / 16.0f;
    //自动播放条件
    public enum AutoPlaySetting {
        // wifi状态下才可以自动播放
        AUTO_PLAY_ONLY_WIFI,
        // 3G、4G、wifi都可以自动播放
        AUTO_PLAY_3G_4G_WIFI,
        // 不允许自动播放
        AUTO_PLAY_NEVER
    }

    //素材类型
    public static final String MATERIAL_IMAGE = "image";
    public static final String MATERIAL_HTML = "html";


}
