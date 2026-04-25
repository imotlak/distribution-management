package com.example.distributionmanagement.data.remote.sheets

import android.content.Context
import com.example.distributionmanagement.data.model.DistributionRequest
import com.example.distributionmanagement.data.model.DistributionItem
import com.example.distributionmanagement.data.model.Camp
import com.example.distributionmanagement.data.model.AidCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Service for interacting with Google Sheets
 * Handles all read/write operations to the spreadsheet
 */
class GoogleSheetsService(
    private val context: Context,
    private val apiService: GoogleSheetsApiService,
    private val spreadsheetId: String,
    private val apiKey: String
) {

    /**
     * Get all distribution requests from Sheets
     */
    suspend fun getDistributionRequests(): Result<List<DistributionRequest>> = withContext(Dispatchers.IO) {
        try {
            val range = "${GoogleSheetsConfig.SHEET_DISTRIBUTION_REQUESTS}!A2:K"
            val response = apiService.getValues(spreadsheetId, range, apiKey)
            val rows = response.data?.values ?: emptyList()
            val requests = rows.mapNotNull { GoogleSheetsMapper.rowToDistributionRequest(it) }
            Result.success(requests)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get all distribution items from Sheets
     */
    suspend fun getDistributionItems(): Result<List<DistributionItem>> = withContext(Dispatchers.IO) {
        try {
            val range = "${GoogleSheetsConfig.SHEET_DISTRIBUTION_ITEMS}!A2:F"
            val response = apiService.getValues(spreadsheetId, range, apiKey)
            val rows = response.data?.values ?: emptyList()
            val items = rows.mapNotNull { row ->
                try {
                    DistributionItem(
                        id = row[0].toString(),
                        requestId = row[1].toString(),
                        categoryId = row[2].toString(),
                        quantity = (row[3] as? Number)?.toDouble() ?: 0.0,
                        unit = row[4].toString(),
                        createdAt = java.time.LocalDateTime.parse(
                            row[5].toString(),
                            java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        )
                    )
                } catch (e: Exception) {
                    null
                }
            }
            Result.success(items)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get all camps from Sheets
     */
    suspend fun getCamps(): Result<List<Camp>> = withContext(Dispatchers.IO) {
        try {
            val range = "${GoogleSheetsConfig.SHEET_CAMPS}!A2:F"
            val response = apiService.getValues(spreadsheetId, range, apiKey)
            val rows = response.data?.values ?: emptyList()
            val camps = rows.mapNotNull { row ->
                try {
                    Camp(
                        id = row[0].toString(),
                        name = row[1].toString(),
                        location = row[2].toString(),
                        capacity = (row[3] as? Number)?.toInt() ?: 0,
                        status = row[4].toString(),
                        createdAt = java.time.LocalDateTime.parse(
                            row[5].toString(),
                            java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        )
                    )
                } catch (e: Exception) {
                    null
                }
            }
            Result.success(camps)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get all aid categories from Sheets
     */
    suspend fun getAidCategories(): Result<List<AidCategory>> = withContext(Dispatchers.IO) {
        try {
            val range = "${GoogleSheetsConfig.SHEET_AID_CATEGORIES}!A2:E"
            val response = apiService.getValues(spreadsheetId, range, apiKey)
            val rows = response.data?.values ?: emptyList()
            val categories = rows.mapNotNull { row ->
                try {
                    AidCategory(
                        id = row[0].toString(),
                        name = row[1].toString(),
                        description = row[2].toString(),
                        unit = row[3].toString(),
                        createdAt = java.time.LocalDateTime.parse(
                            row[4].toString(),
                            java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                        )
                    )
                } catch (e: Exception) {
                    null
                }
            }
            Result.success(categories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Add a new distribution request to Sheets
     */
    suspend fun addDistributionRequest(request: DistributionRequest): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val range = "${GoogleSheetsConfig.SHEET_DISTRIBUTION_REQUESTS}!A:K"
            val row = GoogleSheetsMapper.distributionRequestToRow(request)
            val valueRange = ValueRange(
                range = range,
                values = listOf(row)
            )
            apiService.appendValues(spreadsheetId, range, valueRange, apiKey = apiKey)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Add a new distribution item to Sheets
     */
    suspend fun addDistributionItem(item: DistributionItem): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val range = "${GoogleSheetsConfig.SHEET_DISTRIBUTION_ITEMS}!A:F"
            val row = GoogleSheetsMapper.distributionItemToRow(item)
            val valueRange = ValueRange(
                range = range,
                values = listOf(row)
            )
            apiService.appendValues(spreadsheetId, range, valueRange, apiKey = apiKey)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Add a new camp to Sheets
     */
    suspend fun addCamp(camp: Camp): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val range = "${GoogleSheetsConfig.SHEET_CAMPS}!A:F"
            val row = GoogleSheetsMapper.campToRow(camp)
            val valueRange = ValueRange(
                range = range,
                values = listOf(row)
            )
            apiService.appendValues(spreadsheetId, range, valueRange, apiKey = apiKey)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Add a new aid category to Sheets
     */
    suspend fun addAidCategory(category: AidCategory): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val range = "${GoogleSheetsConfig.SHEET_AID_CATEGORIES}!A:E"
            val row = GoogleSheetsMapper.aidCategoryToRow(category)
            val valueRange = ValueRange(
                range = range,
                values = listOf(row)
            )
            apiService.appendValues(spreadsheetId, range, valueRange, apiKey = apiKey)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
