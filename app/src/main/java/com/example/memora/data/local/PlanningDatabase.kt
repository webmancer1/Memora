package com.example.memora.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.memora.data.local.dao.PlanningDao
import com.example.memora.data.local.entity.CategoryEntity
import com.example.memora.data.local.entity.DocumentEntity
import com.example.memora.data.local.entity.TaskEntity

@Database(
    entities = [TaskEntity::class, CategoryEntity::class, DocumentEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PlanningDatabase : RoomDatabase() {
    abstract fun planningDao(): PlanningDao
}
