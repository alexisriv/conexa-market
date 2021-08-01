package org.sixelasavir.product.conexamarket.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers.io
import org.sixelasavir.product.conexamarket.repository.ProductRepository
import org.sixelasavir.product.conexamarket.utils.Event

class ProductViewModel(
    private val repository: ProductRepository = ProductRepository()
) : BaseViewModel() {

    private val _products: MutableLiveData<Event<List<ProductItem>>> = MutableLiveData()
    val products: LiveData<Event<List<ProductItem>>>
        get() = _products

    private val _categories: MutableLiveData<Event<List<String>>> = MutableLiveData()
    val categories: LiveData<Event<List<String>>>
        get() = _categories

    fun loadProducts() {
        repository.getProduct()
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe { products ->
                products.map { ProductItem(it) }.let { _products.postValue(Event(it)) }
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
        repository.getProductByCategory(category)
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe { products ->
                products.map { ProductItem(it) }.let { _products.postValue(Event(it)) }
            }.addTo(compositeDisposable)
    }
}
