package com.hcb.hcbsdk.okhttp.response;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.hcb.hcbsdk.logutils.save.imp.LogWriter;
import com.hcb.hcbsdk.okhttp.exception.OkHttpException;
import com.hcb.hcbsdk.okhttp.listener.DisposeDataHandle;
import com.hcb.hcbsdk.okhttp.listener.DisposeDataListener;
import com.hcb.hcbsdk.util.ResponseEntityToModule;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.Response;

/**
 * 处理服务器返回的json数据
 * 时间：2017/9/17
 */
public class CommonJsonCallback implements Callback {


    protected final String RESULT_CODE = "status";
    protected final int RESULT_CODE_VALUE = 10000;
    protected final String ERROR_MSG = "emsg";
    protected final String EMPTY_MSG = "";
    protected final String COOKIE_STORE = "Set-Cookie";

    // 网络错误
    protected final int NETWORK_ERROR = -1; // the network relative error
    // json错误
    protected final int JSON_ERROR = -2; // the JSON relative error
    // 其它错误
    protected final int OTHER_ERROR = -3; // the unknow error

    /**
     * 将其它线程的数据转发到UI线程
     */
    private Handler mDeliveryHandler;
    private DisposeDataListener mListener;
    private Class<?> mClass;

    public CommonJsonCallback(DisposeDataHandle handle) {
        mListener = handle.mListener;
        mClass = handle.mClass;
        mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(NETWORK_ERROR, e));
            }
        });
    }

    @Override
    public void onResponse(final Call call, final Response response) throws IOException {
        final String result = response.body().string();
        LogWriter.writeLog("PushService", "服务器返回数据----url:" + call.request().url()+"  data:"+result.toString());
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                HandleResponse(result,call.request().url());
            }
        });
    }

    /**
     * 处理返回结果
     *
     * @param result
     *         返回结果
     */
   /* private void HandleResponse(String result) {
        if (TextUtils.isEmpty(result)) {
            mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.has(RESULT_CODE)) {
                // 取出响应码，如果响应码为0，说明响应成功
                if (jsonObject.optInt(RESULT_CODE) == RESULT_CODE_VALUE) {
                    if (mClass == null) {
                        // 不需要将json解析成实体数据
                        mListener.onSuccess(jsonObject);
                    } else {
                        // 需要将json解析成实体数据
                        Object obj = ResponseEntityToModule.
                                parseJsonObjectToModule(jsonObject, mClass);
                        if (obj == null) {
                            // 解析失败，json格式有错
                            mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                        } else {
                            // 解析成功，返回解析后的实体数据
                            mListener.onSuccess(obj);
                        }
                    }
                } else {
                    // 响应码不为0
                    mListener.onFailure(new OkHttpException(OTHER_ERROR,
                            jsonObject.optInt(RESULT_CODE)));
                }
            } else {
                if (jsonObject.has(ERROR_MSG)) {
                    mListener.onFailure(new OkHttpException(OTHER_ERROR,
                            jsonObject.optString(ERROR_MSG)));
                }
            }
        } catch (Exception e) {
            mListener.onFailure(new OkHttpException(OTHER_ERROR, e.getMessage()));
        }
    }*/



    /**
     * 处理返回结果
     *
     * @param result
     *         返回结果
     * @param url
     */
    private void HandleResponse(String result, HttpUrl url) {
        if (TextUtils.isEmpty(result)) {
            mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(result);
            if (jsonObject.has(RESULT_CODE)) {
                // 取出响应码，如果响应码为0，说明响应成功
                if (jsonObject.optInt(RESULT_CODE) == RESULT_CODE_VALUE) {
                    if (mClass == null) {
                        /*if(url.toString().contains("user/login")){
                            L.info("PushService", "写入user数据----url:" + url.toString()+"  data:"+result.toString());
                            FileUtil.writeFile(FileUtil.getSDDir("sdk_user") + "/sdk_user.txt",result, false);
                        }*/
                        // 不需要将json解析成实体数据
                        mListener.onSuccess(jsonObject);
                    } else {
                        // 需要将json解析成实体数据
                        Object obj = ResponseEntityToModule.
                                parseJsonObjectToModule(jsonObject, mClass);
                        if (obj == null) {
                            // 解析失败，json格式有错
                            mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                        } else {
                            // 解析成功，返回解析后的实体数据
                            mListener.onSuccess(obj);
                        }
                    }
                } else {
                    // 响应码不为0
                    mListener.onFailure(jsonObject);
                }
            } else {
                if (jsonObject.has(ERROR_MSG)) {
                    mListener.onFailure(new OkHttpException(OTHER_ERROR,
                            jsonObject.optString(ERROR_MSG)));
                }
            }
        } catch (Exception e) {
            LogWriter.writeLog("PushService", "服务器返回数据----"+e.getMessage()+"  "+e.toString());
            mListener.onFailure(new OkHttpException(OTHER_ERROR, e.getMessage()));
        }
    }
}
