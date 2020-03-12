package com.easefun.m3u8;

import android.os.AsyncTask;

import com.easefun.m3u8.bean.M3U8Item;
import com.easefun.m3u8.bean.M3U8;

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
            URL url = new URL(record.getDownloadUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setConnectTimeout(5000);
            conn.connect();
            String realUrl = conn.getURL().toString();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String basePath = realUrl.substring(0,realUrl.lastIndexOf("/") + 1);
            M3U8 m3U8 = new M3U8();
            m3U8.setBasepath(basePath);
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
                            m3U8.addTs(new M3U8Item(key,0,M3U8Item.TYPE_KEY));
                        }
                    }
                    continue;
                }else if (line.startsWith("#")){
                    continue;
                }
                m3U8.addTs(new M3U8Item(line,seconds,M3U8Item.TYPE_TS));
                seconds = 0;
            }
            record.setM3U8(m3U8);
            return record;
        } catch (IOException e) {
            M3U8DownloadManager.getInstance().downloadFailed(record, "Get m3u8 file failed!");
            e.printStackTrace();
        }finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(M3U8DownloadRecord m3U8DownloadRecord) {
        if (m3U8DownloadRecord!=null&&m3U8DownloadRecord.getM3U8()!=null){
            m3U8DownloadRecord.getSubTaskList().clear();
            M3U8 m3U8 = m3U8DownloadRecord.getM3U8();
            if (m3U8.getTsList()!=null) {
                for (M3U8Item item:m3U8.getTsList()){
                    String urlPath;
                    if (item.getUri().startsWith("http")) {
                        urlPath = item.getUri();
                    } else {
                        urlPath = m3U8.getBasepath() + item.getUri();
                    }
                    M3U8SubTask subTask = new M3U8SubTask(m3U8DownloadRecord,item.getFileName(),
                            urlPath);
                    m3U8DownloadRecord.getSubTaskList().add(subTask);
                    M3U8DownloadManager.sExecutor.execute(subTask);
                }
            }
            M3U8DownloadManager.getInstance().saveRecord(m3U8DownloadRecord);
        }
    }
}
