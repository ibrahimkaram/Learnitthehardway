package com.example.newtry

import com.example.newtry.DiffWatch
import com.example.newtry.pulls.MyPulls
import retrofit2.Response
import retrofit2.http.GET


interface MySeconIPA {

    @GET("pulls")
    suspend fun getData(): Response<MyPulls>


}