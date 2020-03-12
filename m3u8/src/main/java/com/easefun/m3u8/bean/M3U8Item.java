package com.easefun.m3u8.bean;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;

/**
 * Created by suju by 2020/2/11
 */
public class M3U8Item implements Comparable<M3U8Item> {
    public static final int TYPE_TS = 1;
    public static final int TYPE_KEY = 2;
    @Expose protected String uri;       //item链接
    @Expose protected float seconds;    //长度
    @Expose private int type;

    public M3U8Item(String uri,float seconds,int type){
        this.uri = uri;
        this.seconds = seconds;
        this.type = type;
    }
    public String getUri(){
        return uri;
    }
    public void setUri(String uri){
        this.uri = uri;
    }

    public String getFileName() {
        String fileName = uri.substring(uri.lastIndexOf("/") + 1);
        if (fileName.contains("?")) {
            return fileName.substring(0, fileName.indexOf("?"));
        }
        return fileName;
    }
    public int getType(){
        return type;
    }
    public float getSeconds() {
        return seconds;
    }
    @Override
    public int compareTo(@NonNull M3U8Item m3U8Item) {
        if (type==TYPE_KEY){
            return 0;
        }
        return uri.compareTo(m3U8Item.uri);
    }
}
