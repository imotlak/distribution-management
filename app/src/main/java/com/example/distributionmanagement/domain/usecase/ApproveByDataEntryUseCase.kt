package com.example.distributionmanagement.domain.usecase

import com.example.distributionmanagement.data.model.RequestStatus
import com.example.distributionmanagement.data.repository.DistributionRepository
import java.time.LocalDateTime

class ApproveByDataEntryUseCase(private val distributionRepository: DistributionRepository) {

    suspend operator fun invoke(requestId: String): Result<Unit> = try {
        val request = distributionRepository.getRequestById(requestId)
            ?: throw IllegalArgumentException("Request not found")

        if (request.status != RequestStatus.PENDING_DATA_ENTRY) {
            throw IllegalStateException(
                "Request cannot be approved by data entry. Current status: ${request.status}"
            )
        }

        val updatedRequest = request.copy(
            status = RequestStatus.PENDING_DISTRIBUTION,
            dataEntryApprovalAt = LocalDateTime.now()
        )
        distributionRepository.updateDistributionRequest(updatedRequest)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
