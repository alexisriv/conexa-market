package org.sixelasavir.product.conexamarket.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import org.sixelasavir.product.conexamarket.model.entity.ItemCart

@Dao
interface ItemCartDao {
    @Query("SELECT * FROM items WHERE count > 0")
    fun getAll(): Observable<List<ItemCart>>

    @Query("SELECT SUM(count) as count, SUM(price * count) as total FROM items")
    fun getCart(): Observable<Cart>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: ItemCart): Completable

    @Query("DELETE FROM items")
    fun deleteAll(): Completable
}
