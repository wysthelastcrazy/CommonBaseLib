package com.wys.commonbaselib.activity;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by yas on 2020-01-02
 * Describe:
 */
public class SWActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<B> list = new ArrayList<>();
        B b = new B();
        list.add(b);

        List<A> list1 = new ArrayList<>();
        list1.add(new A());

//        list.addAll(list1);
        list1.addAll(list);
    }
    class A{

    }
    class B extends A{

    }
}
