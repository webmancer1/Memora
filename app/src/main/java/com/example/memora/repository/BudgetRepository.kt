package com.example.memora.repository

import com.example.memora.data.local.dao.BudgetDao
import com.example.memora.data.local.entity.BudgetEntity
import com.example.memora.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BudgetRepository @Inject constructor(
    private val budgetDao: BudgetDao
) {

    fun getAllExpenses(): Flow<List<ExpenseEntity>> {
        return budgetDao.getAllExpenses()
    }

    suspend fun insertExpense(expense: ExpenseEntity) {
        budgetDao.insertExpense(expense)
        updateBudgetTotals()
    }

    suspend fun updateExpense(expense: ExpenseEntity) {
        budgetDao.updateExpense(expense)
        updateBudgetTotals()
    }

    suspend fun deleteExpense(expense: ExpenseEntity) {
        budgetDao.deleteExpense(expense)
        updateBudgetTotals()
    }

    fun getBudget(): Flow<BudgetEntity?> {
        return budgetDao.getBudget()
    }

    suspend fun insertBudget(budget: BudgetEntity) {
        budgetDao.insertBudget(budget)
        updateBudgetTotals()
    }

    suspend fun clearBudget() {
        budgetDao.clearBudget()
    }


    private suspend fun updateBudgetTotals() {
        
        
        
        return
    }
}
