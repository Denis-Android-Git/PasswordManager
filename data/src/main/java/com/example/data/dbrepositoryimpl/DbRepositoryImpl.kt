package com.example.data.dbrepositoryimpl

import com.example.data.database.SiteDataBase
import com.example.domain.model.Item
import com.example.domain.repository.DBRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class DbRepositoryImpl(
    private val db: SiteDataBase
) : DBRepository {
    override suspend fun upsert(item: Item) {
        withContext(Dispatchers.IO) {
            db.itemDao().upsert(item)
        }
    }

    override fun getList(): Flow<List<Item>> {
        return db.itemDao().getList()
    }

    override suspend fun delete(item: Item) {
        withContext(Dispatchers.IO) {
            db.itemDao().delete(item)
        }
    }

    override suspend fun getItem(id: Int): Item {
        return withContext(Dispatchers.IO) {
            db.itemDao().getItem(id)
        }
    }
}