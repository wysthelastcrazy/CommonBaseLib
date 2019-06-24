package com.wys.baselib.net.https;

import java.io.InputStream;

/**
 * Created by yas on 2019/6/24
 * Describe:
 */
public class SSLConfig {
    private InputStream certificates;
    private InputStream bksFile;        //双向认证KeyManager使用
    private String password;            //KeyManager

    public SSLConfig setCertificatesIn(InputStream certificates){
        this.certificates = certificates;
        return this;
    }
    public SSLConfig setKeyMangerConfig(InputStream bksFile,String password){
        this.bksFile = bksFile;
        this.password = password;
        return this;
    }
    public InputStream getCertificatesIn(){
        return certificates;
    }
    public InputStream getKeyMangerIn(){
        return bksFile;
    }
    public String getKeyMangerPWD(){
        return password;
    }
}
