package org.sixelasavir.product.conexamarket.repository

import io.reactivex.Observable
import org.sixelasavir.product.conexamarket.api.ProductApi
import org.sixelasavir.product.conexamarket.model.Product
import org.sixelasavir.product.conexamarket.tools.ClientBuilder.service

class ProductRepository(
    private val productApi: ProductApi = service(ProductApi::class)
) {

    fun getProduct(): Observable<List<Product>> =
        productApi.getProducts()

    fun getProductCategories(): Observable<List<String>> =
        productApi.getProductCategories()

    fun getProductByCategory(category: String): Observable<List<Product>> =
        productApi.getProductsByCategory(category)
}
