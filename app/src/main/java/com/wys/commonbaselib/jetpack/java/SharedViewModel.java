package com.wys.commonbaselib.jetpack.java;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @author wangyasheng
 * @date 2020/6/11
 * @Describe:Fragment共享数据ViewModel
 */
class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> selected = new MutableLiveData<>();

    public void select(String str){
        selected.setValue(str);
    }
    public LiveData<String> getSelected(){
        return selected;
    }
}
