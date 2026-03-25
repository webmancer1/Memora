package com.example.memora.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.memora.data.local.dao.PlanningDao
import com.example.memora.data.local.entity.CategoryEntity
import com.example.memora.data.local.entity.DocumentEntity
import com.example.memora.data.local.entity.TaskEntity
import com.example.memora.data.local.entity.BudgetEntity
import com.example.memora.data.local.entity.ExpenseEntity
import com.example.memora.data.local.dao.BudgetDao

@Database(
    entities = [TaskEntity::class, CategoryEntity::class, DocumentEntity::class, BudgetEntity::class, ExpenseEntity::class],
    version = 2,
    exportSchema = false
)
abstract class PlanningDatabase : RoomDatabase() {
    abstract fun planningDao(): PlanningDao
    abstract fun budgetDao(): BudgetDao
}
