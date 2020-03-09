package com.wys.downloader;

import com.google.gson.annotations.Expose;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yas on 2020-02-13
 * Describe:
 */
public class SubTask implements Runnable{
    private DownloadRecord record;
    @Expose
    private int startLocation;      //下载文件的起点位置
    @Expose
    private int endLocation;        //下载文件的重点位置

    private InputStream is;
    private RandomAccessFile file;

    public SubTask(DownloadRecord record, int startLocation, int endLocation) {
        this.record = record;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

    void setRecord(DownloadRecord record){
        this.record = record;
    }

    @Override
    public void run() {
        try {
            URL url = new URL(record.getDownloadUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Range", "bytes=" + startLocation + "-" + endLocation);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setConnectTimeout(DownloadManager.TIME_OUT);
            conn.setReadTimeout(30 * 1000);

            is = conn.getInputStream();
            file = new RandomAccessFile(record.getFilePath(),"rwd");
            file.seek(startLocation);

            byte[] buffer = new byte[4096];
            int len;
            //为了实现任务的中断，必须在循环写入的时候，判断当前任务的状态，
            //如果状态不是DOWNLOADING，需要立即跳出循环，以实现暂停下载的功能
            while (record.getDownloadState() == DownloadManager.STATE_DOWNLOADING
                    &&(len = is.read(buffer))!= -1){
                file.write(buffer,0,len);
                startLocation += len;
                record.increaseLength(len);
                DownloadManager.getInstance().progressUpdated(record);
            }
            // 满足这个条件，代表该子任务下载完了自己那部分的数据，需要把 DownloadRecord 里记录已完成子任务数的变量值+1
            // 如果自己是最后一个完成的，那么表示整个下载任务完成
            if (record.getDownloadState() == DownloadManager.STATE_DOWNLOADING){
                if (record.completeSubTask()){
                    DownloadManager.getInstance().taskFinished(record);
                }
            }
        } catch (IOException e) {
            DownloadManager.getInstance().downloadFailed(record, "subtask failed!");
        }finally {
            try {
                DownloadManager.getInstance().saveRecord(record);
                file.close();
                is.close();
            }catch (IOException | NullPointerException e){

            }
        }
    }
}
