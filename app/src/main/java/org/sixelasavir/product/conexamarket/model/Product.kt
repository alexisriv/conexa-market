package org.sixelasavir.product.conexamarket.model

data class Product(
    val id: Long,
    val title: String,
    val price: Float,
    val description: String,
    val category: String,
    val image: String,
    var count: Int
)
