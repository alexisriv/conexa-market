package org.sixelasavir.product.conexamarket.api

import io.reactivex.Observable
import org.sixelasavir.product.conexamarket.model.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductApi {

    @GET("/products?sort=desc")
    fun getProducts(): Observable<List<Product>>

    @GET("/products/categories")
    fun getProductCategories(): Observable<List<String>>

    @GET("/products/category/{category}")
    fun getProductsByCategory(@Path("category") category: String): Observable<List<Product>>
}
