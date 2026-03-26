package com.example.memora.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun MemoraAppDrawer(
    drawerState: DrawerState,
    currentRoute: String,
    onNavigateToDashboard: () -> Unit = {},
    onNavigateToPlanning: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToBudget: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Memora",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text(text = "Dashboard") },
                    selected = currentRoute == "home",
                    onClick = { 
                        scope.launch { 
                            drawerState.close() 
                            onNavigateToDashboard()
                        } 
                    },
                    icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    label = { Text(text = "Planning") },
                    selected = currentRoute == "planning",
                    onClick = { 
                        scope.launch { 
                            drawerState.close() 
                            onNavigateToPlanning() 
                        } 
                    },
                    icon = { Icon(Icons.Filled.Edit, contentDescription = null) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    label = { Text(text = "Marketplace") },
                    selected = currentRoute == "marketplace",
                    onClick = { scope.launch { drawerState.close() } },
                    icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = null) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    label = { Text(text = "Budget") },
                    selected = currentRoute == "budget",
                    onClick = { 
                        scope.launch { 
                            drawerState.close() 
                            onNavigateToBudget() 
                        } 
                    },
                    icon = { Icon(Icons.Filled.DateRange, contentDescription = null) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    label = { Text(text = "Ceremony") },
                    selected = currentRoute == "ceremony",
                    onClick = { scope.launch { drawerState.close() } },
                    icon = { Icon(Icons.Filled.Star, contentDescription = null) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    label = { Text(text = "Guests") },
                    selected = currentRoute == "guests",
                    onClick = { scope.launch { drawerState.close() } },
                    icon = { Icon(Icons.Filled.Person, contentDescription = null) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    label = { Text(text = "Memorial") },
                    selected = currentRoute == "memorial",
                    onClick = { scope.launch { drawerState.close() } },
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    label = { Text(text = "Support") },
                    selected = currentRoute == "support",
                    onClick = { scope.launch { drawerState.close() } },
                    icon = { Icon(Icons.Filled.Info, contentDescription = null) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
                NavigationDrawerItem(
                    label = { Text(text = "Profile") },
                    selected = currentRoute == "profile",
                    onClick = { 
                        scope.launch { 
                            drawerState.close() 
                            onNavigateToProfile()
                        } 
                    },
                    icon = { Icon(Icons.Filled.AccountCircle, contentDescription = null) },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        },
        content = content
    )
}
