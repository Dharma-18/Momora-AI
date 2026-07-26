package com.momora.ai.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Navigation routes for the Momora app.
 */
sealed class Screen(val route: String, val title: String) {
    data object Home : Screen("home", "Home")
    data object Memory : Screen("memory", "Memory")
    data object Chat : Screen("chat", "Chat")
    data object Timeline : Screen("timeline", "Timeline")
    data object Profile : Screen("profile", "Profile")
}

/**
 * Bottom navigation items matching the HTML mockup's BottomNavBar.
 */
data class BottomNavItem(
    val screen: Screen,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
)

val bottomNavItems = listOf(
    BottomNavItem(
        screen = Screen.Home,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    BottomNavItem(
        screen = Screen.Memory,
        selectedIcon = Icons.Filled.AutoAwesome, // Using database-like icon
        unselectedIcon = Icons.Outlined.AutoAwesome,
    ),
    BottomNavItem(
        screen = Screen.Chat,
        selectedIcon = Icons.Filled.AutoAwesome,
        unselectedIcon = Icons.Outlined.AutoAwesome,
    ),
    BottomNavItem(
        screen = Screen.Timeline,
        selectedIcon = Icons.Filled.History,
        unselectedIcon = Icons.Outlined.History,
    ),
    BottomNavItem(
        screen = Screen.Profile,
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
    ),
)
