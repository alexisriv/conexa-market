package org.sixelasavir.product.conexamarket.repository

import io.reactivex.Observable
import org.sixelasavir.product.conexamarket.AppDatabase
import org.sixelasavir.product.conexamarket.dao.Cart
import org.sixelasavir.product.conexamarket.dao.ItemCartDao
import org.sixelasavir.product.conexamarket.model.Product
import org.sixelasavir.product.conexamarket.model.entity.ItemCart

class ItemCartRoomRepository(
    private val dao: ItemCartDao = AppDatabase.instance.itemCartDao()
) {

    fun saveItemAndGetCount(product: Product): Observable<Long> {
        val cartObserver = dao.getCart()

        return with(product) {
            ItemCart(
                uid = id,
                title = title,
                image = image,
                price = price,
                count = count
            )
        }.let { item ->
            dao.insert(item)
                .andThen(cartObserver)
                .map { cart ->
                    cart.count
                }
        }
    }

    fun getItems(): Observable<List<ItemCart>> = dao.getAll()

    fun getCart(): Observable<Cart> = dao.getCart()
}
