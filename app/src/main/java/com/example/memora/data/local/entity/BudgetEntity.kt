package com.example.memora.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val totalBudget: Double,
    val totalSpent: Double = 0.0,
    val totalPending: Double = 0.0,
    val remaining: Double = totalBudget
)
