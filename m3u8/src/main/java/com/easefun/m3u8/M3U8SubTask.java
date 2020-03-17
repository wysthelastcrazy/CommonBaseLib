package com.easefun.m3u8;

import android.util.Log;

import com.google.gson.annotations.Expose;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yas on 2020-02-13
 * Describe:
 */
public class M3U8SubTask implements Runnable{
    public static final int TYPE_TS = 1;
    public static final int TYPE_KEY = 2;
    public static final int TYPE_COVER = 3;
    private M3U8DownloadRecord record;
    @Expose
    private int startLocation;      //下载文件的起点位置
    @Expose
    private int fileLength;        //文件大小
    @Expose
    private boolean isCompleted;
    @Expose protected String uri;       //item链接地址
    @Expose protected float seconds;    //长度
    @Expose private int type;           //类型

    private InputStream is;
    private RandomAccessFile file;

    public void reset(){
        startLocation = 0;
        fileLength = 0;
        isCompleted = false;
    }

    public M3U8SubTask(M3U8DownloadRecord record,String uri,float seconds,int type) {
        this.uri = uri;
        this.seconds = seconds;
        this.record = record;
        this.type = type;
    }
    public void updateInfo(String uri,float seconds){
        this.uri = uri;
        this.seconds = seconds;
    }
    void setRecord(M3U8DownloadRecord record){
        this.record = record;
    }

    @Override
    public void run() {
        if (record.isAllSubTaskComplete()){
            //如果子任务已经全部完成，则直接进入m3u8本地文件的合成阶段
            M3U8DownloadManager.getInstance().taskFinished(record);
        }else {
            //有子任务未完成时，判断当前子任务是否完成，未完成的开始下载任务
            if (!isCompleted) {
                if (fileLength <= 0) {
                    getInfo();
                }
                download();
            }
        }
    }
    private void getInfo(){
        try {
            URL url = new URL(getDownloadUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setConnectTimeout(M3U8DownloadManager.TIME_OUT);
            conn.connect();

            fileLength = conn.getContentLength();
            File file = new File(record.getDownloadDir()+File.separator+getFileName());
            if (!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            record.increaseFileLength(fileLength);
            M3U8DownloadManager.getInstance().fileLengthSet(record);
        } catch (IOException e) {
            M3U8DownloadManager.getInstance().downloadFailed(record, "Get filelength failed!");
            e.printStackTrace();
        }
    }

    private void download(){
        try {
            URL url = new URL(getDownloadUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (startLocation<fileLength) {
                conn.setRequestProperty("Range", "bytes=" + startLocation + "-" + fileLength);
            }
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setConnectTimeout(M3U8DownloadManager.TIME_OUT);
            conn.setReadTimeout(30 * 1000);

            is = conn.getInputStream();
            file = new RandomAccessFile(record.getDownloadDir()+ File.separator+getFileName(),"rwd");
            file.seek(startLocation);

            byte[] buffer = new byte[4096];
            int len;
            //为了实现任务的中断，必须在循环写入的时候，判断当前任务的状态，
            //如果状态不是DOWNLOADING，需要立即跳出循环，以实现暂停下载的功能
            while (record.getDownloadState() == M3U8DownloadManager.STATE_DOWNLOADING
                    &&(len = is.read(buffer))!= -1){
                file.write(buffer,0,len);
                startLocation += len;
                record.increaseLength(len);
                M3U8DownloadManager.getInstance().progressUpdated(record);
            }
            // 满足这个条件，代表该子任务下载完了自己那部分的数据，需要把 DownloadRecord 里记录已完成子任务数的变量值+1
            // 如果自己是最后一个完成的，那么表示整个下载任务完成
            if (record.getDownloadState() == M3U8DownloadManager.STATE_DOWNLOADING){
                isCompleted = true;
                record.completeSubTask();
                if (record.isAllSubTaskComplete()){
                    M3U8DownloadManager.getInstance().taskFinished(record);
                }
            }
        } catch (IOException e) {
            M3U8DownloadManager.getInstance().downloadFailed(record, "subtask failed!");
            e.printStackTrace();
        }finally {
            try {
                M3U8DownloadManager.getInstance().saveRecord(record);
                if (file!=null) {
                    file.close();
                }
                if (is!=null) {
                    is.close();
                }
            }catch (IOException | NullPointerException e){

            }
        }
    }

    public int getType(){
        return type;
    }
    private String getDownloadUrl(){
        if (uri.startsWith("http")) {
            return uri;
        } else {
            return record.getSubTaskBaseUrl()+uri;
        }
    }

    public float getSeconds() {
        return seconds;
    }

    public String getFileName() {
        if(type == TYPE_COVER){
            return "cover.png";
        }
        String fileName = uri.substring(uri.lastIndexOf("/") + 1);
        if (fileName.contains("?")) {
            return fileName.substring(0, fileName.indexOf("?"));
        }
        return fileName;
    }
}
