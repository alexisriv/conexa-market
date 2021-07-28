package org.sixelasavir.product.conexamarket.tools

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.reflect.KClass

object ClientBuilder {
    private const val BASE_URL = "https://fakestoreapi.com"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T : Any> service(tClass: KClass<T>): T = retrofit.create(tClass.java)
}