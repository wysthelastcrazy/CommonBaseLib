package com.wys.downloader;

import android.os.AsyncTask;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by yas on 2020-02-13
 * Describe:下载任务，实现获取文件长度，并开启子线程开始下载功能
 */
public class DownloadTask extends AsyncTask<DownloadRecord,Integer,DownloadRecord> {
    @Override
    protected DownloadRecord doInBackground(DownloadRecord... downloadRecords) {
        DownloadRecord record = downloadRecords[0];
        try {
            URL url = new URL(record.getDownloadUrl());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setConnectTimeout(DownloadManager.TIME_OUT);
            conn.connect();

            int fileLength = conn.getContentLength();
            File file = new File(record.getFilePath());
            if (!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            RandomAccessFile rafile = new RandomAccessFile(file,"rwd");
            rafile.setLength(fileLength);
            record.setFileLength(fileLength);
            DownloadManager.getInstance().fileLengthSet(record);
            return record;
        } catch (IOException e) {
            DownloadManager.getInstance().downloadFailed(record, "Get filelength failed!");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(DownloadRecord downloadRecord) {
        if (downloadRecord != null){
            downloadRecord.getSubTaskList().clear();
            int blockSize = downloadRecord.getFileLength()/ DownloadManager.sThreadNum;
            for (int i = 0; i< DownloadManager.sThreadNum; i++){
                int startL = i*blockSize;
                int endL = (i+1)*blockSize;
                if (i == DownloadManager.sThreadNum-1){
                    endL = downloadRecord.getFileLength();
                }
                SubTask subTask = new SubTask(downloadRecord,startL,endL);
                downloadRecord.getSubTaskList().add(subTask);
                DownloadManager.sExecutor.execute(subTask);
            }
            DownloadManager.getInstance().saveRecord(downloadRecord);
        }
    }
}
