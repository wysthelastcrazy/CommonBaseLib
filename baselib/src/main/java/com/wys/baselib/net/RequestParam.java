package com.wys.baselib.net;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yas on 2019/6/20
 * Describe:
 */
public class RequestParam {
    private Map<String,Object> mMap;
    private Map<String, String> files;
    public RequestParam(){
        mMap = new HashMap<>();
        files = new HashMap<>();
        if (RequestConfig.getCommonParams()!=null){
            mMap.putAll(RequestConfig.params.getParams());
        }
    }

    public RequestParam addFileParam(String key,String filePath){
        files.put(key,filePath);
        return this;
    }

    public RequestParam addParam(String key,Object value){
        mMap.put(key,value);
        return this;
    }


    public Object getParam(String key) {
        return mMap.get(key);
    }

    public RequestParam removeParam(String key) {
        mMap.remove(key);
        return this;
    }

    public RequestParam clean() {
        mMap.clear();
        return this;
    }

    public Map<String,Object> getParams(){
        return mMap;
    }

    public Map<String,String> getFileParams(){
        return files;
    }

    public String toJsonStr(){
        Gson gson = new Gson();
        return gson.toJson(mMap);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String,Object> entry:mMap.entrySet()){
            builder.append(entry.getKey() + "=" + entry.getValue()+"&");
        }
        if (builder.length()>0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }
}
