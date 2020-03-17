package com.easefun.m3u8;

import com.google.gson.annotations.Expose;

/**
 * Created by yas on 2020-02-13
 * Describe:保存网络请求的类
 */
public class M3U8DownloadRequest {
    @Expose private  String downloadUrl; //下载的url
    @Expose private final String downloadDir; //下载的目标文件路径
    @Expose private final String downloadName; //下载的文件名
    @Expose private final String taskId;      //任务id
    @Expose private final int taskType;       //区分任务分类
    @Expose private final String coverUrl;     //缩略图下载地址

    private M3U8DownloadRequest(Builder builder){
        downloadUrl = builder.downloadUrl;
        downloadDir = builder.downloadDir;
        downloadName = builder.downloadName;
        taskId = builder.taskId;
        taskType = builder.taskType;
        coverUrl = builder.coverUrl;
    }

    public String getFilePath() {
        return getDownloadDir() + "/" + getDownloadName();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void updateDownloadUrl(String downloadUrl){
        this.downloadUrl = downloadUrl;
    }

    public String getDownloadDir() {
        return downloadDir;
    }

    public String getDownloadName() {
        return downloadName;
    }

    public String getTaskId(){
        return taskId;
    }
    public int getTaskType(){
        return taskType;
    }

    public String getCoverUrl(){
        return coverUrl;
    }


    public static final class Builder{
        private String downloadUrl;
        private String downloadDir;
        private String downloadName;
        private String taskId;
        private int taskType;
        private String coverUrl;

        private Builder(){}

        public Builder downloadUrl(String downloadUrl){
            this.downloadUrl = downloadUrl;
            return this;
        }

        public Builder downloadDir(String downloadDir){
            this.downloadDir = downloadDir;
            return this;
        }

        public Builder downloadName(String downloadName){
            this.downloadName = downloadName;
            return this;
        }
        public Builder taskId(String taskId){
            this.taskId = taskId;
            return this;
        }
        public Builder taskType(int taskType){
            this.taskType = taskType;
            return this;
        }
        public Builder coverUrl(String coverUrl){
            this.coverUrl = coverUrl;
            return this;
        };
        public M3U8DownloadRequest build(){
            return new M3U8DownloadRequest(this);
        }
    }
}
