package com.example.memora.ui.planning

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.memora.data.local.entity.CategoryEntity
import com.example.memora.data.local.entity.DocumentEntity
import com.example.memora.data.local.entity.TaskEntity
import com.example.memora.repository.PlanningRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlanningViewModel @Inject constructor(
    private val repository: PlanningRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedStatusFilter = MutableStateFlow<TaskStatusFilter>(TaskStatusFilter.ALL)
    val selectedStatusFilter: StateFlow<TaskStatusFilter> = _selectedStatusFilter

    val categories = repository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val rawTasks = repository.getAllTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val documents = repository.getAllDocuments()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredTasks = combine(rawTasks, _searchQuery, _selectedStatusFilter) { tasks, query, status ->
        tasks.filter { task ->
            val matchesQuery = task.title.contains(query, ignoreCase = true) || 
                               task.description.contains(query, ignoreCase = true)
            val matchesStatus = when (status) {
                TaskStatusFilter.ALL -> true
                TaskStatusFilter.PENDING -> !task.isCompleted
                TaskStatusFilter.COMPLETED -> task.isCompleted
            }
            matchesQuery && matchesStatus
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val progressPercentage: StateFlow<Float> = combine(rawTasks) { (tasks) ->
        if (tasks.isEmpty()) 0f else tasks.count { it.isCompleted }.toFloat() / tasks.size
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0f)

    init {
        
        viewModelScope.launch {
            categories.collect { cats ->
                if (cats.isEmpty()) {
                    val defaultCategories = listOf(
                        CategoryEntity(name = "Logistics", colorHex = "#FF5722"),
                        CategoryEntity(name = "Communications", colorHex = "#2196F3"),
                        CategoryEntity(name = "Documentation", colorHex = "#607D8B"),
                        CategoryEntity(name = "Financial", colorHex = "#4CAF50"),
                        CategoryEntity(name = "Ceremony", colorHex = "#9C27B0"),
                        CategoryEntity(name = "Legal", colorHex = "#795548"),
                        CategoryEntity(name = "Memorial", colorHex = "#E91E63")
                    )
                    defaultCategories.forEach { repository.insertCategory(it) }
                }
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateStatusFilter(filter: TaskStatusFilter) {
        _selectedStatusFilter.value = filter
    }

    fun addTask(title: String, description: String, categoryId: Long, priority: Int, dueDate: Long?) {
        viewModelScope.launch {
            repository.insertTask(
                TaskEntity(
                    title = title,
                    description = description,
                    categoryId = categoryId,
                    priority = priority,
                    dueDate = dueDate,
                    isCompleted = false,
                    createdDate = System.currentTimeMillis()
                )
            )
        }
    }

    fun toggleTaskCompletion(task: TaskEntity) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }
    
    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    fun addDocument(title: String, type: String, filePath: String) {
        viewModelScope.launch {
            repository.insertDocument(
                DocumentEntity(
                    title = title,
                    type = type,
                    filePath = filePath,
                    uploadDate = System.currentTimeMillis()
                )
            )
        }
    }

    fun deleteDocument(document: DocumentEntity) {
        viewModelScope.launch {
            repository.deleteDocument(document)
        }
    }
}

enum class TaskStatusFilter { ALL, PENDING, COMPLETED }
