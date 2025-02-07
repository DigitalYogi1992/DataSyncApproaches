package com.digitalyogi.datasyncapproaches.ui.hybrid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.digitalyogi.datasyncapproaches.data.local.MyEntity
import com.digitalyogi.datasyncapproaches.data.repository.HybridRepository
import com.digitalyogi.datasyncapproaches.domain.model.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HybridViewModel @Inject constructor(
    private val repository: HybridRepository
) : ViewModel() {

    private val _items = MutableStateFlow<List<MyEntity>>(emptyList())
    val items = _items.asStateFlow()

    val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    fun loadLocalData() {
        viewModelScope.launch {
            _items.value = repository.getLocalData()
        }
    }

    fun setError(msg: String){
        viewModelScope.launch {
        _errorMessage.value = msg}
    }

    fun addData(content: String) {
        viewModelScope.launch {
            val result = repository.addData(content)
            when (result) {
                is NetworkResult.Success -> {
                    loadLocalData()
                    _errorMessage.value = null
                }
                is NetworkResult.HttpError -> {
                    _errorMessage.value = "HTTP ${result.code}: ${result.message}"
                }
                is NetworkResult.NetworkError -> {
                    _errorMessage.value = result.error.localizedMessage ?: "Network error"
                }
                else -> {}
            }
        }
    }

    fun syncData() {
        viewModelScope.launch {
            val result = repository.syncPendingData()
            when (result) {
                is NetworkResult.Success -> {
                    loadLocalData()
                    _errorMessage.value = null
                }
                is NetworkResult.HttpError -> {
                    _errorMessage.value = "HTTP ${result.code}: ${result.message}"
                }
                is NetworkResult.NetworkError -> {
                    _errorMessage.value = result.error.localizedMessage ?: "Network error"
                }
                else -> {}
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
