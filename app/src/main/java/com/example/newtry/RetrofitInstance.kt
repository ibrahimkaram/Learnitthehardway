package com.example.newtry

import com.example.newtry.Constants.Companion.MY_URL
import com.example.newtry.Constants.Companion.MY_URL2
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy{
        Retrofit.Builder()
            .baseUrl(MY_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
    private val retrofit2 by lazy{
        Retrofit.Builder()
            .baseUrl(MY_URL2)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
    val api: MyApi by lazy {
        retrofit.create(MyApi::class.java)

    }
    val api2: MySeconIPA by lazy {
        retrofit2.create(MySeconIPA::class.java)

    }


}