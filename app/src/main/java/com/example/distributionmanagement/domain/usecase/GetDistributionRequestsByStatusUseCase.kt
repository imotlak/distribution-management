package com.example.distributionmanagement.domain.usecase

import com.example.distributionmanagement.data.model.RequestStatus
import com.example.distributionmanagement.data.repository.DistributionRepository
import kotlinx.coroutines.flow.Flow

class GetDistributionRequestsByStatusUseCase(
    private val distributionRepository: DistributionRepository
) {

    operator fun invoke(status: RequestStatus): Flow<List<com.example.distributionmanagement.data.model.DistributionRequest>> =
        distributionRepository.getRequestsByStatus(status)
}
