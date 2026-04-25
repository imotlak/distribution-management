package com.example.distributionmanagement.domain.service

import com.example.distributionmanagement.data.model.DistributionRequest
import com.example.distributionmanagement.data.model.RequestStatus
import com.example.distributionmanagement.data.model.UserRole
import com.example.distributionmanagement.data.repository.DistributionRepository
import com.example.distributionmanagement.data.repository.UserRepository

/**
 * Service to manage the distribution workflow orchestration.
 * 
 * Workflow:
 * 1. Data Entry Person creates a distribution request for a camp
 * 2. Manager approves the request
 * 3. Data Entry Person enters the items and quantities
 * 4. Distribution Manager starts and completes the distribution
 */
class DistributionWorkflowService(
    private val distributionRepository: DistributionRepository,
    private val userRepository: UserRepository
) {

    /**
     * Get all requests awaiting manager approval
     */
    suspend fun getPendingManagerApproval(): List<DistributionRequest> {
        return distributionRepository.getRequestsByStatus(RequestStatus.PENDING_MANAGER_APPROVAL)
            .let { flow ->
                val list = mutableListOf<DistributionRequest>()
                flow.collect { list.addAll(it) }
                list
            }
    }

    /**
     * Get all requests awaiting data entry
     */
    suspend fun getPendingDataEntry(): List<DistributionRequest> {
        return distributionRepository.getRequestsByStatus(RequestStatus.PENDING_DATA_ENTRY)
            .let { flow ->
                val list = mutableListOf<DistributionRequest>()
                flow.collect { list.addAll(it) }
                list
            }
    }

    /**
     * Get all requests ready for distribution
     */
    suspend fun getPendingDistribution(): List<DistributionRequest> {
        return distributionRepository.getRequestsByStatus(RequestStatus.PENDING_DISTRIBUTION)
            .let { flow ->
                val list = mutableListOf<DistributionRequest>()
                flow.collect { list.addAll(it) }
                list
            }
    }

    /**
     * Verify user role before approval
     */
    suspend fun canApproveAsManager(userId: String): Boolean {
        val user = userRepository.getUserById(userId) ?: return false
        return user.role == UserRole.MANAGER
    }

    /**
     * Verify user role for data entry approval
     */
    suspend fun canApproveAsDataEntry(userId: String): Boolean {
        val user = userRepository.getUserById(userId) ?: return false
        return user.role == UserRole.DATA_ENTRY
    }

    /**
     * Verify user role for distribution management
     */
    suspend fun canManageDistribution(userId: String): Boolean {
        val user = userRepository.getUserById(userId) ?: return false
        return user.role == UserRole.DISTRIBUTION_MANAGER
    }
}
