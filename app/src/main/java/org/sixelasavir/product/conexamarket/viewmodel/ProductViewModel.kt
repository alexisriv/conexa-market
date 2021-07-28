package org.sixelasavir.product.conexamarket.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers.io
import org.sixelasavir.product.conexamarket.model.Product
import org.sixelasavir.product.conexamarket.repository.ProductRepository

class ProductViewModel(
    private val repository: ProductRepository = ProductRepository()
) : BaseViewModel() {

    private val _products: MutableLiveData<List<ProductItem>> = MutableLiveData()
    val products: LiveData<List<ProductItem>>
        get() = _products

    private val _categories: MutableLiveData<List<String>> = MutableLiveData()
    val categories: LiveData<List<String>>
        get() = _categories

    fun loadProducts() {
        repository.getProduct()
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe(::handleProductResponse)
            .addTo(compositeDisposable)
    }

    private fun handleProductResponse(products: List<Product>?) {
        products?.map { ProductItem(it) }.let { _products.postValue(it) }
    }

    fun loadProductCategories() {
        repository.getProductCategories()
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe(::handleProductCategoriesResponse)
            .addTo(compositeDisposable)
    }

    private fun handleProductCategoriesResponse(categories: List<String>?) {
        _categories.postValue(categories)
    }

    fun loadProductsByCategory(category: String) {
        repository.getProductByCategory(category)
            .subscribeOn(io())
            .observeOn(mainThread())
            .subscribe(::handleProductByCategoryResponse)
            .addTo(compositeDisposable)
    }

    private fun handleProductByCategoryResponse(products: List<Product>?) {
        products?.map { ProductItem(it) }.let { _products.postValue(it) }
    }
}
