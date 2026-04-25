package com.example.distributionmanagement.data.repository

import com.example.distributionmanagement.data.dao.AidCategoryDao
import com.example.distributionmanagement.data.model.AidCategory
import kotlinx.coroutines.flow.Flow

class AidCategoryRepository(private val aidCategoryDao: AidCategoryDao) {

    fun getAllCategories(): Flow<List<AidCategory>> = aidCategoryDao.getAllCategories()

    suspend fun getCategoryById(categoryId: String): AidCategory? =
        aidCategoryDao.getCategoryById(categoryId)

    suspend fun insertCategory(category: AidCategory) = aidCategoryDao.insertCategory(category)

    suspend fun updateCategory(category: AidCategory) = aidCategoryDao.updateCategory(category)

    suspend fun deleteCategory(categoryId: String) = aidCategoryDao.deleteCategory(categoryId)

    suspend fun getCategoryByName(name: String): AidCategory? =
        aidCategoryDao.getCategoryByName(name)
}
