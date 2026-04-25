package com.example.distributionmanagement.domain.usecase

import com.example.distributionmanagement.data.model.RequestStatus
import com.example.distributionmanagement.data.repository.DistributionRepository
import java.time.LocalDateTime

class StartDistributionUseCase(private val distributionRepository: DistributionRepository) {

    suspend operator fun invoke(requestId: String): Result<Unit> = try {
        val request = distributionRepository.getRequestById(requestId)
            ?: throw IllegalArgumentException("Request not found")

        if (request.status != RequestStatus.PENDING_DISTRIBUTION) {
            throw IllegalStateException(
                "Distribution cannot be started. Current status: ${request.status}"
            )
        }

        val updatedRequest = request.copy(
            status = RequestStatus.IN_DISTRIBUTION,
            distributionStartedAt = LocalDateTime.now()
        )
        distributionRepository.updateDistributionRequest(updatedRequest)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
