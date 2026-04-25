package com.example.distributionmanagement.data.repository

import com.example.distributionmanagement.data.dao.DistributionRequestDao
import com.example.distributionmanagement.data.dao.DistributionItemDao
import com.example.distributionmanagement.data.dao.DistributionRecordDao
import com.example.distributionmanagement.data.model.DistributionRequest
import com.example.distributionmanagement.data.model.DistributionItem
import com.example.distributionmanagement.data.model.DistributionRecord
import com.example.distributionmanagement.data.model.RequestStatus
import kotlinx.coroutines.flow.Flow

class DistributionRepository(
    private val distributionRequestDao: DistributionRequestDao,
    private val distributionItemDao: DistributionItemDao,
    private val distributionRecordDao: DistributionRecordDao
) {

    // ==================== Distribution Request Operations ====================

    fun getAllDistributionRequests(): Flow<List<DistributionRequest>> =
        distributionRequestDao.getAllRequests()

    fun getRequestsByStatus(status: RequestStatus): Flow<List<DistributionRequest>> =
        distributionRequestDao.getRequestsByStatus(status)

    suspend fun getRequestById(requestId: String): DistributionRequest? =
        distributionRequestDao.getRequestById(requestId)

    suspend fun insertDistributionRequest(request: DistributionRequest) =
        distributionRequestDao.insertRequest(request)

    suspend fun updateDistributionRequest(request: DistributionRequest) =
        distributionRequestDao.updateRequest(request)

    // ==================== Distribution Item Operations ====================

    fun getItemsByRequestId(requestId: String): Flow<List<DistributionItem>> =
        distributionItemDao.getItemsByRequestId(requestId)

    suspend fun insertDistributionItem(item: DistributionItem) =
        distributionItemDao.insertItem(item)

    suspend fun updateDistributionItem(item: DistributionItem) =
        distributionItemDao.updateItem(item)

    suspend fun deleteDistributionItem(itemId: String) =
        distributionItemDao.deleteItem(itemId)

    // ==================== Distribution Record Operations ====================

    fun getAllDistributionRecords(): Flow<List<DistributionRecord>> =
        distributionRecordDao.getAllRecords()

    fun getRecordsByRequestId(requestId: String): Flow<List<DistributionRecord>> =
        distributionRecordDao.getRecordsByRequestId(requestId)

    suspend fun insertDistributionRecord(record: DistributionRecord) =
        distributionRecordDao.insertRecord(record)

    suspend fun updateDistributionRecord(record: DistributionRecord) =
        distributionRecordDao.updateRecord(record)
}
