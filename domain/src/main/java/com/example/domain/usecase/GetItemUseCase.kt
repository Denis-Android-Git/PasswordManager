package com.example.domain.usecase

import com.example.domain.model.Item
import com.example.domain.repository.DBRepository

class GetItemUseCase(
    private val dbRepository: DBRepository
) {
    suspend fun execute(id: Int): Item {
        return dbRepository.getItem(id)
    }
}