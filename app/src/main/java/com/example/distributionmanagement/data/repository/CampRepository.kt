package com.example.distributionmanagement.data.repository

import com.example.distributionmanagement.data.dao.CampDao
import com.example.distributionmanagement.data.model.Camp
import kotlinx.coroutines.flow.Flow

class CampRepository(private val campDao: CampDao) {

    fun getAllCamps(): Flow<List<Camp>> = campDao.getAllCamps()

    fun getCampsByStatus(status: String): Flow<List<Camp>> = campDao.getCampsByStatus(status)

    suspend fun getCampById(campId: String): Camp? = campDao.getCampById(campId)

    suspend fun insertCamp(camp: Camp) = campDao.insertCamp(camp)

    suspend fun updateCamp(camp: Camp) = campDao.updateCamp(camp)

    suspend fun deleteCamp(campId: String) = campDao.deleteCamp(campId)

    suspend fun getCampByName(name: String): Camp? = campDao.getCampByName(name)
}
