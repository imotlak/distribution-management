package com.example.distributionmanagement.data.repository

import com.example.distributionmanagement.data.dao.UserDao
import com.example.distributionmanagement.data.model.User
import com.example.distributionmanagement.data.model.UserRole
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()

    fun getUsersByRole(role: UserRole): Flow<List<User>> = userDao.getUsersByRole(role)

    suspend fun getUserById(userId: String): User? = userDao.getUserById(userId)

    suspend fun insertUser(user: User) = userDao.insertUser(user)

    suspend fun updateUser(user: User) = userDao.updateUser(user)

    suspend fun deleteUser(userId: String) = userDao.deleteUser(userId)

    suspend fun authenticateUser(username: String, password: String): User? {
        val user = userDao.getUserByUsername(username)
        return if (user != null && user.password == password) user else null
    }

    suspend fun getUserByUsername(username: String): User? = userDao.getUserByUsername(username)
}
