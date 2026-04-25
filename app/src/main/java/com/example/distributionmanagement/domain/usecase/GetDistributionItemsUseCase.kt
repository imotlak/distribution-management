package com.example.distributionmanagement.domain.usecase

import com.example.distributionmanagement.data.model.DistributionItem
import com.example.distributionmanagement.data.repository.DistributionRepository
import kotlinx.coroutines.flow.Flow

class GetDistributionItemsUseCase(private val distributionRepository: DistributionRepository) {

    operator fun invoke(requestId: String): Flow<List<DistributionItem>> =
        distributionRepository.getItemsByRequestId(requestId)
}
