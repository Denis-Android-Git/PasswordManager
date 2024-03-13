package com.example.domain.repository

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.model.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface DBRepository {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: Item)

    @Query("select * from item")
    fun getList(): Flow<List<Item>>

    @Delete
    suspend fun delete(item: Item)

    @Query("select * from item where id = :id")
    suspend fun getItem(id: Int): Item
}