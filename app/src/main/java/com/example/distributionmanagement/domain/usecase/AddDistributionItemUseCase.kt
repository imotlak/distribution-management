package com.example.distributionmanagement.domain.usecase

import com.example.distributionmanagement.data.model.DistributionItem
import com.example.distributionmanagement.data.repository.DistributionRepository
import java.util.UUID
import java.time.LocalDateTime

class AddDistributionItemUseCase(private val distributionRepository: DistributionRepository) {

    suspend operator fun invoke(
        requestId: String,
        categoryId: String,
        quantity: Double,
        unit: String
    ): Result<DistributionItem> = try {
        val item = DistributionItem(
            id = UUID.randomUUID().toString(),
            requestId = requestId,
            categoryId = categoryId,
            quantity = quantity,
            unit = unit,
            createdAt = LocalDateTime.now()
        )
        distributionRepository.insertDistributionItem(item)
        Result.success(item)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
