package org.sixelasavir.product.conexamarket

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.sixelasavir.product.conexamarket.dao.ItemCartDao
import org.sixelasavir.product.conexamarket.model.entity.ItemCart

@Database(entities = [ItemCart::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemCartDao(): ItemCartDao

    companion object {
        lateinit var instance: AppDatabase
        fun init(applicationContext: Application) {
            instance = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "database-shopping"
            ).fallbackToDestructiveMigration()
                .build()
        }
    }
}
