package com.example.newtry

import android.support.v4.os.IResultReceiver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newtry.pulls.MyPulls
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private val repository: Repository): ViewModel() {

    val myresp: MutableLiveData<Response<DiffWatch>> = MutableLiveData()
    val myseconresp: MutableLiveData<Response<MyPulls>> = MutableLiveData()



    fun getPull (){
        viewModelScope.launch {
            val responsen2 = repository.getPulls()
            myseconresp.value = responsen2


        }}



    fun getPost(){
        viewModelScope.launch {
            val response = repository.getPost()
            myresp.value = response


        }


    }

}

