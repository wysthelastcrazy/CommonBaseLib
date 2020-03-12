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
    private M3U8DownloadRecord record;
    @Expose
    private int startLocation;      //下载文件的起点位置
    @Expose
    private int endLoaction;        //下载结束位置
    @Expose
    private String subFileName;     //子文件（.ts）名称
    @Expose
    private String downloadUrl;             //子文件下载地址
    @Expose
    private boolean isCompleted;

    private InputStream is;
    private RandomAccessFile file;

    public M3U8SubTask(M3U8DownloadRecord record,String subFileName,String downloadUrl) {
        this.subFileName = subFileName;
        this.downloadUrl = downloadUrl;
        this.record = record;
    }
    void setRecord(M3U8DownloadRecord record){
        this.record = record;
    }

    @Override
    public void run() {
        if (!isCompleted) {
            getInfo();
            download();
        }
    }
    private void getInfo(){
        try {
            URL url = new URL(downloadUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setConnectTimeout(M3U8DownloadManager.TIME_OUT);
            conn.connect();

            int fileLength = conn.getContentLength();
            File file = new File(record.getDownloadDir()+File.separator+subFileName);
            if (!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
//            RandomAccessFile rafile = new RandomAccessFile(file,"rwd");
//            rafile.setLength(fileLength);
            record.increaseFileLength(fileLength);
            endLoaction = fileLength;
            M3U8DownloadManager.getInstance().fileLengthSet(record);
        } catch (IOException e) {
            M3U8DownloadManager.getInstance().downloadFailed(record, "Get filelength failed!");
            e.printStackTrace();
        }
    }

    private void download(){
        try {
            URL url = new URL(downloadUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (endLoaction>startLocation) {
                conn.setRequestProperty("Range", "bytes=" + startLocation + "-" + endLoaction);
            }
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setConnectTimeout(M3U8DownloadManager.TIME_OUT);
            conn.setReadTimeout(30 * 1000);

            is = conn.getInputStream();
            RandomAccessFile rafile = new RandomAccessFile(record.getDownloadDir()+ File.separator+subFileName,"rwd");
            rafile.seek(startLocation);

            byte[] buffer = new byte[4096];
            int len;
            //为了实现任务的中断，必须在循环写入的时候，判断当前任务的状态，
            //如果状态不是DOWNLOADING，需要立即跳出循环，以实现暂停下载的功能
            while (record.getDownloadState() == M3U8DownloadManager.STATE_DOWNLOADING
                    &&(len = is.read(buffer))!= -1){
                rafile.write(buffer,0,len);
                startLocation += len;
                record.increaseLength(len);
            }
            // 满足这个条件，代表该子任务下载完了自己那部分的数据，需要把 DownloadRecord 里记录已完成子任务数的变量值+1
            // 如果自己是最后一个完成的，那么表示整个下载任务完成
            if (record.getDownloadState() == M3U8DownloadManager.STATE_DOWNLOADING){
                isCompleted = true;
                if (record.completeSubTask()){
                    M3U8DownloadManager.getInstance().taskFinished(record);
                }
            }
            M3U8DownloadManager.getInstance().progressUpdated(record);
            Log.d("wys","[download] download:"+downloadUrl);
//            M3U8DownloadManager.getInstance().saveRecord(record);
        } catch (IOException e) {
            M3U8DownloadManager.getInstance().downloadFailed(record, "subtask failed!");
            Log.d("wys","[download&IOException] download:"+downloadUrl);
            e.printStackTrace();
        }finally {
            try {
                M3U8DownloadManager.getInstance().saveRecord(record);
                file.close();
                is.close();
            }catch (IOException | NullPointerException e){

            }
        }
    }
}
