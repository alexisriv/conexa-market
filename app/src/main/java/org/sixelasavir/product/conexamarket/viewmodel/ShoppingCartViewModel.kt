package org.sixelasavir.product.conexamarket.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers.io
import org.sixelasavir.product.conexamarket.dao.Cart
import org.sixelasavir.product.conexamarket.extensible.subscribeTo
import org.sixelasavir.product.conexamarket.repository.ItemCartRoomRepository
import org.sixelasavir.product.conexamarket.utils.Event

class ShoppingCartViewModel(
    private val repositoryRoom: ItemCartRoomRepository = ItemCartRoomRepository()
) : BaseViewModel() {

    private val _items: MutableLiveData<Event<List<ItemShoppingCart>>> = MutableLiveData()
    val items: LiveData<Event<List<ItemShoppingCart>>>
        get() = _items

    private val _cart: MutableLiveData<Event<Cart>> = MutableLiveData()
    val cart: LiveData<Event<Cart>>
        get() = _cart

    fun loadShoppingCartItems() {
        repositoryRoom.getItems()
            .subscribeTo({
                _items.postValue(Event(it.map { i0 -> ItemShoppingCart(i0) }))
            }, ::onError).addTo(compositeDisposable)
    }

    fun loadCart() {
        repositoryRoom.getCart()
            .subscribeTo({
                _cart.postValue(Event(it))
            }, ::onError)
            .addTo(compositeDisposable)
    }

    fun deleteAll() {
        repositoryRoom.deleteAll()
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe({
                _items.postValue(Event(arrayListOf()))
                _cart.postValue(Event(Cart(0L, 0F)))
            }, ::onError).addTo(compositeDisposable)
    }

    private fun onError(t: Throwable) {
        println("MESSAGE_ERROR - ${t.message}")
    }
}
