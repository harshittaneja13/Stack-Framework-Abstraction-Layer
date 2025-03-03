package com.example.demo.api

import com.example.demo.models.ApiResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface{
    @GET("p6764/{test_mint}")

    suspend fun getStackItems(@Path("test_mint") test_mint: String) : Response<ApiResponse>

}