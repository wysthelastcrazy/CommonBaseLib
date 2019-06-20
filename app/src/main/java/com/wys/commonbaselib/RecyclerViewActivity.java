package com.wys.commonbaselib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wys.commonbaselib.adapter.TestAdapter;
import com.wys.module_common_ui.widget.RandomCodeView;
import com.wys.module_common_ui.widget.recycler.XRecyclerView;

import java.util.ArrayList;

public class RecyclerViewActivity extends AppCompatActivity {
    private XRecyclerView recycler_view;
    private TestAdapter testAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setEmptyView(findViewById(R.id.tv_empty));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        TextView header=new TextView(this);
        RandomCodeView codeView = new RandomCodeView(this);
        codeView.setLayoutParams(new RecyclerView.LayoutParams(200,
                100));
        header.setText("this is a headerView");

        recycler_view.addHeaderView(codeView);
        recycler_view.addHeaderView(header);

        recycler_view.setLayoutManager(layoutManager);
        testAdapter = new TestAdapter(this,getTestStrings());
        recycler_view.setAdapter(testAdapter);
    }

    private ArrayList<String> getTestStrings(){
        ArrayList<String> mList = new ArrayList<>();
        for (int i=0;i<9;i++){
            mList.add("item0"+(i+1));
        }
        return mList;
    }

    public void onClick(View view) {
        testAdapter.reSetList(null);
    }
}
