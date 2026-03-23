package com.example.memora.ui.planning

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.memora.data.local.entity.TaskEntity
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TimelineTabContent(viewModel: PlanningViewModel) {
    val tasks by viewModel.rawTasks.collectAsState()
    val categories by viewModel.categories.collectAsState()

    // Sort tasks: due date first (nulls last), then priority
    val sortedTasks = tasks.sortedWith(compareBy<TaskEntity> { it.dueDate == null }
        .thenBy { it.dueDate }
        .thenByDescending { it.priority })

    if (sortedTasks.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "No tasks yet. Add a task to build your timeline.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp) // Removed spacing between items to allow line connection
        ) {
            itemsIndexed(sortedTasks) { index, task ->
                val category = categories.find { it.id == task.categoryId }
                val isLast = index == sortedTasks.size - 1
                
                TimelineItem(
                    task = task,
                    categoryColorHex = category?.colorHex,
                    isLast = isLast
                )
            }
        }
    }
}

@Composable
fun TimelineItem(
    task: TaskEntity,
    categoryColorHex: String?,
    isLast: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)
    ) {
        // Timeline node & connecting line
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(end = 16.dp)
        ) {
            val nodeColor = if (task.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
            
            // The Dot
            Canvas(modifier = Modifier.size(20.dp).padding(top = 4.dp)) {
                drawCircle(color = nodeColor, radius = size.minDimension / 2f)
            }
            // Connecting line
            if (!isLast) {
                val lineColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
                Canvas(modifier = Modifier.width(2.dp).fillMaxHeight()) {
                    drawLine(
                        color = lineColor,
                        start = Offset(size.width / 2, 0f),
                        end = Offset(size.width / 2, size.height),
                        strokeWidth = 2.dp.toPx()
                    )
                }
            } else {
                Spacer(modifier = Modifier.fillMaxHeight())
            }
        }

        // Timeline Content (Task Card)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp), // spacing below each item
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (task.isCompleted) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                }
                if (task.dueDate != null) {
                    val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                    Text(
                        text = formatter.format(Date(task.dueDate)),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Text(
                        text = "No Due Date",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Show completion status flag
                if (task.isCompleted) {
                    Text(
                        text = "Completed",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                } else {
                    Text(
                        text = "In Progress",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
