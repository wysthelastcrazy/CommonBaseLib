package com.wys.module_common_ui.widget.recycler.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by yas on 2017/7/19.
 */

public abstract class BaseMultiTypeRecyclerAdapter<T>extends RecyclerView.Adapter<BaseViewHolder> implements View.OnClickListener {
    protected Context mContext;
    private ArrayList<T> mList;
    private OnItemClickListener<T> itemClickListener;
    public BaseMultiTypeRecyclerAdapter(Context mContext, ArrayList<T> mList) {
        this.mContext=mContext;
        this.mList = mList;
    }
    protected abstract int getItemLayout(int viewType);
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(getItemLayout(viewType), parent, false);
        return getViewHolder(itemView,viewType);
    }

    @Override
    public int getItemViewType(int position) {
        return getTypeByPos(position);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (holder!=null){
            holder.setPos(position);
            holder.setSize(getItemCount());
            holder.initView();
            holder.mContext = mContext;
            holder.setValues(mList.get(position));
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(this);
        }
    }
    @Override
    public int getItemCount() {
        if (mList!=null){
            return mList.size();
        }
        return 0;
    }

    /**
     * 设置item点击回调
     * @param itemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener itemClickListener){
         this.itemClickListener=itemClickListener;
    }

    public void appendList(ArrayList<T> mList){
        if(this.mList == null){
            this.mList = mList;
        }else{
            this.mList.addAll(mList);
        }
        notifyDataSetChanged();
    }

    public void reSetList(ArrayList<T> mList){
        this.mList = mList;
        notifyDataSetChanged();
    }
    public T getItemEntity(int pos){
        if (mList!=null&&pos>=0&&mList.size()>pos){
            return mList.get(pos);
        }
        return null;
    }
    public ArrayList<T> getList(){
        return mList;
    }
    protected abstract BaseViewHolder getViewHolder(View itemView,int viewType);

    protected abstract int getTypeByPos(int position);

    @Override
    public void onClick(View v) {
        if (itemClickListener!=null){
            int pos= (int) v.getTag();
            itemClickListener.onItemClick(v,mList.get(pos),pos);
        }
    }
}
