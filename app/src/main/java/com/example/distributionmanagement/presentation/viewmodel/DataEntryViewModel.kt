package com.example.distributionmanagement.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.distributionmanagement.data.model.DistributionItem
import com.example.distributionmanagement.data.model.DistributionRequest
import com.example.distributionmanagement.domain.service.DistributionWorkflowService
import com.example.distributionmanagement.domain.service.ValidationService
import com.example.distributionmanagement.domain.usecase.AddDistributionItemUseCase
import com.example.distributionmanagement.domain.usecase.ApproveByDataEntryUseCase
import com.example.distributionmanagement.domain.usecase.GetDistributionItemsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DataEntryViewModel(
    private val distributionWorkflowService: DistributionWorkflowService,
    private val addDistributionItemUseCase: AddDistributionItemUseCase,
    private val approveByDataEntryUseCase: ApproveByDataEntryUseCase,
    private val getDistributionItemsUseCase: GetDistributionItemsUseCase,
    private val validationService: ValidationService
) : ViewModel() {

    private val _pendingRequests = MutableStateFlow<List<DistributionRequest>>(emptyList())
    val pendingRequests: StateFlow<List<DistributionRequest>> = _pendingRequests.asStateFlow()

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
        loadPendingRequests()
    }

    private fun loadPendingRequests() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val requests = distributionWorkflowService.getPendingDataEntry()
                _pendingRequests.value = requests
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

    fun addItem(categoryId: String, quantity: Double, unit: String) {
        val requestId = _selectedRequestId.value
        if (requestId == null) {
            _errorMessage.value = "يرجى اختيار طلب أولاً"
            return
        }

        if (!validationService.validateItemQuantity(quantity)) {
            _errorMessage.value = "الكمية يجب أن تكون أكبر من صفر"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = addDistributionItemUseCase(requestId, categoryId, quantity, unit)
                if (result.isSuccess) {
                    _successMessage.value = "تم إضافة الصنف بنجاح"
                    loadRequestItems(requestId)
                } else {
                    _errorMessage.value = result.exceptionOrNull()?.message ?: "خطأ في إضافة الصنف"
                }
            } catch (e: Exception) {
                _errorMessage.value = "خطأ: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun approveRequest(requestId: String) {
        val items = _currentRequestItems.value
        if (!validationService.validateRequestHasItems(items)) {
            _errorMessage.value = "يجب إضافة أصناف قبل الموافقة"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = approveByDataEntryUseCase(requestId)
                if (result.isSuccess) {
                    _successMessage.value = "تم الموافقة على الطلب بنجاح"
                    loadPendingRequests()
                    _selectedRequestId.value = null
                } else {
                    _errorMessage.value = result.exceptionOrNull()?.message ?: "خطأ في الموافقة"
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
