package com.example.domain.usecase

import com.example.domain.model.Item
import com.example.domain.repository.DBRepository

class DeleteUseCase(
    private val dbRepository: DBRepository
) {
    suspend fun execute(item: Item) {
        dbRepository.delete(item)
    }
}