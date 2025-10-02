package com.harak.encyklopedieZbrani.Interface

import com.harak.encyklopedieZbrani.Model.DetailModel
import com.harak.encyklopedieZbrani.Model.ImagesModel
import com.harak.encyklopedieZbrani.Model.ListModel
import com.harak.encyklopedieZbrani.Model.Model3dModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {
    @GET("api.php")
    fun getListData(
        @Query("api_key") apiKey: String
    ): Call<MutableList<ListModel>>

    @GET("api.php/{detail_id}")
    fun getDetailData(@Path("detail_id") id:Int,@Query("api_key") apiKey: String): Call<DetailModel>

    @GET("image_api.php/{img_id}")
    fun getImageData(@Path("img_id") id:Int): Call<ImagesModel>

    @GET("model_api.php/{model_id}")
    fun get3dModelData(@Path("model_id") id:Int): Call<Model3dModel>

}