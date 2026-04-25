package com.example.distributionmanagement.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.distributionmanagement.data.model.DistributionRequest
import com.example.distributionmanagement.data.model.RequestStatus
import com.example.distributionmanagement.domain.service.DistributionWorkflowService
import com.example.distributionmanagement.domain.usecase.ApproveByManagerUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ManagerViewModel(
    private val distributionWorkflowService: DistributionWorkflowService,
    private val approveByManagerUseCase: ApproveByManagerUseCase
) : ViewModel() {

    private val _pendingRequests = MutableStateFlow<List<DistributionRequest>>(emptyList())
    val pendingRequests: StateFlow<List<DistributionRequest>> = _pendingRequests.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage.asStateFlow()

    init {
        loadPendingRequests()
    }

    private fun loadPendingRequests() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val requests = distributionWorkflowService.getPendingManagerApproval()
                _pendingRequests.value = requests
                _errorMessage.value = null
            } catch (e: Exception) {
                _errorMessage.value = "خطأ في تحميل الطلبات: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun approveRequest(requestId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = approveByManagerUseCase(requestId)
                if (result.isSuccess) {
                    _successMessage.value = "تم الموافقة على الطلب بنجاح"
                    loadPendingRequests() // Refresh the list
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
