package com.wys.commonbaselib.jetpack.java;

import com.wys.commonbaselib.jetpack.kotlin.User;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @author wangyasheng
 * @date 2020/6/11
 * @Describe:
 */
class MyViewModel extends ViewModel {
    /***
     * ViewModel绝不能引用视图、Lifecycle或者可能存储对Activity上下文引用的任何类，
     * 因为ViewModel对象的存在时间比视图或LifecycleOwners的特定实例存在的时间更长。
     *
     * 这还意味着，可以更轻松的编写覆盖ViewModel的测试，因为它不了解视图和Lifecycle对象。
     *
     * ViewModel对象可以包含LifecycleObservers，如LiveData对象，但是ViewModel对象
     * 不能观察对生命周期感知型可观察对象（例如LiveData对象）的更改。
     *
     * 如果ViewModel需要Application上下文（例如，为了查找系统服务），
     * 它可以扩展AndroidViewModel类并设置用于接收Application的构造函数，
     * 因为Application类会扩展Context
     *
     *
     * ViewModel的生命周期：
     * ViewModel对象存在的时间范围是获取ViewModel时传递给ViewModelProvider的Lifecycle。
     * ViewModel将一直留在内存中，直到限定其存在时间范围的Lifecycle永久消失：
     * 对于Activity，是在Activity完成时；而对于Fragment，是在Fragment分离时
     */
    private MutableLiveData<List<User>> users;

    public LiveData<List<User>> getUsers(){
        if (users == null){
            users = new MutableLiveData<>();
            loadUsers();
        }
        return users;
    }

    private void loadUsers(){
        //do an asynchronous operation to fetch users
    }

}
