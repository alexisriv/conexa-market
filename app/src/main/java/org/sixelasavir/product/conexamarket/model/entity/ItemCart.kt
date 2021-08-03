package org.sixelasavir.product.conexamarket.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemCart(
    @PrimaryKey val uid: Long,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "price") val price: Float,
    @ColumnInfo(name = "count") val count: Int
)
