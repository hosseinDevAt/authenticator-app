package com.example.authentication.remote

import com.example.authentication.remote.apiRepository.LoginApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {

    private const val baseUrl = "https://learn.alirezaahmadi.info/api/v1/auth/"


    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val loginApiService: LoginApiService = retrofit.create(LoginApiService::class.java)
}