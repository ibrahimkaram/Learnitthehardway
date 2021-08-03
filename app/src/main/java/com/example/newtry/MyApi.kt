package com.example.newtry

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MyApi {

    @GET("d3039a033ca2bfacec6c8c6baf69e3799056f486")
    suspend fun getData(): Response<DiffWatch>


}