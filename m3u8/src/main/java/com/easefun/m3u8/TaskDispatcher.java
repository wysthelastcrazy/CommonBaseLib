package com.easefun.m3u8;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by yas on 2020-02-17
 * Describe:任务调度，维护一个无界的阻塞队列
 * 不断从队列头部取出待执行的任务，如果没有任务就阻塞，直到有新任务被加入队列
 */
public class TaskDispatcher extends Thread{
    private BlockingQueue<M3U8DownloadRecord> mRecordQueue;
    private volatile boolean mQuit = false;

    public TaskDispatcher(){
        mRecordQueue = new LinkedBlockingDeque<>();
    }

    public void quit(){
        mQuit = true;
        interrupt();
    }

    @Override
    public void run() {
        while (!isInterrupted()){
            try {
                //取出一个下载任务，如果队列为空就阻塞在这里
                M3U8DownloadRecord record = mRecordQueue.take();
                //获取信号量，获取不到就阻塞在这里，直到有下载完成的任务释放一个信号量
                M3U8DownloadManager.sDownloadPermit.acquire();
                if (record.getDownloadState() == M3U8DownloadManager.STATE_REENQUEUE
                        &&record.getFileLength()>0
                        &&record.getSubTaskList().size()>0){
                    // 这里对应暂停后重新启动的情形，详见后面 DownloadUtil 的分析
                    M3U8DownloadManager.getInstance().reStart(record);
                }else{
                    // 这里对应新开始的任务的情形
                    M3U8DownloadManager.getInstance().start(record);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                if (mQuit){
                    return;
                }
            }
        }
    }

    /**
     * 添加任务
     * @param record
     */
    public void enqueueRecord(M3U8DownloadRecord record){
        mRecordQueue.add(record);
    }
}
