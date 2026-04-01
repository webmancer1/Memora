package com.example.memora.ui.planning

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.memora.data.local.entity.BudgetEntity
import com.example.memora.data.local.entity.ExpenseEntity
import com.example.memora.repository.BudgetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BudgetState(
    val budget: BudgetEntity? = null,
    val expenses: List<ExpenseEntity> = emptyList(),
    val totalBudget: Double = 0.0,
    val totalSpent: Double = 0.0,
    val totalPending: Double = 0.0,
    val remaining: Double = 0.0,
    val categoryBreakdown: List<CategoryExpense> = emptyList()
)

data class CategoryExpense(
    val name: String,
    val amount: Double,
    val percentage: Double
)

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val repository: BudgetRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val state: StateFlow<BudgetState> = combine(
        repository.getBudget(),
        repository.getAllExpenses(),
        _searchQuery
    ) { budget, expenses, query ->
        val filteredExpenses = if (query.isBlank()) {
            expenses
        } else {
            expenses.filter {
                it.title.contains(query, ignoreCase = true) || 
                it.category.contains(query, ignoreCase = true) ||
                it.vendor.contains(query, ignoreCase = true)
            }
        }

        val totalBudget = budget?.totalBudget ?: 0.0
        val totalSpent = expenses.filter { it.status == "paid" }.sumOf { it.amount }
        val totalPending = expenses.filter { it.status == "pending" }.sumOf { it.amount }
        val remaining = totalBudget - totalSpent - totalPending

        val categoryMap = expenses.groupBy { it.category }
            .mapValues { entry -> entry.value.sumOf { it.amount } }

        val totalAmountForPercentages = if (totalBudget > 0) totalBudget else if (expenses.isNotEmpty()) expenses.sumOf { it.amount } else 1.0
        val categoryBreakdown = categoryMap.map { (category, amount) ->
            CategoryExpense(
                name = category,
                amount = amount,
                percentage = if (totalAmountForPercentages > 0) (amount / totalAmountForPercentages) * 100 else 0.0
            )
        }.sortedByDescending { it.amount }

        BudgetState(
            budget = budget,
            expenses = filteredExpenses,
            totalBudget = totalBudget,
            totalSpent = totalSpent,
            totalPending = totalPending,
            remaining = remaining,
            categoryBreakdown = categoryBreakdown
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BudgetState())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            repository.insertExpense(expense)
        }
    }

    fun updateExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            repository.updateExpense(expense)
        }
    }

    fun deleteExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            repository.deleteExpense(expense)
        }
    }

    fun setBudget(amount: Double) {
        viewModelScope.launch {
            val currentBudget = state.value.budget
            if (currentBudget != null) {
                repository.insertBudget(currentBudget.copy(totalBudget = amount))
            } else {
                repository.insertBudget(BudgetEntity(totalBudget = amount))
            }
        }
    }
}
