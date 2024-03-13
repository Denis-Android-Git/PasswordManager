package com.example.key.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Item
import com.example.domain.usecase.DeleteUseCase
import com.example.domain.usecase.GetItemUseCase
import com.example.domain.usecase.GetListUseCase
import com.example.domain.usecase.UpsertUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DbViewModel(
    private val getListUseCase: GetListUseCase,
    private val upsertUseCase: UpsertUseCase,
    private val deleteUseCase: DeleteUseCase,
    private val getItemUseCase: GetItemUseCase
) : ViewModel() {

    val list = this.getListUseCase.execute()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    private var _item = MutableStateFlow<Item?>(null)
    val item = _item.asStateFlow()

    fun getItem(id: Int) {
        viewModelScope.launch {
            _item.value = getItemUseCase.execute(id)
        }
    }

    fun upsertItem(
        password: String,
        url: String
    ) {
        viewModelScope.launch {
            val item = Item(
                password = password,
                url = url
            )
            upsertUseCase.execute(item)
        }
    }

    fun updateItem(
        password: String,
        url: String,
        itemId: Int
    ) {
        viewModelScope.launch {
            val newItem = Item(
                id = itemId,
                password = password,
                url = url
            )
            upsertUseCase.execute(newItem)
        }
    }

    fun delete(item: Item) {
        viewModelScope.launch {
            deleteUseCase.execute(item)
        }
    }
}