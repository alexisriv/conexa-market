package org.sixelasavir.product.conexamarket.extensible

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

fun <T> Observable<T>.subscribeTo(
    onNext: Consumer<in T>,
    onError: Consumer<in Throwable>
): Disposable {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(onNext, onError)
}