package com.wys.commonbaselib.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.aixuexi.gushi.R;
import com.wys.commonbaselib.adapter.TestAdapter;
import com.wys.commonbaselib.adapter.TestMulAdapter;
import com.wys.module_common_ui.widget.RandomCodeView;
import com.wys.module_common_ui.widget.recycler.XRecyclerView;

import java.util.ArrayList;

public class RecyclerViewActivity extends AppCompatActivity {
    private XRecyclerView recycler_view;
    private TestAdapter testAdapter;
    private TestMulAdapter mulAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setEmptyView(findViewById(R.id.tv_empty));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

        TextView header=new TextView(this);
        RandomCodeView codeView = new RandomCodeView(this);
        codeView.setLayoutParams(new RecyclerView.LayoutParams(200,
                100));
        header.setText("this is a headerView");
//
//        recycler_view.addHeaderView(codeView);
//        recycler_view.addHeaderView(header);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recycler_view);


        recycler_view.setLayoutManager(layoutManager);
//        testAdapter = new TestAdapter(this,getTestStrings());
//        recycler_view.setAdapter(testAdapter);
        mulAdapter = new TestMulAdapter(this,getTestStrings());
        recycler_view.setAdapter(mulAdapter);
    }

    private ArrayList<String> getTestStrings(){
        ArrayList<String> mList = new ArrayList<>();
        for (int i=0;i<9;i++){
//            if (i%3 == 0){
//                mList.add("aaa001");
//            }
            mList.add("item0"+(i+1));
        }
        return mList;
    }

    public void onClick(View view) {
        testAdapter.reSetList(null);
    }
}
