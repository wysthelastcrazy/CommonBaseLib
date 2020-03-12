package com.easefun.m3u8.bean;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * M3U8实体类
 * Created by HDL on 2017/7/24.
 */

public class M3U8 {
    @Expose private String basepath;
    @Expose private List<M3U8Item> tsList = new ArrayList<>();

    public String getBasepath() {
        return basepath;
    }

    public void setBasepath(String basepath) {
        this.basepath = basepath;
    }

    public List<M3U8Item> getTsList() {
        return tsList;
    }

    public void addTs(M3U8Item ts) {
        this.tsList.add(ts);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("basepath: " + basepath);
        for (M3U8Item ts : tsList) {
            sb.append("\nts_file_name = " + ts);
        }
        return sb.toString();
    }
}
