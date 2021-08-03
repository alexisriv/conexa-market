package org.sixelasavir.product.conexamarket.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxkotlin.addTo
import org.sixelasavir.product.conexamarket.extensible.subscribeTo
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

    private var isLoadProducts = false
    private var isLoadProductsByCategory = false

    fun loadShoppingCart() {
        repositoryRoom.getCart()
            .subscribeTo({
                _shoppingCart.postValue(Event(it.count))
            }, ::onError).addTo(compositeDisposable)
    }

    fun loadProducts() {
        isLoadProducts = true
        repositoryRoom.getItems().concatMap { items ->
            repository.getProduct().map { products ->
                products.forEach { prod ->
                    items.forEach { item ->
                        if (prod.id == item.uid) prod.count = item.count
                    }
                }
                return@map products
            }
        }.subscribeTo({ products ->
            if (isLoadProducts)
                products.map(::loadProductItem)
                    .let { _products.postValue(Event(it)) }
            isLoadProducts = false
        }, ::onError).addTo(compositeDisposable)
    }

    fun loadProductCategories() {
        repository.getProductCategories()
            .subscribeTo({ categories ->
                _categories.postValue(Event(categories))
            }, ::onError).addTo(compositeDisposable)
    }

    fun loadProductsByCategory(category: String) {
        isLoadProductsByCategory = true
        repositoryRoom.getItems().concatMap { items ->
            repository.getProductByCategory(category).map { products ->
                products.forEach { prod ->
                    items.forEach { item ->
                        if (prod.id == item.uid) prod.count = item.count
                    }
                }

                return@map products
            }
        }.subscribeTo({ products ->
            if (isLoadProductsByCategory)
                products.map(::loadProductItem)
                    .let { _products.postValue(Event(it)) }
            isLoadProductsByCategory = false
        }, ::onError).addTo(compositeDisposable)
    }

    fun saveProductInShoppingCart(product: Product) {
        repositoryRoom.saveItemAndGetCount(product)
            .subscribeTo({ count ->
                _shoppingCart.postValue(Event(count))
            }, ::onError).addTo(compositeDisposable)
    }

    private fun loadProductItem(product: Product): ProductItem =
        ProductItem(product)

    private fun onError(t: Throwable) {
        println("MESSAGE_ERROR - ${t.message}")
    }
}
