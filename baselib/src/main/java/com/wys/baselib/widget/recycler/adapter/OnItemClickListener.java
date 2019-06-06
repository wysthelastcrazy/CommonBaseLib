package com.wys.baselib.widget.recycler.adapter;

import android.view.View;

/**
 * Created by yas on 2018/4/27.
 */

public interface OnItemClickListener<E> {
    void onItemClick(View itemView, E bean, int position);
}
