package com.easefun.m3u8;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.easefun.m3u8.bean.M3U8Item;
import com.easefun.m3u8.bean.M3U8;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by yas on 2020-02-13
 * Describe:
 */
public class M3U8DownloadManager {
    private final String TAG = "M3U8DownloadManager";
    public static final int STATE_INITIAL = 0;  //初始化
    public static final int STATE_DOWNLOADING = 1;  //下载中
    public static final int STATE_PAUSED = 2;       //暂停下载中
    public static final int STATE_FINISHED = 3;     //下载完成
    public static final int STATE_CANCELED = 4;     //取消下载
    public static final int STATE_FAILED = 5;       //下载是吧
    public static final int STATE_REENQUEUE = 6;    //等待下载

    final static int TIME_OUT = 5000;
    final static int DEFAULT_TASK_AMOUNT = 3;
    final static int DEFAULT_THREAD_AMOUNT = 6;

    static ExecutorService sExecutor;
    static Map<String, M3U8DownloadRecord> sRecordMap;
    static Semaphore sDownloadPermit;
    static int sThreadNum = DEFAULT_THREAD_AMOUNT;

    private static M3U8DownloadManager instance;
    private TaskDispatcher mTaskDispatcher;
    private DataHelper mDataHelper;

    private boolean initialized;

    private ArrayList<WeakReference<IDownloadListener>> listeners = new ArrayList<>();

    private M3U8DownloadManager(){
        sRecordMap = new LinkedHashMap<>();
    }
    public static M3U8DownloadManager getInstance() {
        if (instance == null) {
            synchronized (M3U8DownloadManager.class) {
                if (instance == null)
                    instance = new M3U8DownloadManager();
            }
        }
        return instance;
    }

    /**
     * 初始化
     * @param context
     */
    public void init(Context context){
       init(context,DEFAULT_TASK_AMOUNT);
    }

    /**
     * 初始化
     * @param context
     * @param taskAmount 最大同时下载任务数量
     */
    public void init(Context context,int taskAmount){
        if (initialized) return;
        mDataHelper = new DataHelper(context);
        sExecutor = Executors.newCachedThreadPool();
        sDownloadPermit = new Semaphore(taskAmount);
        sThreadNum = DEFAULT_THREAD_AMOUNT;
        mTaskDispatcher = new TaskDispatcher();
        mTaskDispatcher.start();
        initialized = true;
        loadAll();
    }
    public Collection<M3U8DownloadRecord> getAllTasks(){
        return sRecordMap.values();
    }

    /**
     * 注册监听
     * @param listener
     */
    public void registerListener(IDownloadListener listener){
        if (listener == null) return;
        for (WeakReference<IDownloadListener> weakReference : listeners){
            if (weakReference.get() == listener){
                return;
            }
        }
        listeners.add(new WeakReference<IDownloadListener>(listener));
    }

    /**
     * 反注册监听
     * @param listener
     */
    public void unRegisterListener(IDownloadListener listener){
        if (listener == null) return;
        for (WeakReference<IDownloadListener> weakReference : listeners){
            if (weakReference.get() == listener){
                listeners.remove(weakReference);
                return;
            }
        }
    }

    /**
     * 添加下载任务到队列中
     * @param request
     */
    public void enqueue(M3U8DownloadRequest request){
        enqueue(request,false);
    }

    /**
     * 把下载任务加入队列中
     * @param request
     * @param start 是否立即下载该任务
     */
    public void enqueue(M3U8DownloadRequest request,boolean start){
        if (request == null || TextUtils.isEmpty(request.getTaskId())){
            return ;
        }
        if (start){
            //暂时处理为停止所有任务，保证此任务能马上执行
            //后续可优化为停止最后启动的任务，让出一个资源即可
            stopAllTask();
        }
        M3U8DownloadRecord record = sRecordMap.get(request.getTaskId());
        if (record!=null){
            reEnqueue(record.getTaskId());
        }else {
            record = new M3U8DownloadRecord(request);
            sRecordMap.put(request.getTaskId(), record);
            mDataHelper.saveRecord(record);
            mTaskDispatcher.enqueueRecord(record);
            notifyNewTaskAdd(record);
            notifyEnqueue(record);
        }
    }

    public boolean reEnqueue(String taskId){
        if (sRecordMap.get(taskId)!=null){
            if (sRecordMap.get(taskId).getDownloadState()==STATE_FINISHED){
                taskFinished(sRecordMap.get(taskId));
                return false;
            }else if (sRecordMap.get(taskId).getDownloadState() == STATE_FAILED){
                sRecordMap.get(taskId).setDownloadState(STATE_INITIAL);
            }else {
                sRecordMap.get(taskId).setDownloadState(STATE_REENQUEUE);
            }
            mTaskDispatcher.enqueueRecord(sRecordMap.get(taskId));
            return true;
        }
        return false;
    }


    /**
     * 开始下载任务
     * @param record
     */
    protected void start(M3U8DownloadRecord record) {
        record.reset();
        record.setDownloadState(STATE_DOWNLOADING);
        new M3U8DownloadTask().executeOnExecutor(sExecutor, record);
        notifyStart(record);
    }
    /**
     * 重新开始下载暂停的任务
     * @param record
     */
    protected void reStart(M3U8DownloadRecord record) {
        if (record!= null) {
            record.setDownloadState(STATE_DOWNLOADING);
            for (int i = 0; i < record.getSubTaskList().size(); i++) {
                sExecutor.execute(record.getSubTaskList().get(i));
            }
            notifyStart(record);
        }
    }

    /**
     * 暂停一个任务
     * 只针对STATE_REENQUEUE和STATE_DOWNLOADING的任务有效
     * 其他情况返回false
     * @param taskId
     * @return
     */
    public boolean pause(String taskId){
        M3U8DownloadRecord record = sRecordMap.get(taskId);
        if (record != null) {
            if (record.getDownloadState() == STATE_REENQUEUE
                    || record.getDownloadState() == STATE_DOWNLOADING)
            sRecordMap.get(taskId).setDownloadState(STATE_PAUSED);
            sDownloadPermit.release();
            notifyPaused(record);
            return true;
        }
        return false;
    }

    /**
     * 当前任务是否在下载中
     * @param taskId
     * @return
     */
    public boolean isDownloading(String taskId){
        if (TextUtils.isEmpty(taskId)) return false;
        M3U8DownloadRecord record = sRecordMap.get(taskId);
        if (record!=null&&record.getDownloadState() == STATE_DOWNLOADING){
            return true;
        }
        return false;
    }

    /**
     * 当前任务是否已经下载完成
     * @param taskId
     * @return
     */
    public boolean isDownLoadFinished(String taskId){
        if (TextUtils.isEmpty(taskId)) return false;
        M3U8DownloadRecord record = sRecordMap.get(taskId);
        if (record!=null&&record.getDownloadState() == STATE_FINISHED){
            return true;
        }
        return false;
    }
    /**
     * 更新下载进度
     * @param record
     */
    protected void progressUpdated(M3U8DownloadRecord record) {
        notifyProgress(record);
        saveRecord(record);
    }

    /**
     * 任务下载结束
     * @param record
     */
    protected void taskFinished(M3U8DownloadRecord record) {
        Log.d(TAG,"[taskFinished]+++++++++");
        if (createM3U8File(record.getDownloadDir(),record.getDownloadName(),record.getM3U8())){
            sDownloadPermit.release();
            record.setDownloadState(STATE_FINISHED);
            saveRecord(record);
            notifyFinish(record);
        }else{
            record.setDownloadState(STATE_FAILED);
            saveRecord(record);
            notifyFailed(record,"生成m3u8文件失败");
        }
    }
    protected void fileLengthSet(M3U8DownloadRecord record) {
        mDataHelper.saveRecord(record);
    }

    /**
     * 任务下载失败
     * @param record
     * @param errorMsg
     */
    protected void downloadFailed(M3U8DownloadRecord record, String errorMsg) {
        sDownloadPermit.release();
        record.setDownloadState(STATE_FAILED);
        saveRecord(record);
        notifyFailed(record,errorMsg);
    }
    synchronized protected void saveRecord(M3U8DownloadRecord record) {
        mDataHelper.saveRecord(record);
    }

    /**
     * 获取全部任务
     */
    protected void loadAll(){
        for (M3U8DownloadRecord record : mDataHelper.loadAllRecords()){
            if (record.getDownloadState() != STATE_FINISHED
                    &&record.getDownloadState()!=STATE_INITIAL
                    &&record.getDownloadState()!=STATE_CANCELED){
                record.setDownloadState(STATE_PAUSED);
            }
            sRecordMap.put(record.getTaskId(),record);
        }
    }
    protected void saveAll(){
        mDataHelper.saveAllRecord(sRecordMap.values());
    }

    public void destroy(){
        mTaskDispatcher.quit();
        stopAllTask();
        saveAll();
        sRecordMap.clear();
        sRecordMap = null;
        sDownloadPermit = null;
        sExecutor = null;
        instance = null;
        initialized = false;
    }
    protected void stopAllTask(){
        for (M3U8DownloadRecord record : sRecordMap.values()) {
            pause(record.getTaskId());
        }
    }

    /**
     * 删除
     * @param taskId
     * @return
     */
    public boolean deleteTask(String taskId){
        if (TextUtils.isEmpty(taskId)) return false;
        M3U8DownloadRecord record = sRecordMap.get(taskId);
        if (record!=null){
            sRecordMap.remove(record);
            mDataHelper.deleteRecord(record);
            return true;
        }
        return false;
    }

    /**
     * 获取对应type下全部已完成的任务任务
     * @param taskType
     * @return
     */
    public List<M3U8DownloadRecord> loadFinishedRecordsByType(int taskType){
        List<M3U8DownloadRecord> list = new ArrayList<>();
        for (M3U8DownloadRecord record :sRecordMap.values()){
            if (record.getTaskType() == taskType
                    &&record.getDownloadState() == STATE_FINISHED){
                list.add(record);
            }
        }
        return list;
    }


    private boolean createM3U8File(String dirPath,String fileName, M3U8 m3U8) {
        Log.d(TAG,"[createM3U8File]+++++++++");
        List<M3U8Item> m3U8ItemList = m3U8.getTsList();
        StringBuilder m3u8StringBuilder = new StringBuilder();

        m3u8StringBuilder.append("#EXTM3U").append('\n');
        m3u8StringBuilder.append("#EXT-X-VERSION:3").append('\n');
        m3u8StringBuilder.append("#EXT-X-TARGETDURATION:18").append('\n');
        m3u8StringBuilder.append("#EXT-X-MEDIA-SEQUENCE:0").append("\n\n");

        for (M3U8Item m3U8Item :  m3U8ItemList) {
            if (m3U8Item.getType()==M3U8Item.TYPE_KEY) {
                m3u8StringBuilder.append(String.format("#EXT-X-KEY:METHOD=AES-128,URI=\"%s\",IV=0x4f97f9624a3f62d77a575236b4caba32",
                        this.coverUriToLocal(m3U8Item.getFileName())));
            } else {
                m3u8StringBuilder.append(String.format("#EXTINF:%f,\n%s", m3U8Item.getSeconds(),
                        this.coverUriToLocal(m3U8Item.getFileName())));
            }
            m3u8StringBuilder.append('\n');
        }

        m3u8StringBuilder.append("#EXT-X-ENDLIST");
        try {
            File file = new File(dirPath, fileName);
            if (!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            FileUtils.writeStringToFile(file, m3u8StringBuilder.toString());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String coverUriToLocal(String uri) {
        return String.format("%s", uri);
    }



    /**=======监听回调=========**/
    private void notifyNewTaskAdd(M3U8DownloadRecord record){
        for (WeakReference<IDownloadListener> weakReference : listeners){
            weakReference.get().onNewTaskAdd(record);
        }
    }
    private void notifyEnqueue(M3U8DownloadRecord record){
        for (WeakReference<IDownloadListener> weakReference : listeners){
            weakReference.get().onEnqueue(record);
        }
    }
    private void notifyProgress(M3U8DownloadRecord record){
        for (WeakReference<IDownloadListener> weakReference : listeners){
            weakReference.get().onProgress(record);
        }
    }
    private void notifyFailed(M3U8DownloadRecord record, String errMsg){
        for (WeakReference<IDownloadListener> weakReference : listeners){
            weakReference.get().onFailed(record,errMsg);
        }
    }
    private void notifyStart(M3U8DownloadRecord record){
        for (WeakReference<IDownloadListener> weakReference : listeners){
            weakReference.get().onStart(record);
        }
    }
    private void notifyPaused(M3U8DownloadRecord record){
        for (WeakReference<IDownloadListener> weakReference : listeners){
            weakReference.get().onPaused(record);
        }
    }
    private void notifyFinish(M3U8DownloadRecord record){
        for (WeakReference<IDownloadListener> weakReference : listeners){
            weakReference.get().onFinish(record);
        }
    }
}
