package com.wys.commonbaselib.im;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wys.commonbaselib.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yas on 2020-02-19
 * Describe:
 */
public class GslRecordMessageListView extends LinearLayout {
    private RecyclerView mRecyclerView;
    private LinearLayoutManager linearManager;
    private GslMessageAdapter msgAdapter;
    private LinkedList<GslRecordMessage> mData = new LinkedList<>();
    private ArrayList<GslRecordMessage> mCacheData = new ArrayList<>();

    private int DATA_CAPACITY = 100; //设置存储个数

    private TextView tvRoomId;

    private Timer mTimer = new Timer();
    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {
            if (mCacheData.size() == 0){
                return;
            }
            mHandler.sendEmptyMessage(1);
        }
    };

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (mCacheData!=null&&mCacheData.size()>0){
                int cacheSize = mCacheData.size();
                limitCapacity(mData,mCacheData);
                mCacheData.clear();
                msgAdapter.notifyDataSetChanged();

                int lastVisibleIndex = linearManager!=null?linearManager.findLastVisibleItemPosition():0;
                if (lastVisibleIndex+1+cacheSize == mData.size()){
                    mRecyclerView.scrollToPosition(mData.size()-1);
                }
            }
        }
    };

    public GslRecordMessageListView(Context context) {
        super(context);
        initView(context);
    }
    private void initView(Context context){
        setOrientation(VERTICAL);
        {
            tvRoomId = new TextView(context);
            LayoutParams roomIdParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            tvRoomId.setLayoutParams(roomIdParams);
            tvRoomId.setPadding(px(context, 8), px(context, 4), 0, px(context, 4));
            tvRoomId.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12f);
            tvRoomId.setTextColor(Color.parseColor("#999999"));
            tvRoomId.setText("房间号：");
            addView(tvRoomId);
        }

        {
            mRecyclerView = new RecyclerView(context);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
            mRecyclerView.setLayoutParams(params);

            linearManager = new LinearLayoutManager(context,RecyclerView.VERTICAL,false);

            mRecyclerView.setLayoutManager(linearManager);
            mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            mRecyclerView.setPadding(px(context,8),0,0,px(context,0));
            mRecyclerView.addItemDecoration(new SpacesItemDecoration(px(context,1)));
            msgAdapter = new GslMessageAdapter(context,mData);
            mRecyclerView.setAdapter(msgAdapter);
            addView(mRecyclerView);
        }

        if (mTimer!=null&&mTimerTask!=null){
            mTimer.schedule(mTimerTask,100,100);
        }
    }

    synchronized private void limitCapacity(LinkedList<GslRecordMessage> desList,
                                            ArrayList<GslRecordMessage> newList){
        try {
            int desSize = desList.size();
            int newSize = newList.size();
            int length = desSize + newSize;

            if (length>DATA_CAPACITY){
                LinkedList<GslRecordMessage> redundanList = new LinkedList<>();
                for (GslRecordMessage message : desList){
                    redundanList.offer(message);
                }
                for (GslRecordMessage message : newList){
                    redundanList.offer(message);
                }
                desList.clear();

                for (int index = length-DATA_CAPACITY;index < length;index--){
                    desList.offer(redundanList.get(index)) ;
                }
            }else{
                for (GslRecordMessage message : newList){
                    desList.offer(message);
                }
            }
        }catch (Exception e){

        }
    }

    /**
     * 添加消息到聊天列表中
     * @param message
     */
    synchronized public void putMessage(GslRecordMessage message){
        if (mCacheData == null){
            mCacheData = new ArrayList<>();
        }
        mCacheData.add(message);
    }

    /**
     * 批量添加消息到聊天列表中（seek时一次多条消息添加）
     * @param messages
     */
    synchronized public void putMessages(ArrayList<GslRecordMessage> messages){
        if (mCacheData == null){
            mCacheData = new ArrayList<>();
        }
        mCacheData.addAll(messages);
    }

    /**
     * 更新消息列表
     * @param messages
     */
    synchronized public void updateMessages(ArrayList<GslRecordMessage> messages){
        if (mCacheData == null){
            mCacheData = new ArrayList<>();
        }
        mData.clear();
        mCacheData.addAll(messages);
    }
    public void setRoomId(String roomId){
        tvRoomId.setText(roomId);
    }
    private int px(Context context,int dp){
        return (int) (context.getResources().getDisplayMetrics().density*dp+0.5);
    }

    class GslMessageAdapter extends RecyclerView.Adapter<ViewHolder> {
        private WeakReference<Context> mContext;
        private List<GslRecordMessage> data;
        public GslMessageAdapter(Context context,List<GslRecordMessage> data){
            mContext = new WeakReference(context);
            this.data = data;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gsl_record_item_msg,null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            if (data == null) return;
            GslRecordMessage msg = data.get(position);
            String role = msg.getNickName()+": ";
            int color = Color.parseColor("#499F2F");
            if ("teacher".equals(msg.getUserRole())){
                role = "老师: ";
                color = Color.parseColor("#FF5C00");
            }else if (mContext.get()!=null&&msg.isSelf()){
                role = "[自己]: ";
                color = Color.parseColor("#666666");
            }
            String content = msg.getContent();

            SpannableStringBuilder span = new SpannableStringBuilder(role+content);
            span.setSpan(new ForegroundColorSpan(color),0,role.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            viewHolder.tvContent.setText(span);
        }

        @Override
        public int getItemCount() {
            if (data!=null){
                return data.size();
            }
            return 0;
        }
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvContent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tvContent);
        }
    }
    class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;
        protected SpacesItemDecoration(int space){
            this.space = space;
        }

        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            if (parent.getChildPosition(view)==0){
                outRect.top = space;
            }
        }
    }
}
