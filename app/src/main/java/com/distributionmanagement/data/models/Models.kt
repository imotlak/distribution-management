package com.distributionmanagement.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * مستخدم النظام
 */
@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: String,
    val name: String,
    val email: String,
    val phone: String?,
    val role: UserRole,
    val password: String,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

enum class UserRole {
    MANAGER,           // مدير الموافقة
    DATA_ENTRY,        // مدخل البيانات
    DISTRIBUTION_MGR,  // مسئول التوزيع
    ADMIN             // مسئول النظام
}

/**
 * المخيم/موقع التوزيع
 */
@Entity(tableName = "camps")
data class Camp(
    @PrimaryKey
    val id: String,
    val name: String,
    val location: String,
    val latitude: Double?,
    val longitude: Double?,
    val contactPerson: String,
    val contactPhone: String,
    val capacity: Int?,
    val notes: String? = null,
    val isActive: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

/**
 * فئة المساعدة
 */
@Entity(tableName = "aid_categories")
data class AidCategory(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String? = null,
    val unit: String, // كج، لتر، عدد، إلخ
    val isActive: Boolean = true
)

/**
 * طلب التوزيع
 */
@Entity(tableName = "distribution_requests")
data class DistributionRequest(
    @PrimaryKey
    val id: String,
    val campId: String,
    val createdBy: String,  // مدخل البيانات
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val description: String? = null,
    val status: RequestStatus = RequestStatus.PENDING,
    val approvedByManager: Boolean = false,
    val managerApprovalTime: LocalDateTime? = null,
    val approvedByDataEntry: Boolean = false,
    val dataEntryApprovalTime: LocalDateTime? = null,
    val completedByDistributionMgr: Boolean = false,
    val completionTime: LocalDateTime? = null
)

enum class RequestStatus {
    PENDING,              // في الانتظار
    MANAGER_APPROVED,     // موافقة المدير
    DATA_ENTRY_APPROVED,  // موافقة مدخل البيانات
    IN_PROGRESS,          // قيد التنفيذ
    COMPLETED,            // مكتمل
    CANCELLED             // ملغى
}

/**
 * تفاصيل التوزيع (الأصناف والكميات)
 */
@Entity(tableName = "distribution_items")
data class DistributionItem(
    @PrimaryKey
    val id: String,
    val requestId: String,
    val categoryId: String,
    val quantity: Double,
    val unit: String,
    val notes: String? = null,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

/**
 * تسجيل التوزيع (ما تم توزيعه بالفعل)
 */
@Entity(tableName = "distribution_records")
data class DistributionRecord(
    @PrimaryKey
    val id: String,
    val requestId: String,
    val itemId: String,
    val quantityDistributed: Double,
    val unit: String,
    val distributedBy: String,  // مسئول التوزيع
    val distributedAt: LocalDateTime = LocalDateTime.now(),
    val notes: String? = null,
    val photoUrl: String? = null  // صورة التوزيع
)

/**
 * تقرير التوزيع
 */
@Entity(tableName = "distribution_reports")
data class DistributionReport(
    @PrimaryKey
    val id: String,
    val requestId: String,
    val campId: String,
    val totalItemsPlanned: Int,
    val totalItemsDistributed: Int,
    val generatedBy: String,
    val generatedAt: LocalDateTime = LocalDateTime.now(),
    val notes: String? = null
)

/**
 * مستودع المخزون
 */
@Entity(tableName = "warehouses")
data class Warehouse(
    @PrimaryKey
    val id: String,
    val name: String,
    val location: String,
    val manager: String,
    val contactPhone: String,
    val isActive: Boolean = true
)

/**
 * مخزون المستودع
 */
@Entity(tableName = "warehouse_inventory")
data class WarehouseInventory(
    @PrimaryKey
    val id: String,
    val warehouseId: String,
    val categoryId: String,
    val quantity: Double,
    val unit: String,
    val lastUpdated: LocalDateTime = LocalDateTime.now()
)

/**
 * سجل النشاط
 */
@Entity(tableName = "activity_logs")
data class ActivityLog(
    @PrimaryKey
    val id: String,
    val userId: String,
    val action: String,
    val description: String? = null,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val ipAddress: String? = null
)
