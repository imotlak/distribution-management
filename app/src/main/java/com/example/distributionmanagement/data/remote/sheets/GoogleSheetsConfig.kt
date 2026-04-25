package com.example.distributionmanagement.data.remote.sheets

import com.example.distributionmanagement.data.model.DistributionRequest
import com.example.distributionmanagement.data.model.DistributionItem
import com.example.distributionmanagement.data.model.Camp
import com.example.distributionmanagement.data.model.AidCategory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Google Sheets Configuration and Constants
 */
object GoogleSheetsConfig {
    // Spreadsheet sheets names
    const val SHEET_DISTRIBUTION_REQUESTS = "Distribution Requests"
    const val SHEET_DISTRIBUTION_ITEMS = "Distribution Items"
    const val SHEET_CAMPS = "Camps"
    const val SHEET_AID_CATEGORIES = "Aid Categories"
    const val SHEET_USERS = "Users"

    // Column headers
    object DistributionRequestsColumns {
        const val ID = "A"
        const val CAMP_ID = "B"
        const val WAREHOUSE_ID = "C"
        const val STATUS = "D"
        const val CREATED_BY_USER_ID = "E"
        const val CREATED_AT = "F"
        const val MANAGER_APPROVAL_AT = "G"
        const val DATA_ENTRY_APPROVAL_AT = "H"
        const val DISTRIBUTION_STARTED_AT = "I"
        const val DISTRIBUTION_COMPLETED_AT = "J"
        const val NOTES = "K"
    }

    object DistributionItemsColumns {
        const val ID = "A"
        const val REQUEST_ID = "B"
        const val CATEGORY_ID = "C"
        const val QUANTITY = "D"
        const val UNIT = "E"
        const val CREATED_AT = "F"
    }

    object CampsColumns {
        const val ID = "A"
        const val NAME = "B"
        const val LOCATION = "C"
        const val CAPACITY = "D"
        const val STATUS = "E"
        const val CREATED_AT = "F"
    }

    object AidCategoriesColumns {
        const val ID = "A"
        const val NAME = "B"
        const val DESCRIPTION = "C"
        const val UNIT = "D"
        const val CREATED_AT = "E"
    }
}

/**
 * Mapper for converting models to Google Sheets rows
 */
object GoogleSheetsMapper {

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun distributionRequestToRow(request: DistributionRequest): List<Any> = listOf(
        request.id,
        request.campId,
        request.warehouseId,
        request.status.name,
        request.createdByUserId,
        request.createdAt.format(dateFormatter),
        request.managerApprovalAt?.format(dateFormatter) ?: "",
        request.dataEntryApprovalAt?.format(dateFormatter) ?: "",
        request.distributionStartedAt?.format(dateFormatter) ?: "",
        request.distributionCompletedAt?.format(dateFormatter) ?: "",
        request.notes
    )

    fun distributionItemToRow(item: DistributionItem): List<Any> = listOf(
        item.id,
        item.requestId,
        item.categoryId,
        item.quantity,
        item.unit,
        item.createdAt.format(dateFormatter)
    )

    fun campToRow(camp: Camp): List<Any> = listOf(
        camp.id,
        camp.name,
        camp.location,
        camp.capacity,
        camp.status,
        camp.createdAt.format(dateFormatter)
    )

    fun aidCategoryToRow(category: AidCategory): List<Any> = listOf(
        category.id,
        category.name,
        category.description,
        category.unit,
        category.createdAt.format(dateFormatter)
    )

    fun rowToDistributionRequest(row: List<Any>): DistributionRequest? {
        if (row.size < 11) return null
        return try {
            DistributionRequest(
                id = row[0].toString(),
                campId = row[1].toString(),
                warehouseId = row[2].toString(),
                status = com.example.distributionmanagement.data.model.RequestStatus.valueOf(
                    row[3].toString()
                ),
                createdByUserId = row[4].toString(),
                createdAt = LocalDateTime.parse(
                    row[5].toString(),
                    dateFormatter
                ),
                managerApprovalAt = if (row[6].toString().isNotEmpty()) {
                    LocalDateTime.parse(row[6].toString(), dateFormatter)
                } else null,
                dataEntryApprovalAt = if (row[7].toString().isNotEmpty()) {
                    LocalDateTime.parse(row[7].toString(), dateFormatter)
                } else null,
                distributionStartedAt = if (row[8].toString().isNotEmpty()) {
                    LocalDateTime.parse(row[8].toString(), dateFormatter)
                } else null,
                distributionCompletedAt = if (row[9].toString().isNotEmpty()) {
                    LocalDateTime.parse(row[9].toString(), dateFormatter)
                } else null,
                notes = row[10].toString()
            )
        } catch (e: Exception) {
            null
        }
    }
}
