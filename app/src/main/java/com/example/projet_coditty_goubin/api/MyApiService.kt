package com.example.projet_coditty_goubin.api

import com.example.projet_coditty_goubin.model.Card
import com.example.projet_coditty_goubin.model.User
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


private const val BASE_URL = "https://projetcoditty.herokuapp.com"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface MyApiService {
    @GET("/card/getCards")
    fun getCards(): Deferred<List<Card>>

    @POST("/user/create")
    fun createUser(@Body user: User): Deferred<User>

    @GET("/user/getUsers")
    fun getUsers(): Deferred<List<User>>

}

object MyApi {
    val retrofitService: MyApiService by lazy { retrofit.create(MyApiService::class.java) }
}
