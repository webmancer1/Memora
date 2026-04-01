package com.example.memora.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val category: String,
    val vendor: String,
    val amount: Double,
    val date: String, 
    val status: String, 
    val receiptPath: String? = null
)
