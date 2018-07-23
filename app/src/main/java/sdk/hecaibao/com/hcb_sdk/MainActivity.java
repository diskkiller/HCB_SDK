package sdk.hecaibao.com.hcb_sdk;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.datamatrix.encoder.SymbolShapeHint;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.hcb.hcbsdk.manager.SDKManager;
import com.hcb.hcbsdk.okhttp.listener.DisposeDataListener;
import com.hcb.hcbsdk.socketio.listener.IConstants;

import java.util.ArrayList;
import java.util.HashMap;

import sdk.hecaibao.com.hcb_sdk.http.TestRequestCenter;
import sdk.hecaibao.com.hcb_sdk.utils.BarcodeUtils;


public class MainActivity extends Activity {

    private Toast mToast;
    private Button bt_startLoginpage,bt_startgoldpage,bt_startLog,bt_endLog;
    private TextView tx_double,tx_socket_status;
    private double hasPayNum = 129.98;
    private ImProgressMsgDialog progressDialog;
    private Handler mHandler = new Handler();
    private ImageView mSweepIV;
    private boolean isdebug = true;
    private MyRecever mRecever;
    private EditText et_deviceno;
    private String  deviceNo = "2aa5f1a4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        left_recycler_view = (RecyclerView) findViewById(R.id.left_recycler_view);
        modelList = new ArrayList<>();
        SDKManager.getInstance().init(MainActivity.this,"5aa66b95f3745e62ffd44a8e");
        bt_startLoginpage = (Button) findViewById(R.id.bt_startLoginpage);
        bt_startgoldpage = (Button)findViewById(R.id.bt_startgoldpage);
        bt_startLog = (Button)findViewById(R.id.bt_startLog);
        bt_endLog = (Button)findViewById(R.id.bt_endLog);
        tx_double = (TextView)findViewById(R.id.tx_double);
        mSweepIV = (ImageView) findViewById(com.hcb.hcbsdk.R.id.sweepIV);


        tx_socket_status = (TextView)findViewById(R.id.tx_socket_status);
        et_deviceno = (EditText)findViewById(R.id.et_deviceno);


        tx_double.setText(SDKManager.getInstance().getSocketURL());


//        Bitmap bitmap = createQRImage("35029234309580522280593353757");
        Bitmap bitmap = BarcodeUtils.createBarcode("35029234309580522280593353757", null,QR_WIDTH, QR_HEIGHT, BarcodeFormat.DATA_MATRIX, Color.BLACK, Color.WHITE);
        mSweepIV.setImageBitmap(bitmap);


        bt_startLoginpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                tx_double.setText(""+(((float) hasPayNum)*100));

//                SDKManager.getInstance().startRechargeGoldPage(MainActivity.this,null);
//                AppSocket.getInstance().sdk_logout();

                //                SDKManager.getInstance().runPayScheduledTask();
                //                test();
                SDKManager.getInstance().startPayPage("5abdf845a209f21fdadf264e");
//                SDKManager.getInstance().stopconnect();


                /*SDKManager.getInstance().sendLog("13911391996", new SDKDisposeDataListener() {
                    @Override
                    public void onSuccess() {
                        showToastCenter(MainActivity.this,"发送成功");
                    }

                    @Override
                    public void onFailure() {
                        showToastCenter(MainActivity.this,"发送失败");
                    }
                });*/



//                showProgress("支付中...");




            }
        });

        bt_startgoldpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SDKManager.getInstance().changeModle(isdebug,"5aa66b95f3745e62ffd44a8e");
                isdebug = !isdebug;
            }
        });
        bt_startLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDMcode();

                /*if(et_deviceno.getText() != null && !et_deviceno.getText().toString().equals(""))
                    deviceNo = et_deviceno.getText().toString();
                SDKManager.getInstance().startSendLog(deviceNo);*/
            }
        });
        bt_endLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_deviceno.getText() != null && !et_deviceno.getText().equals(""))
                    deviceNo = et_deviceno.getText().toString();
                SDKManager.getInstance().endSendLog(deviceNo);
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction(IConstants.EVENT_RECEIVE_LOG);
        filter.addAction(IConstants.LOG_EVENT_CONNECT);
        filter.addAction(IConstants.LOG_EVENT_DISCONNECT);
        mRecever = new MyRecever();
        this.registerReceiver(mRecever,filter);


    }

    public void getDMcode(){

        TestRequestCenter.getDMcode("35029234309580522280593353757", new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {



            }

            @Override
            public void onFailure(Object reasonObj) {



            }
        });

    }



    public class MyRecever extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(IConstants.EVENT_RECEIVE_LOG.equals(intent.getAction())){
                String msg= intent.getStringExtra(IConstants.EXTRA_MESSAGE);
                modelList.add(new JData(msg));
                setInstentIntrceAdapter();
            }else if(IConstants.LOG_EVENT_CONNECT.equals(intent.getAction())){
                tx_socket_status.setTextColor(Color.GREEN);
                tx_socket_status.setText("当前设备已连接");

            }else if(IConstants.LOG_EVENT_DISCONNECT.equals(intent.getAction())){
                tx_socket_status.setTextColor(Color.RED);
                tx_socket_status.setText("当前设备未连接");
            }

        }
    }


    private RecyclerView left_recycler_view;
    private pushRecyclerViewAdapter left_mAdapater;
    private ArrayList<JData> modelList;
    private void setInstentIntrceAdapter() {
        if (left_mAdapater == null) {

            left_mAdapater = new pushRecyclerViewAdapter(this, modelList);
            final LinearLayoutManager lin_layoutManager = new LinearLayoutManager(this);
            left_recycler_view.setLayoutManager(lin_layoutManager);
            left_recycler_view.setAdapter(left_mAdapater);
        }
        left_mAdapater.updateList(modelList);
        left_recycler_view.scrollToPosition(left_mAdapater.getItemCount() - 1);
        if(modelList.size()>200)
            modelList.clear();
    }



    private static final int QR_WIDTH = 300;
    private static final int QR_HEIGHT = 300;
    public static Bitmap createQRImage(String url)
    {
        try
        {
            //判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1)
            {
                return null;
            }
            // 定义二维码的参数
            HashMap<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");// 设置字符编码
            //hints.put(EncodeHintType.CHARACTER_SET, "unicode-16");// 设置字符编码
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);// 设置容错等级
            hints.put(EncodeHintType.MARGIN, 2);// 设置边距（默认值5）
            hints.put(EncodeHintType.DATA_MATRIX_SHAPE, SymbolShapeHint.FORCE_SQUARE);
            // 生成二维码
            BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++)
            {
                for (int x = 0; x < QR_WIDTH; x++)
                {
                    if (bitMatrix.get(x, y))
                    {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    }
                    else
                    {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            //显示到一个ImageView上面
            return bitmap;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }


    public void showToastCenter(Context mContext, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
        }
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.setText(msg);
        mToast.show();

    }


    /**
     * 显示对话框
     */
    public void showProgress(String message) {
        progressDialog = new ImProgressMsgDialog.Builder(this).setTextContent(message).create();
        if (!isFinishing()) {
            progressDialog.show();
        }
    }

    /**
     * 取消等待框
     */
    public void dismissProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            try {
                progressDialog.dismiss();
                progressDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        Log.i("pushservice", "onDestroy: --------");
        SDKManager.getInstance().destroy();
        super.onDestroy();
    }
}
