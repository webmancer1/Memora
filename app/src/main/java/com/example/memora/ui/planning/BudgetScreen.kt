package com.example.memora.ui.planning

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import java.util.Locale
import androidx.compose.foundation.clickable
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.memora.data.local.entity.ExpenseEntity
import com.example.memora.ui.components.MemoraAppDrawer
import com.example.memora.ui.planning.components.AddExpenseSheet
import com.example.memora.ui.planning.components.SetBudgetDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetScreen(
    viewModel: BudgetViewModel = hiltViewModel(),
    onNavigateToDashboard: () -> Unit = {},
    onNavigateToPlanning: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToBudget: () -> Unit = {}
) {
    val state by viewModel.state.collectAsState()
    
    
    var showAddExpenseDialog by remember { mutableStateOf(false) }
    var showSetBudgetDialog by remember { mutableStateOf(false) }
    
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    MemoraAppDrawer(
        drawerState = drawerState,
        currentRoute = "budget",
        onNavigateToDashboard = onNavigateToDashboard,
        onNavigateToPlanning = onNavigateToPlanning,
        onNavigateToProfile = onNavigateToProfile,
        onNavigateToBudget = onNavigateToBudget
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Memora") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        val context = androidx.compose.ui.platform.LocalContext.current
                        val exporter = remember { com.example.memora.utils.BudgetPdfExporter(context) }
                        IconButton(onClick = { 
                            scope.launch(kotlinx.coroutines.Dispatchers.IO) {
                                val file = exporter.exportBudgetToPdf(state)
                                if (file != null) {
                                    kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.Main) {
                                        android.widget.Toast.makeText(context, "PDF saved to Documents folder", android.widget.Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }) {
                            Icon(Icons.Default.Download, contentDescription = "Export PDF")
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { showAddExpenseDialog = true },
                    containerColor = Color(0xFF8B5CF6), 
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Expense")
                }
            }
        ) { paddingValues ->
        if (showAddExpenseDialog) {
            AddExpenseSheet(
                onDismiss = { showAddExpenseDialog = false },
                onSave = { expense -> viewModel.addExpense(expense) }
            )
        }

        if (showSetBudgetDialog) {
            SetBudgetDialog(
                currentBudget = state.totalBudget,
                onDismiss = { showSetBudgetDialog = false },
                onSave = { amount -> viewModel.setBudget(amount) }
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                BudgetHeaderSection()
            }
            item {
                BudgetOverviewCards(
                    totalBudget = state.totalBudget,
                    totalSpent = state.totalSpent,
                    remaining = state.remaining,
                    totalPending = state.totalPending,
                    onSetBudgetClick = { showSetBudgetDialog = true }
                )
            }
            item {
                BudgetUsageSection(
                    totalBudget = state.totalBudget,
                    totalSpent = state.totalSpent,
                    totalPending = state.totalPending,
                    available = state.remaining
                )
            }
            if (state.categoryBreakdown.isNotEmpty()) {
                item {
                    ExpensesByCategorySection(categories = state.categoryBreakdown)
                }
            }
            item {
                AllExpensesHeader()
            }
            items(state.expenses) { expense ->
                ExpenseCard(
                    expense = expense,
                    onClick = {  }
                )
            }
            item {
                BudgetManagementTips()
            }
        }
    }
}
}

@Composable
fun BudgetHeaderSection() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Budget Manager",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Track and manage your funeral expenses",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

fun formatAmount(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale.US)
    return format.format(amount)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetOverviewCard(
    title: String,
    amount: Double,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(16.dp),
        onClick = { onClick?.invoke() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(title, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                formatAmount(amount),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun BudgetOverviewCards(
    totalBudget: Double,
    totalSpent: Double,
    remaining: Double,
    totalPending: Double,
    onSetBudgetClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        BudgetOverviewCard(
            title = "Total Budget",
            amount = totalBudget,
            icon = Icons.Default.AccountBalanceWallet,
            iconColor = Color(0xFF8B5CF6), 
            modifier = Modifier.fillMaxWidth(),
            onClick = onSetBudgetClick
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
            BudgetOverviewCard(
                title = "Total Spent",
                amount = totalSpent,
                icon = Icons.Default.TrendingDown,
                iconColor = Color(0xFFEF4444), 
                modifier = Modifier.weight(1f)
            )
            BudgetOverviewCard(
                title = "Remaining",
                amount = remaining,
                icon = Icons.Default.TrendingUp,
                iconColor = Color(0xFF10B981), 
                modifier = Modifier.weight(1f)
            )
        }
        BudgetOverviewCard(
            title = "Pending",
            amount = totalPending,
            icon = Icons.Default.Receipt,
            iconColor = Color(0xFFF59E0B), 
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun BudgetUsageSection(totalBudget: Double, totalSpent: Double, totalPending: Double, available: Double) {
    val totalAllocated = totalSpent + totalPending
    val usagePercentage = if (totalBudget > 0) (totalAllocated / totalBudget) * 100 else 0.0
    val paidPercentage = if (totalBudget > 0) (totalSpent / totalBudget) else 0.0
    val pendingPercentage = if (totalBudget > 0) ((totalSpent + totalPending) / totalBudget) else 0.0

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Budget Usage", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text("How much of your budget has been allocated", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(16.dp))


            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("${usagePercentage.toInt()}% Used", fontWeight = FontWeight.SemiBold)
                Text(formatAmount(totalBudget), fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color.LightGray.copy(alpha = 0.3f))
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth(pendingPercentage.toFloat())
                        .height(12.dp)
                        .background(Color(0xFFF59E0B)) 
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth(paidPercentage.toFloat())
                        .height(12.dp)
                        .background(Color(0xFF8B5CF6)) 
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            BudgetUsageLegendItem("Paid", totalSpent, Color(0xFF8B5CF6))
            BudgetUsageLegendItem("Pending", totalPending, Color(0xFFF59E0B))
            BudgetUsageLegendItem("Available", available, Color.LightGray)
        }
    }
}

@Composable
fun BudgetUsageLegendItem(label: String, amount: Double, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(12.dp).clip(RoundedCornerShape(6.dp)).background(color))
            Spacer(modifier = Modifier.width(8.dp))
            Text(label, style = MaterialTheme.typography.bodyMedium)
        }
        Text(formatAmount(amount), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun ExpensesByCategorySection(categories: List<CategoryExpense>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Expenses by Category", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            categories.forEachIndexed { index, category ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(category.name, style = MaterialTheme.typography.bodyMedium)
                    Text(
                        "${formatAmount(category.amount)} (${category.percentage.toInt()}%)",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                if (index < categories.size - 1) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f))
                }
            }
        }
    }
}

@Composable
fun AllExpensesHeader() {
    Text(
        text = "All Expenses",
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}

@Composable
fun ExpenseCard(expense: ExpenseEntity, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(expense.title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("${expense.category} • ${expense.vendor}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(expense.date, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (expense.receiptPath != null) {
                    val context = androidx.compose.ui.platform.LocalContext.current
                    Spacer(modifier = Modifier.height(4.dp))
                    TextButton(
                        onClick = { 
                            try {
                                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                                    setDataAndType(android.net.Uri.parse(expense.receiptPath), "*/*")
                                    addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                android.widget.Toast.makeText(context, "Cannot open file", android.widget.Toast.LENGTH_SHORT).show()
                            }
                        },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("View Receipt", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(formatAmount(expense.amount), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                val statusColor = if (expense.status.lowercase() == "paid") Color(0xFF10B981) else Color(0xFFF59E0B)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(statusColor)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(expense.status.uppercase(Locale.US), style = MaterialTheme.typography.labelSmall, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun BudgetManagementTips() {
    Card(
        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Budget Management Tips", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            val tips = listOf(
                "Set a realistic budget before making any commitments",
                "Compare prices from multiple vendors",
                "Ask about package deals and discounts",
                "Keep all receipts and invoices organized",
                "Review your budget regularly and adjust as needed"
            )
            tips.forEach { tip ->
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text("•", modifier = Modifier.padding(end = 8.dp), fontWeight = FontWeight.Bold)
                    Text(tip, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}
