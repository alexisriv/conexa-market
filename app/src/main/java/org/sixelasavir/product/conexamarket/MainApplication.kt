package org.sixelasavir.product.conexamarket

import android.app.Application

class MainApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        AppDatabase.init(this)
    }
}
