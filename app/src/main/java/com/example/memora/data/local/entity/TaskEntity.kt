package com.example.memora.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val categoryId: Long,
    val priority: Int, // 0 = Low, 1 = Medium, 2 = High
    val dueDate: Long?,
    val isCompleted: Boolean,
    val createdDate: Long
)
