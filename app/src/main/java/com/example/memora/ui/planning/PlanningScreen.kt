package com.example.memora.ui.planning

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.memora.ui.theme.MemoraTheme
import com.example.memora.ui.planning.components.AddTaskSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlanningScreen(
    viewModel: PlanningViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
    onOpenDrawer: () -> Unit = {}
) {
    val progressPercentage by viewModel.progressPercentage.collectAsState()
    val tasks by viewModel.filteredTasks.collectAsState()
    
    var selectedTabIndex by remember { mutableStateOf(1) } // Default to Tasks tab
    var showAddSheet by remember { mutableStateOf(false) }
    val tabs = listOf("Timeline", "Tasks", "Documents")

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Memora", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onOpenDrawer) {
                        Icon(Icons.Default.Menu, contentDescription = "Open Menu")
                    }
                }
            )
        },
        floatingActionButton = {
            if (selectedTabIndex == 1) { // Only show FAB on Tasks tab
                FloatingActionButton(
                    onClick = { showAddSheet = true },
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.onTertiary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Task")
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header Section
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Funeral Planning",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Organize and track all necessary preparations.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Progress Section
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val completedTasks = tasks.count { it.isCompleted }
                            val totalTasks = tasks.size
                            Text(
                                text = "$completedTasks of $totalTasks tasks completed",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${(progressPercentage * 100).toInt()}%",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        LinearProgressIndicator(
                            progress = { progressPercentage },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(10.dp)
                                .clip(RoundedCornerShape(5.dp)),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                        )
                    }
                }
            }

            // Tab Navigation
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.background
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title, fontWeight = FontWeight.SemiBold) }
                    )
                }
            }

            // Tab Content
            Box(modifier = Modifier.fillMaxSize()) {
                when (selectedTabIndex) {
                    0 -> TimelineTabContent(viewModel = viewModel)
                    1 -> TasksTabContent(viewModel = viewModel)
                    2 -> DocumentsTabContent()
                }
            }
        }
    }

    if (showAddSheet) {
        val categories by viewModel.categories.collectAsState()
        AddTaskSheet(
            categories = categories,
            onDismissRequest = { showAddSheet = false },
            onSaveTask = { title, description, categoryId, priority, dueDate ->
                viewModel.addTask(title, description, categoryId, priority, dueDate)
            }
        )
    }
}

@Composable
fun DocumentsTabContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Documents View Coming Soon")
    }
}
