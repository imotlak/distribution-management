package com.example.distributionmanagement.domain.usecase

import com.example.distributionmanagement.data.model.DistributionRequest
import com.example.distributionmanagement.data.model.RequestStatus
import com.example.distributionmanagement.data.repository.DistributionRepository
import java.util.UUID
import java.time.LocalDateTime

class CreateDistributionRequestUseCase(private val distributionRepository: DistributionRepository) {

    suspend operator fun invoke(
        campId: String,
        warehouseId: String,
        createdByUserId: String
    ): Result<DistributionRequest> = try {
        val request = DistributionRequest(
            id = UUID.randomUUID().toString(),
            campId = campId,
            warehouseId = warehouseId,
            status = RequestStatus.PENDING_MANAGER_APPROVAL,
            createdByUserId = createdByUserId,
            createdAt = LocalDateTime.now(),
            managerApprovalAt = null,
            dataEntryApprovalAt = null,
            distributionStartedAt = null,
            distributionCompletedAt = null,
            notes = ""
        )
        distributionRepository.insertDistributionRequest(request)
        Result.success(request)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
