package com.wys.downloader;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by yas on 2020-02-13
 * Describe:
 */
public class DataHelper {
    private final String TAG = "DataHelper";
    private SharedPreferences sp;
    private Gson gson;
    public DataHelper(Context context){
        sp = context.getSharedPreferences("download_task", Context.MODE_PRIVATE);
        gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    public void saveAllRecord(Collection<DownloadRecord> records){
        for (DownloadRecord record : records) {
            saveRecord(record);
        }
    }

    public void saveRecord(DownloadRecord record){
        String json = gson.toJson(record);
        sp.edit().putString(record.getTaskId(), json).commit();
        Log.d(TAG, "[saveRecord]"+json);
    }

    public List<DownloadRecord> loadAllRecords(){
        Map<String, String> map = (Map<String, String>) sp.getAll();
        List<DownloadRecord> list = new ArrayList<>();
        for (String json: map.values()) {
            Log.d(TAG, "[loadAllRecords]"+json);
            if (!TextUtils.isEmpty(json)) {
                DownloadRecord record = gson.fromJson(json, DownloadRecord.class);
                record.linkSubTask();
                list.add(record);
            }
        }
        Collections.sort(list);
        return list;
    }
    public void deleteRecord(DownloadRecord record){
        Log.d(TAG, "[deleteRecord] taskId:"+record.getTaskId());
        sp.edit().putString(record.getTaskId(), "").commit();
        deleteFile(record.getFilePath());
    }

    private void deleteFile(String filePath){
        if (TextUtils.isEmpty(filePath)) return;
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }
}
