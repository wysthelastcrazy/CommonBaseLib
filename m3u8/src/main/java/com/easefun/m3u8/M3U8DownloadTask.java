package com.easefun.m3u8;

import android.os.AsyncTask;
import android.text.TextUtils;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yas on 2020-03-12
 * Describe:
 */
public class M3U8DownloadTask extends AsyncTask<M3U8DownloadRecord,Integer,M3U8DownloadRecord> {
    private  BufferedReader reader;
    @Override
    protected M3U8DownloadRecord doInBackground(M3U8DownloadRecord... m3U8DownloadRecords) {
        M3U8DownloadRecord record = m3U8DownloadRecords[0];
        try {
            String coverUrl = record.getCoverUrl();
            if (!TextUtils.isEmpty(coverUrl)){
                M3U8SubTask subTask = record.getSubTask(record.getCoverFileName());
                if (subTask==null){
                    subTask = new M3U8SubTask(record,record.getCoverUrl(),0,M3U8SubTask.TYPE_COVER);
                    record.getSubTaskList().add(subTask);
                }else{
                    subTask.updateInfo(record.getCoverUrl(),0);
                }
            }

            URL url = new URL(record.getDownloadUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setConnectTimeout(5000);
            conn.connect();
            String realUrl = conn.getURL().toString();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String basePath = realUrl.substring(0,realUrl.lastIndexOf("/") + 1);
            record.setSubTaskBaseUrl(basePath);
            String line;
            float seconds = 0;
            while ((line = reader.readLine())!=null){
                if (line.startsWith("#EXTINF:")){
                    line = line.substring(8);
                    if (line.endsWith(",")){
                        line = line.substring(0,line.length()-1);
                    }
                    seconds = Float.parseFloat(line);
                    continue;
                }else if (line.startsWith("#EXT-X-KEY:")){
                    int keyUriIndex = line.indexOf("#EXT-X-KEY:METHOD=AES-128,URI=");
                    if (keyUriIndex != -1){
                        line = line.substring(keyUriIndex+"#EXT-X-KEY:METHOD=AES-128,URI=".length());
                        int ivIndex = line.indexOf(",IV=");
                        if (ivIndex!=-1){
                            String key = line.substring(1,ivIndex-1);
                            String iv = line.substring(ivIndex+4);
                            M3U8SubTask subTask = record.getSubTask(getFileName(key));
                            if (subTask==null){
                                subTask = new M3U8SubTask(record,key,0,M3U8SubTask.TYPE_KEY);
                                record.getSubTaskList().add(subTask);
                            }else {
                                subTask.updateInfo(key,0);
                            }
                        }
                    }
                    continue;
                }else if (line.startsWith("#")){
                    continue;
                }

                M3U8SubTask subTask = record.getSubTask(getFileName(line));
                if (subTask!=null){
                    subTask.updateInfo(line,seconds);
                }else {
                    subTask = new M3U8SubTask(record,line,seconds,M3U8SubTask.TYPE_TS);
                    record.getSubTaskList().add(subTask);
                }
                M3U8DownloadManager.getInstance().saveRecord(record);
                seconds = 0;
            }
            return record;
        } catch (IOException e) {
            M3U8DownloadManager.getInstance().downloadFailed(record, "Get m3u8 file failed!");
            e.printStackTrace();
        }finally {
            try {
                if (reader!=null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(M3U8DownloadRecord m3U8DownloadRecord) {
        if (m3U8DownloadRecord!=null&&m3U8DownloadRecord.getSubTaskList()!=null){
            for (M3U8SubTask subTask:m3U8DownloadRecord.getSubTaskList()){
                 M3U8DownloadManager.sExecutor.execute(subTask);
            }
        }
    }

    private String getFileName(String uri) {
        String fileName = uri.substring(uri.lastIndexOf("/") + 1);
        if (fileName.contains("?")) {
            return fileName.substring(0, fileName.indexOf("?"));
        }
        return fileName;
    }
}
