package com.example.domain.usecase

import com.example.domain.model.Item
import com.example.domain.repository.DBRepository
import kotlinx.coroutines.flow.Flow

class GetListUseCase(
    private val dbRepository: DBRepository
) {
    fun execute(): Flow<List<Item>> {
        return dbRepository.getList()
    }
}