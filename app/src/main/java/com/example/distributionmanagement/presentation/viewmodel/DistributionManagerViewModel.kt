package com.example.distributionmanagement.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.distributionmanagement.data.model.DistributionItem
import com.example.distributionmanagement.data.model.DistributionRequest
import com.example.distributionmanagement.domain.service.DistributionWorkflowService
import com.example.distributionmanagement.domain.usecase.CompleteDistributionUseCase
import com.example.distributionmanagement.domain.usecase.GetDistributionItemsUseCase
import com.example.distributionmanagement.domain.usecase.StartDistributionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DistributionManagerViewModel(
    private val distributionWorkflowService: DistributionWorkflowService,
    private val startDistributionUseCase: StartDistributionUseCase,
    private val completeDistributionUseCase: CompleteDistributionUseCase,
    private val getDistributionItemsUseCase: GetDistributionItemsUseCase
) : ViewModel() {

    private val _pendingDistributionRequests = MutableStateFlow<List<DistributionRequest>>(emptyList())
    val pendingDistributionRequests: StateFlow<List<DistributionRequest>> = _pendingDistributionRequests.asStateFlow()

    private val _currentRequestItems = MutableStateFlow<List<DistributionItem>>(emptyList())
    val currentRequestItems: StateFlow<List<DistributionItem>> = _currentRequestItems.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    private val _selectedRequestId = MutableStateFlow<String?>(null)
    val selectedRequestId: StateFlow<String?> = _selectedRequestId.asStateFlow()

    init {
        loadPendingDistributions()
    }

    private fun loadPendingDistributions() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val requests = distributionWorkflowService.getPendingDistribution()
                _pendingDistributionRequests.value = requests
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "خطأ في تحميل الطلبات: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectRequest(requestId: String) {
        _selectedRequestId.value = requestId
        loadRequestItems(requestId)
    }

    private fun loadRequestItems(requestId: String) {
        viewModelScope.launch {
            try {
                getDistributionItemsUseCase(requestId).collect { items ->
                    _currentRequestItems.value = items
                }
            } catch (e: Exception) {
                _errorMessage.value = "خطأ في تحميل الأصناف: ${e.message}"
            }
        }
    }

    fun startDistribution(requestId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = startDistributionUseCase(requestId)
                if (result.isSuccess) {
                    _successMessage.value = "تم بدء عملية التوزيع بنجاح"
                    loadPendingDistributions()
                } else {
                    _errorMessage.value = result.exceptionOrNull()?.message ?: "خطأ في بدء التوزيع"
                }
            } catch (e: Exception) {
                _errorMessage.value = "خطأ: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun completeDistribution(requestId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = completeDistributionUseCase(requestId)
                if (result.isSuccess) {
                    _successMessage.value = "تم إكمال عملية التوزيع بنجاح"
                    loadPendingDistributions()
                    _selectedRequestId.value = null
                } else {
                    _errorMessage.value = result.exceptionOrNull()?.message ?: "خطأ في إكمال التوزيع"
                }
            } catch (e: Exception) {
                _errorMessage.value = "خطأ: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }
}
