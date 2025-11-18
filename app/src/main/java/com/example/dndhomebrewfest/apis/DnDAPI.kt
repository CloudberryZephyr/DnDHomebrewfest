package com.example.dndhomebrewfest.apis

import com.example.dndhomebrewfest.viewmodels.DnDCategory
import com.example.dndhomebrewfest.viewmodels.Object
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path

interface DndAPIService {

    @GET("/api/2014/{category}")
    suspend fun getCategory(@Path("category") category : String) : DnDCategory

    @GET("{path}")
    suspend fun getObjectInfo(@Path("path") path : String) : Object
}

object DnDAPI {

    private val base_url = "https://www.dnd5eapi.co/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json{ignoreUnknownKeys = true}.asConverterFactory("application/json".toMediaType()))
        .baseUrl(base_url)
        .build()

    val retrofitService : DndAPIService by lazy{
        retrofit.create(DndAPIService::class.java)
    }

}