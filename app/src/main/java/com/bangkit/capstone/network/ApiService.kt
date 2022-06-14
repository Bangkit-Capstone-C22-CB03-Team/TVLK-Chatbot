package com.bangkit.capstone.network

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST(".")
    fun getResponse(
        @Query("msg") msg:String,
        @Query("categid") categid:Int
    ): Call<BotResponse>

}
