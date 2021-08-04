package com.example.newtry

import com.example.newtry.pulls.MyPulls
import retrofit2.Response

class Repository {

    suspend fun getPost(): Response<DiffWatch> {
      return  RetrofitInstance.api.getData()

    }
    suspend fun getPulls(): Response<MyPulls> {
        return  RetrofitInstance.api2.getData()

    }


}