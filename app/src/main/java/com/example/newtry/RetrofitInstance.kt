package com.example.newtry

import com.example.newtry.Constants.Companion.MY_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(MY_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
    val api: MyApi by lazy {
        retrofit.create(MyApi::class.java)

    }


}