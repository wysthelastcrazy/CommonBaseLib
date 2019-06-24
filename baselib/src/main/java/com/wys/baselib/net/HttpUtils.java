package com.wys.baselib.net;

import android.text.TextUtils;
import android.util.Log;

import com.wys.baselib.net.https.SSLConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by yas on 2019/6/20
 * Describe:
 */
public class HttpUtils {

    public static final MediaType MEDIA_TYPE_PLAIN = MediaType.parse("text/plain;charset=utf-8");
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");
    public static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");



    public static Map<String,Object> commonParams;


    /**
     * 普通post FormBody
     * @param params
     * @return
     */
    public static FormBody createFormBody(Map<String,Object> params){
        FormBody.Builder builder = new FormBody.Builder();

        for (String key : params.keySet()) {
            //追加表单信息
            builder.add(key, params.get(key)+"");
        }
        return builder.build();
    }

    /**
     * 多图片上传请求数据组织
     * @param params 文本参数
     * @param files 文件参数，map的key为参数名  val为文件路径
     * @return
     */
    public static MultipartBody createMultipartBody(Map<String, Object> params, Map<String,String> files)  {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型
        if (params!=null&&params.size()>0){
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = entry.getKey();
                Object val = entry.getValue();
                if(val == null){
                    builder.addFormDataPart(key ,"");
                } else {
                    builder.addFormDataPart(key ,val+"");
                }
            }
        }
        if (files!=null&&files.size()>0){
            for (Map.Entry<String, String> entry : files.entrySet()) {
                String key = entry.getKey();
                String val = entry.getValue();
                if(!TextUtils.isEmpty(val)) {
                    File file = new File(val);
                    if (file.exists()) {
                        RequestBody imageBody = RequestBody.create(MediaType.parse(guessMimeType(file.getName())), file);
                        builder.addFormDataPart(key, file.getName(), imageBody);//imgfile 后台接收图片流的参数名
                    }else{
                        Log.e("HttpUtils","文件"+val+"不存在");
                    }
                }
            }
        }
        return builder.build();
    }

    /**
     * 获取文件类型
     * @param name 文件名称
     * @return
     */
    private static String guessMimeType(String name)
    {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(name);
        if (contentTypeFor == null)
        {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }








    public static X509TrustManager UnSafeTrustManager = new X509TrustManager() {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    };
    public static HostnameVerifier UnSafeHostnameVerifier = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    public HttpUtils() {
    }

    public static HttpUtils.SSLParams getSslSocketFactory() {
        SSLConfig config = RequestClient.getSSLConfig();
        if (config!=null){
            return getSslSocketFactoryBase(config.getKeyMangerIn(), config.getKeyMangerPWD(),config.getCertificatesIn());
        }else{
            return getSslSocketFactoryBase((InputStream)null, (String)null);
        }
    }
    private static HttpUtils.SSLParams getSslSocketFactoryBase(InputStream bksFile, String password, InputStream... certificates) {
        HttpUtils.SSLParams sslParams = new HttpUtils.SSLParams();

        try {
            KeyManager[] keyManagers = prepareKeyManager(bksFile, password);
            TrustManager[] trustManagers = prepareTrustManager(certificates);
            X509TrustManager manager;
            if (trustManagers != null) {
                manager = chooseTrustManager(trustManagers);
            } else {
                manager = UnSafeTrustManager;
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagers, new TrustManager[]{manager}, (SecureRandom)null);
            sslParams.sSLSocketFactory = sslContext.getSocketFactory();
            sslParams.trustManager = manager;
            return sslParams;
        } catch (NoSuchAlgorithmException var9) {
            throw new AssertionError(var9);
        } catch (KeyManagementException var10) {
            throw new AssertionError(var10);
        }
    }

    private static KeyManager[] prepareKeyManager(InputStream bksFile, String password) {
        try {
            if (bksFile != null && password != null) {
                KeyStore clientKeyStore = KeyStore.getInstance("BKS");
                clientKeyStore.load(bksFile, password.toCharArray());
                KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                kmf.init(clientKeyStore, password.toCharArray());
                return kmf.getKeyManagers();
            } else {
                return null;
            }
        } catch (Exception var4) {
//            OkLogger.printStackTrace(var4);
            return null;
        }
    }

    private static TrustManager[] prepareTrustManager(InputStream... certificates) {
        if (certificates != null && certificates.length > 0) {
            try {
                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load((KeyStore.LoadStoreParameter)null);
                int index = 0;
                InputStream[] var4 = certificates;
                int var5 = certificates.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    InputStream certStream = var4[var6];
                    String certificateAlias = Integer.toString(index++);
                    Certificate cert = certificateFactory.generateCertificate(certStream);
                    keyStore.setCertificateEntry(certificateAlias, cert);

                    try {
                        if (certStream != null) {
                            certStream.close();
                        }
                    } catch (IOException var11) {
//                        OkLogger.printStackTrace(var11);
                    }
                }

                TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                tmf.init(keyStore);
                return tmf.getTrustManagers();
            } catch (Exception var12) {
//                OkLogger.printStackTrace(var12);
                return null;
            }
        } else {
            return null;
        }
    }

    private static X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
        TrustManager[] var1 = trustManagers;
        int var2 = trustManagers.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            TrustManager trustManager = var1[var3];
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager)trustManager;
            }
        }

        return null;
    }

    public static class SSLParams {
        public SSLSocketFactory sSLSocketFactory;
        public X509TrustManager trustManager;

        public SSLParams() {
        }
    }
}
