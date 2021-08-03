package com.example.newtry

import retrofit2.Response

class Repository {

    suspend fun getPost(): Response<DiffWatch> {
      return  RetrofitInstance.api.getData()

    }

}