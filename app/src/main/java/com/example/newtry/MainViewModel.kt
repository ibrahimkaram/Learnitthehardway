package com.example.newtry

import android.support.v4.os.IResultReceiver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository): ViewModel() {

    val myresp: MutableLiveData<Response<DiffWatch>> = MutableLiveData()


    fun getPost(){
        viewModelScope.launch {
            val response = repository.getPost()
            myresp.value = response


        }


    }

}

