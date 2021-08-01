package org.sixelasavir.product.conexamarket.extensible

fun <T> List<T>.getListIsNotEmpty(): List<T>? =
    if (!isNullOrEmpty()) {
        this
    } else null