package com.example.memora.ui.planning

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.memora.ui.planning.components.TaskCard

@Composable
fun TasksTabContent(viewModel: PlanningViewModel) {
    val tasks by viewModel.filteredTasks.collectAsState()
    val categories by viewModel.categories.collectAsState()

    val pendingTasks = tasks.filter { !it.isCompleted }
    val completedTasks = tasks.filter { it.isCompleted }

    var isPendingExpanded by remember { mutableStateOf(true) }
    var isCompletedExpanded by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp) 
    ) {
        
        item {
            CollapsibleHeader(
                title = "Pending (${pendingTasks.size})",
                isExpanded = isPendingExpanded,
                onToggle = { isPendingExpanded = !isPendingExpanded }
            )
        }
        if (isPendingExpanded) {
            items(pendingTasks) { task ->
                val category = categories.find { it.id == task.categoryId }
                TaskCard(
                    task = task,
                    category = category,
                    onToggleComplete = { viewModel.toggleTaskCompletion(task) },
                    onClick = {  }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            if (pendingTasks.isEmpty()) {
                item {
                    Text(
                        "No pending tasks",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        
        item {
            CollapsibleHeader(
                title = "Completed (${completedTasks.size})",
                isExpanded = isCompletedExpanded,
                onToggle = { isCompletedExpanded = !isCompletedExpanded }
            )
        }
        if (isCompletedExpanded) {
            items(completedTasks) { task ->
                val category = categories.find { it.id == task.categoryId }
                TaskCard(
                    task = task,
                    category = category,
                    onToggleComplete = { viewModel.toggleTaskCompletion(task) },
                    onClick = {  }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun CollapsibleHeader(
    title: String,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggle)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Icon(
            imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
            contentDescription = if (isExpanded) "Collapse" else "Expand",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
