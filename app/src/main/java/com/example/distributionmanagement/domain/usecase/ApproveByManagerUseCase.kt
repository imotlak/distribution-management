package com.example.distributionmanagement.domain.usecase

import com.example.distributionmanagement.data.model.RequestStatus
import com.example.distributionmanagement.data.repository.DistributionRepository
import java.time.LocalDateTime

class ApproveByManagerUseCase(private val distributionRepository: DistributionRepository) {

    suspend operator fun invoke(requestId: String): Result<Unit> = try {
        val request = distributionRepository.getRequestById(requestId)
            ?: throw IllegalArgumentException("Request not found")

        if (request.status != RequestStatus.PENDING_MANAGER_APPROVAL) {
            throw IllegalStateException(
                "Request cannot be approved. Current status: ${request.status}"
            )
        }

        val updatedRequest = request.copy(
            status = RequestStatus.PENDING_DATA_ENTRY,
            managerApprovalAt = LocalDateTime.now()
        )
        distributionRepository.updateDistributionRequest(updatedRequest)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
