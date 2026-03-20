package com.example.memora.repository

import com.example.memora.data.local.dao.PlanningDao
import com.example.memora.data.local.entity.CategoryEntity
import com.example.memora.data.local.entity.DocumentEntity
import com.example.memora.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PlanningRepository @Inject constructor(
    private val planningDao: PlanningDao
) {
    // Tasks
    fun getAllTasks(): Flow<List<TaskEntity>> = planningDao.getAllTasks()
    
    suspend fun insertTask(task: TaskEntity) {
        planningDao.insertTask(task)
    }
    
    suspend fun updateTask(task: TaskEntity) {
        planningDao.updateTask(task)
    }
    
    suspend fun deleteTask(task: TaskEntity) {
        planningDao.deleteTask(task)
    }

    // Categories
    fun getAllCategories(): Flow<List<CategoryEntity>> = planningDao.getAllCategories()
    
    suspend fun insertCategory(category: CategoryEntity) {
        planningDao.insertCategory(category)
    }
    
    // Documents
    fun getAllDocuments(): Flow<List<DocumentEntity>> = planningDao.getAllDocuments()
    
    suspend fun insertDocument(document: DocumentEntity) {
        planningDao.insertDocument(document)
    }
    
    suspend fun deleteDocument(document: DocumentEntity) {
        planningDao.deleteDocument(document)
    }
}
