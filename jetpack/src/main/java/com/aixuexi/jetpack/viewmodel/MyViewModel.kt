package com.aixuexi.jetpack.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aixuexi.jetpack.bean.User
import java.util.logging.Handler

/**
 * @author wangyasheng
 * @date 2020/8/5
 * @Describe:
 */
class MyViewModel : ViewModel() {
    private val users: MutableLiveData<List<User>> by lazy {
        MutableLiveData<List<User>>().also {
            loadUsers(it)
        }
    }
    fun getUsers(): LiveData<List<User>>{
        return users
    }
    private fun loadUsers(it:MutableLiveData<List<User>>){
        //do an asynchronous operation to fetch users
        it.value = listOf(User("wys",18),User("dnd",16))
    }
    fun updateUser(){
        users.value = listOf(User("wys",18),User("dnd",16))
    }
}