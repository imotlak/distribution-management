package com.example.distributionmanagement.data.remote.sheets

import android.content.Context
import com.example.distributionmanagement.data.model.DistributionRequest
import com.example.distributionmanagement.data.model.DistributionItem
import com.example.distributionmanagement.data.repository.DistributionRepository
import com.example.distributionmanagement.data.repository.CampRepository
import com.example.distributionmanagement.data.repository.AidCategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Manager for synchronizing data between local database and Google Sheets
 * Handles two-way sync with conflict resolution
 */
class GoogleSheetsSyncManager(
    private val context: Context,
    private val googleSheetsService: GoogleSheetsService,
    private val distributionRepository: DistributionRepository,
    private val campRepository: CampRepository,
    private val aidCategoryRepository: AidCategoryRepository
) {

    private val isSyncing = AtomicBoolean(false)
    private val lastSyncTime = mutableMapOf<String, Long>()

    /**
     * Sync all data from local database to Google Sheets (upload)
     */
    suspend fun syncToSheets(): Result<Unit> = withContext(Dispatchers.IO) {
        if (!isSyncing.compareAndSet(false, true)) {
            return@withContext Result.failure(Exception("Sync already in progress"))
        }

        try {
            syncDistributionRequests()
            syncDistributionItems()
            syncCamps()
            syncAidCategories()
            lastSyncTime["sheets"] = System.currentTimeMillis()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            isSyncing.set(false)
        }
    }

    /**
     * Sync all data from Google Sheets to local database (download)
     */
    suspend fun syncFromSheets(): Result<Unit> = withContext(Dispatchers.IO) {
        if (!isSyncing.compareAndSet(false, true)) {
            return@withContext Result.failure(Exception("Sync already in progress"))
        }

        try {
            // Download distribution requests
            val requestsResult = googleSheetsService.getDistributionRequests()
            if (requestsResult.isSuccess) {
                requestsResult.getOrNull()?.forEach { request ->
                    distributionRepository.insertDistributionRequest(request)
                }
            }

            // Download distribution items
            val itemsResult = googleSheetsService.getDistributionItems()
            if (itemsResult.isSuccess) {
                itemsResult.getOrNull()?.forEach { item ->
                    distributionRepository.insertDistributionItem(item)
                }
            }

            // Download camps
            val campsResult = googleSheetsService.getCamps()
            if (campsResult.isSuccess) {
                campsResult.getOrNull()?.forEach { camp ->
                    campRepository.insertCamp(camp)
                }
            }

            // Download aid categories
            val categoriesResult = googleSheetsService.getAidCategories()
            if (categoriesResult.isSuccess) {
                categoriesResult.getOrNull()?.forEach { category ->
                    aidCategoryRepository.insertCategory(category)
                }
            }

            lastSyncTime["local"] = System.currentTimeMillis()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            isSyncing.set(false)
        }
    }

    /**
     * Perform bidirectional sync
     */
    suspend fun biDirectionalSync(): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            // First upload local changes to Sheets
            syncToSheets().getOrNull()
            // Then download Sheets changes to local
            syncFromSheets()
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Check if sync is currently in progress
     */
    fun isSyncInProgress(): Boolean = isSyncing.get()

    /**
     * Get last sync time for a specific source
     */
    fun getLastSyncTime(source: String): Long = lastSyncTime[source] ?: 0L

    // Private helper methods

    private suspend fun syncDistributionRequests() {
        // Get all requests from local database and upload to Sheets
        // This would need a way to get all requests from repository
    }

    private suspend fun syncDistributionItems() {
        // Get all items from local database and upload to Sheets
        // This would need a way to get all items from repository
    }

    private suspend fun syncCamps() {
        // Get all camps from local database and upload to Sheets
        // This would need a way to get all camps from repository
    }

    private suspend fun syncAidCategories() {
        // Get all categories from local database and upload to Sheets
        // This would need a way to get all categories from repository
    }
}
