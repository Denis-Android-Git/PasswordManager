package com.example.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item")
data class Item(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    val id: Int = 0,
    @ColumnInfo
    val url: String,
    @ColumnInfo
    val password: String
)
