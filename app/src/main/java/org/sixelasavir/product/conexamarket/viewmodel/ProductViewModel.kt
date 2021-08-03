package org.sixelasavir.product.conexamarket.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers.io
import org.sixelasavir.product.conexamarket.model.Product
import org.sixelasavir.product.conexamarket.repository.ItemCartRoomRepository
import org.sixelasavir.product.conexamarket.repository.ProductRepository
import org.sixelasavir.product.conexamarket.utils.Event

class ProductViewModel(
    private val repository: ProductRepository = ProductRepository(),
    private val repositoryRoom: ItemCartRoomRepository = ItemCartRoomRepository()
) : BaseViewModel() {

    private val _products: MutableLiveData<Event<List<ProductItem>>> = MutableLiveData()
    val products: LiveData<Event<List<ProductItem>>>
        get() = _products

    private val _categories: MutableLiveData<Event<List<String>>> = MutableLiveData()
    val categories: LiveData<Event<List<String>>>
        get() = _categories

    private val _shoppingCart: MutableLiveData<Event<Long>> = MutableLiveData()
    val shoppingCart: LiveData<Event<Long>>
        get() = _shoppingCart

    fun loadShoppingCart() {
        repositoryRoom.getCart()
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe {
                _shoppingCart.postValue(Event(it.count))
            }.addTo(compositeDisposable)
    }

    fun loadProducts() {
        repositoryRoom.getItems().concatMap { items ->
            repository.getProduct().concatMap { products ->
                products.forEach { prod ->
                    items.forEach { item ->
                        if (prod.id == item.uid) prod.count = item.count
                    }
                }
                Observable.just(products)
            }
        }.subscribeOn(io())
            .observeOn(mainThread())
            .subscribe { products ->
                products.map(::loadProductItem)
                    .let { _products.postValue(Event(it)) }
            }.addTo(compositeDisposable)
    }

    fun loadProductCategories() {
        repository.getProductCategories()
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe { categories ->
                _categories.postValue(Event(categories))
            }.addTo(compositeDisposable)
    }

    fun loadProductsByCategory(category: String) {
        repositoryRoom.getItems().concatMap { items ->
            repository.getProductByCategory(category).map { products ->
                products.forEach { prod ->
                    items.forEach { item ->
                        if (prod.id == item.uid) prod.count = item.count
                    }
                }

                return@map products
            }
        }.subscribeOn(io())
            .observeOn(mainThread())
            .subscribe { products ->
                products.map(::loadProductItem)
                    .let { _products.postValue(Event(it)) }
            }.addTo(compositeDisposable)
    }

    private fun loadProductItem(product: Product): ProductItem =
        ProductItem(product)

    fun saveProductInShoppingCart(product: Product) {
        repositoryRoom.saveItemAndGetCount(product)
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe { count ->
                _shoppingCart.postValue(Event(count))
            }.addTo(compositeDisposable)
    }
}
