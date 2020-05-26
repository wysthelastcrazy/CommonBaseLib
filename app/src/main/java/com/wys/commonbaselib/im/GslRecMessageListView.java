package com.wys.commonbaselib.im;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wys.commonbaselib.R;
import com.wys.module_common_ui.widget.recycler.XRecyclerView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yas on 2020-02-19
 * Describe:
 */
public class GslRecMessageListView extends LinearLayout {
    private XRecyclerView mRecyclerView;
    private LinearLayoutManager linearManager;
    private GslMessageAdapter msgAdapter;
    private LinkedList<GslRecMessage> mData = new LinkedList<>();

    private TextView tvRoomId;
    private String userId;

    public GslRecMessageListView(Context context, ViewGroup parent, String userId) {
        super(context);
        parent.addView(this, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        this.userId = userId;
        initView(context);
    }
    private void initView(Context context){
        View contentView = LayoutInflater.from(context).inflate(R.layout.layout_rec_message_list_view, this);
        {
            tvRoomId = contentView.findViewById(R.id.tvRoomId);
        }

        {
            mRecyclerView = contentView.findViewById(R.id.rlvMessage);
            linearManager = new LinearLayoutManager(context,RecyclerView.VERTICAL,false);
            mRecyclerView.setLayoutManager(linearManager);
            mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            mRecyclerView.setEmptyView(contentView.findViewById(R.id.tvEmptyView));
            msgAdapter = new GslMessageAdapter(context,mData);
            mRecyclerView.setAdapter(msgAdapter);
        }
    }

    /**
     * 添加消息到聊天列表中
     * @param message
     */
    synchronized public void putMessage(GslRecMessage message){
        mData.add(message);
        refreshUI();
    }

    /**
     * 批量添加消息到聊天列表中（seek时一次多条消息添加）
     * @param messages
     */
    synchronized public void putMessages(ArrayList<GslRecMessage> messages){
        mData.addAll(messages);
        refreshUI();
    }

    /**
     * 更新消息列表
     * @param messages
     */
    synchronized public void updateMessages(ArrayList<GslRecMessage> messages){
        mData.clear();
        mData.addAll(messages);
        refreshUI();
    }
    private void refreshUI(){
        msgAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(mData.size()-1);
    }
    public void setRoomId(String roomId){
        tvRoomId.setText(roomId);
    }
    private int px(Context context,int dp){
        return (int) (context.getResources().getDisplayMetrics().density*dp+0.5);
    }

    class GslMessageAdapter extends RecyclerView.Adapter<ViewHolder> {
        private WeakReference<Context> mContext;
        private List<GslRecMessage> data;
        public GslMessageAdapter(Context context,List<GslRecMessage> data){
            mContext = new WeakReference(context);
            this.data = data;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gsl_rec_item_msg,viewGroup,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
            if (data == null) return;
            GslRecMessage msg = data.get(position);
            boolean isSelf = false;
            String role = msg.getNickName();
            int roleColor = Color.parseColor("#BBBFC1");
//            if ("teacher".equals(msg.getUserRole())){
//                role = "[老师] "+msg.getNickName()+" :";
//                roleColor = Color.parseColor("#00BE7B");
//            }else if (msg.isSelf()||msg.getUserId().equals(userId)){
//                role = ": [我] "+msg.getNickName();
//                roleColor = Color.parseColor("#00BE7B");
//                isSelf = true;
//            }


            if(position%4 == 0||position%4==1){
                role = !TextUtils.isEmpty(msg.getNickName())?msg.getNickName():"";
                roleColor = Color.parseColor("#BBBFC1");
                isSelf = false;
            }else if (position%4 == 2||msg.getUserId().equals(userId)){
                role = "[我] "+msg.getNickName();
                roleColor = Color.parseColor("#00BE7B");
                isSelf = true;
            }else{
                role = "[老师] "+msg.getNickName();
                roleColor = Color.parseColor("#00BE7B");
                isSelf = false;
            }

            String content = msg.getContent();
            SpannableStringBuilder span = getFace(mContext.get(),content);
            viewHolder.tvUserName.setTextColor(roleColor);
            if (isSelf){
                viewHolder.tvUserName.setGravity(Gravity.RIGHT);
                viewHolder.tvContentLeft.setVisibility(GONE);
                viewHolder.tvContentRight.setVisibility(VISIBLE);
                viewHolder.tvContentRight.setText(span);
            }else{
                viewHolder.tvUserName.setGravity(Gravity.LEFT);
                viewHolder.tvContentRight.setVisibility(GONE);
                viewHolder.tvContentLeft.setVisibility(VISIBLE);
                viewHolder.tvContentLeft.setText(span);
            }
            viewHolder.tvUserName.setText(role);
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
        public TextView tvUserName;
        public TextView tvContentLeft;
        public TextView tvContentRight;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvContentLeft = itemView.findViewById(R.id.tvContentLeft);
            tvContentRight = itemView.findViewById(R.id.tvContentRight);
        }
    }
    private SpannableStringBuilder getFace(Context context,String content){
        if (content == null || TextUtils.isEmpty(content)){
            return new SpannableStringBuilder(!TextUtils.isEmpty(content)?content:"");
        }
        SpannableStringBuilder spanBuilder = new SpannableStringBuilder(content);
        String regex = "\\[.{1,3}\\]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()){
            String tempText = matcher.group();
            try {
                String png = tempText.substring(1,tempText.length()-1)+".png";
                Bitmap bitmap = BitmapFactory.decodeStream(context.getAssets().open("gslRecface/" +png));
                ImageSpan imageSpan = new ImageSpan(context,bitmap);
                spanBuilder.setSpan(imageSpan, matcher.start(),matcher.end(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return spanBuilder;
    }
}
