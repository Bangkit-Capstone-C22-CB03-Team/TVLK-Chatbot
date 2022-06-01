package com.bangkit.capstone.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("get")
    fun getResponse(
        @Query("bid") bid:String ="166439",
        @Query("key") key:String = "8WSWqYKMR7CQkWBu",
        @Query("uid") uid:Int = 2,
        @Query("msg") message:String
    ): Call<BotResponse>

}
