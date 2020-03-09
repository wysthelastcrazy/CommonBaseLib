package com.wys.downloader;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yas on 2020-02-13
 * Describe:下载记录，主要用来记录下载任务相关的数据
 * 实现Comparable接口，可以按照创建时间对任务排序
 */
public class DownloadRecord implements Comparable<DownloadRecord>{
    @Expose private final DownloadRequest request;  //下载请求
    @Expose private int downloadState;  //下载状态
    @Expose private int currentLength;  //已经下载的数据大小
    @Expose private int fileLength;     //文件总大小
    @Expose private int completedSubTask;//完成任务的子任务数
    @Expose private List<SubTask> subTaskList;  //记录子任务的列表
    @Expose private long createTime;    //任务创建时间。可以用来排序

    public DownloadRecord(DownloadRequest request) {
        this.request = request;
        subTaskList = new ArrayList<>();
        downloadState = DownloadManager.STATE_INITIAL;
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


    public List<SubTask> getSubTaskList(){
        return subTaskList;
    }
    public long getCreateTime(){
        return createTime;
    }
    /**
     * 子任务完成
     * @return 是否全部子任务都已经完成
     */
    synchronized public boolean completeSubTask(){
        completedSubTask++;
        if (completedSubTask == subTaskList.size()){
            return true;
        }
        return false;
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

    synchronized public void reset(){
        currentLength = 0;
        fileLength = 0;
        completedSubTask = 0;
        downloadState = DownloadManager.STATE_INITIAL;
        subTaskList.clear();
    }

    public void linkSubTask(){
        for (SubTask subTask : subTaskList){
            subTask.setRecord(this);
        }
    }

    @Override
    public int compareTo(DownloadRecord o) {
        if (createTime < o.getCreateTime()) return -1;
        if (createTime > o.getCreateTime()) return 1;
        return 0;
    }
}
