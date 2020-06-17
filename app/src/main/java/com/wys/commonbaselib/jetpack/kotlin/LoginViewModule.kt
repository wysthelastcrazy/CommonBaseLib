package com.wys.commonbaselib.jetpack.kotlin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * @author wangyasheng
 * @date 2020-06-09
 * @Describe:
 */
class LoginViewModule : ViewModel(){
    private val users: MutableLiveData<List<String>> by lazy{
        MutableLiveData<List<String>>().also {
            loadUsers()
        }
    }
    fun getUsers():LiveData<List<String>>{
        return users
    }
    private fun loadUsers(){
        //do an asynchronous operation to fetch users
        users.value = List<String>(5){
            "user00"+it
        }
    }
}