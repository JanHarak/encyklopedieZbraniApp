package com.harak.encyklopedieZbrani.Common

import com.harak.encyklopedieZbrani.Interface.RetrofitService
import com.harak.encyklopedieZbrani.Retrofit.RetrofitClient

object Common {
    val WEB_URL = "https://starozitnostiharak.wz.cz"
    private val BASE_URL = "https://starozitnostiharak.wz.cz/AppData/encyklopedie/"
    public val API_KEY = "f9f61f79-fd00-4b49-a40a-ffb6c2904fb4"

    val retrofitService: RetrofitService
        get()= RetrofitClient.getClient(BASE_URL).create(RetrofitService::class.java)
}