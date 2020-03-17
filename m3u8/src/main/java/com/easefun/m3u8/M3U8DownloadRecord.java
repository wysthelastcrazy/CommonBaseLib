package com.easefun.m3u8;

import com.google.gson.annotations.Expose;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yas on 2020-02-13
 * Describe:下载记录，主要用来记录下载任务相关的数据
 * 实现Comparable接口，可以按照创建时间对任务排序
 */
public class M3U8DownloadRecord implements Comparable<M3U8DownloadRecord>{
    @Expose private final M3U8DownloadRequest request;  //下载请求
    @Expose private int downloadState;  //下载状态
    @Expose private int currentLength;  //已经下载的数据大小
    @Expose private int fileLength;     //文件总大小
    @Expose private int completedSubTask;//完成任务的子任务数
    @Expose private List<M3U8SubTask> subTaskList;  //记录子任务的列表
    @Expose private long createTime;    //任务创建时间。可以用来排序
    @Expose private String subTaskBaseUrl;      //子任务的baseUrl


    public M3U8DownloadRecord(M3U8DownloadRequest request) {
        this.request = request;
        subTaskList = new ArrayList<>();
        downloadState = M3U8DownloadManager.STATE_INITIAL;
        createTime = System.currentTimeMillis();
    }



    synchronized public int getDownloadState() {
        return downloadState;
    }
    synchronized public int getCurrentLength() {
        return currentLength;
    }
    public int getFileLength() {
        return fileLength;
    }
    synchronized public void setDownloadState(int downloadState) {
        this.downloadState = downloadState;
    }

    synchronized public void setCurrentLength(int currentLength) {
        this.currentLength = currentLength;
    }

    public void setFileLength(int fileLength) {
        this.fileLength = fileLength;
    }
    synchronized void increaseFileLength(int length){
        fileLength += length;
    }

    public String getDownloadUrl(){
        return request.getDownloadUrl();
    }

    public String getDownloadDir(){
        return request.getDownloadDir();
    }

    public String getDownloadName(){
        return request.getDownloadName();
    }
    public String getTaskId(){
        return request.getTaskId();
    }

    public int getTaskType(){return request.getTaskType();}

    public String getCoverUrl(){
        return request.getCoverUrl();
    }
    public String getCoverFileName(){
        return "cover.png";
    }

    /**
     * 更新下载链接，以为m3u8下载链接具有时效性，过期后需要使用新的链接
     * @param downloadUrl
     */
    public void updateDownloadUrl(String downloadUrl){
        request.updateDownloadUrl(downloadUrl);
    }

    public List<M3U8SubTask> getSubTaskList(){
        return subTaskList;
    }
    public M3U8SubTask getSubTask(String fileName){
        for (M3U8SubTask item:subTaskList){
            if (fileName.equals(item.getFileName())){
                return item;
            }
        }
        return null;
    }
    public long getCreateTime(){
        return createTime;
    }
    /**
     * 子任务完成
     * @return 是否全部子任务都已经完成
     */
    synchronized public void completeSubTask(){
        completedSubTask++;
    }

    synchronized public boolean isAllSubTaskComplete(){
        if (subTaskList.size()==0) return false;
        return completedSubTask==subTaskList.size();
    }

    synchronized void increaseLength(int length){
        currentLength += length;
    }

    public int getProgress(){
        return Math.round(getCurrentLength()/(getFileLength()*1.0f)*100);
    }

    public String getFilePath(){
        return getDownloadDir()+"/"+getDownloadName();
    }
    /**获取缩略图本地路径*/
    public String getCoverPath(){
        return getDownloadDir()+ File.separator+getCoverFileName();
    }

    synchronized public void reset(){
        currentLength = 0;
        fileLength = 0;
        completedSubTask = 0;
        downloadState = M3U8DownloadManager.STATE_INITIAL;
        subTaskList.clear();
    }

    public void linkSubTask(){
        for (M3U8SubTask subTask : subTaskList){
            subTask.setRecord(this);
        }
    }

    @Override
    public int compareTo(M3U8DownloadRecord o) {
        if (createTime < o.getCreateTime()) return -1;
        if (createTime > o.getCreateTime()) return 1;
        return 0;
    }

    public String getSubTaskBaseUrl() {
        return subTaskBaseUrl;
    }

    public void setSubTaskBaseUrl(String subTaskBaseUrl) {
        this.subTaskBaseUrl = subTaskBaseUrl;
    }
}
