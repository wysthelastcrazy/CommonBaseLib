package com.aixuexi.jetpack.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aixuexi.jetpack.bean.User
import kotlinx.coroutines.delay
import java.util.logging.Handler

/**
 * @author wangyasheng
 * @date 2020/8/5
 * @Describe:
 */
class MyViewModel : ViewModel() {
    private val users: MutableLiveData<MutableList<User>> by lazy {
        MutableLiveData<MutableList<User>>().also {
            loadUsers(it)
        }
    }
    private val name: MutableLiveData<String> by lazy {
        MutableLiveData<String>().also {
            it.value = "init"
        }
    }
    fun getName():LiveData<String>{
        return name
    }
    fun getUsers(): LiveData<MutableList<User>>{
        return users
    }
    private fun loadUsers(it:MutableLiveData<MutableList<User>>){
        //do an asynchronous operation to fetch users
        it.value = mutableListOf(User("wys",18),User("dnd",16))
    }
    fun updateUser(){
        users.value = mutableListOf(User("wys",18),User("dnd",16))
//        users.value?.add(User("dnd",16))
    }
}