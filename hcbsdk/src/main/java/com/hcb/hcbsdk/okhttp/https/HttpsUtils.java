package com.hcb.hcbsdk.okhttp.https;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class HttpsUtils {


    /**
     * trust all the https point
     */
    public static SSLSocketFactory getSslSocketFactory() {

        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("SSL");
            X509TrustManager[] xTrustArray = new X509TrustManager[]
                    {TRUST_MANAGER};
            sslContext.init(null, xTrustArray, new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslContext.getSocketFactory();
    }


    private static final String KEY_STORE_TYPE_BKS = "bks";//证书类型
    private static final String KEY_STORE_TYPE_P12 = "PKCS12";//证书类型


    private static final String KEY_STORE_PASSWORD = "***";//证书密码（应该是客户端证书密码）
    private static final String KEY_STORE_TRUST_PASSWORD = "***";//授信证书密码（应该是服务端证书密码）

    public static SSLSocketFactory getSSLSocketFactory(Context context) {
        InputStream trust_input = null;
        InputStream client_input = null;
        try {
             trust_input = context.getAssets().open("ca.bks");//服务器授信证书
             client_input = context.getAssets().open("outgoing.CertwithKey.pkcs12");//客户端证书

//            InputStream trust_input = context.getResources().openRawResource(R.raw.client_trust);//服务器授信证书
//            InputStream client_input = context.getResources().openRawResource(R.raw.client);//客户端证书

            SSLContext sslContext = SSLContext.getInstance("TLS");
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(trust_input, KEY_STORE_TRUST_PASSWORD.toCharArray());
            KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE_P12);
            keyStore.load(client_input, KEY_STORE_PASSWORD.toCharArray());
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, KEY_STORE_PASSWORD.toCharArray());
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
            SSLSocketFactory factory = sslContext.getSocketFactory();
            return factory;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                trust_input.close();
                client_input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    /**
     *主机名验证
     */
    private static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };


    private static final X509TrustManager TRUST_MANAGER = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    };





    /**
     * 设置HTTPS认证：默认信任所有证书
     */
    public static void setDefaultSslSocketFactory(OkHttpClient.Builder clientBuilder){
        clientBuilder.hostnameVerifier(DO_NOT_VERIFY);
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null,new TrustManager[]{TRUST_MANAGER}, new SecureRandom());
            clientBuilder.sslSocketFactory(sc.getSocketFactory(),TRUST_MANAGER);
        } catch (Exception e) {
            Log.e("https","Https认证异常: "+e.getMessage());
        }
    }


    /**
     * 设置HTTPS认证
     */
    public static void setTLSSSLSocketFactory(OkHttpClient.Builder clientBuilder,Context ctx,String httpsCertificate){
        SSLContext sslContext = getSSLContext(ctx,httpsCertificate);
        if(sslContext != null){
            clientBuilder.sslSocketFactory(sslContext.getSocketFactory(),TRUST_MANAGER);
        }
    }


    private static SSLContext getSSLContext(Context ctx,String httpsCertificate) {
        SSLContext sslContext = null;
        try {


            sslContext = SSLContext.getInstance("TLS");
            InputStream inputStream = ctx.getAssets().open(httpsCertificate);
            CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
            Certificate cer = cerFactory.generateCertificate(inputStream);
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(null, null);
            keyStore.setCertificateEntry("trust", cer);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, null);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslContext;
    }


}