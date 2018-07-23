package sdk.hecaibao.com.hcb_sdk.http;

import com.hcb.hcbsdk.manager.SDKManager;
import com.hcb.hcbsdk.okhttp.client.CommonOkHttpClient;
import com.hcb.hcbsdk.okhttp.listener.DisposeDataHandle;
import com.hcb.hcbsdk.okhttp.listener.DisposeDataListener;
import com.hcb.hcbsdk.okhttp.request.CommonRequest;
import com.hcb.hcbsdk.okhttp.request.RequestParams;

/**
 * <br/>
 * <br/>
 * 时间：2017/9/25
 */
public class TestRequestCenter {

    //根据参数发送所有post请求
//    public static void postRequest(String url, RequestParams params,
//                                   DisposeDataListener listener) {
//        CommonOkHttpClient.request(CommonRequest.
//                createPostRequest(SDKManager.API_URL+url, params), new DisposeDataHandle(listener));
//    }
//    public static void getRequest(String url, RequestParams params,
//                                  DisposeDataListener listener) {
//        CommonOkHttpClient.request(CommonRequest.
//                createGetRequest(url, params), new DisposeDataHandle(listener));
//    }

    /**
     *
     */
    public static void getDMcode(String code, DisposeDataListener listener) {

        RequestParams params = new RequestParams();
        params.put("t", code);
        params.put("m", "8");
        params.put("e", "8");
//        TestRequestCenter.getRequest("http://www.efittech.com/dm.aspx", params, listener);
    }
}
