package com.example.progresstracker.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Task
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Task
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomBarDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector
) {
    DailyTaskMainScreen(
        route = Screen.DailyTaskScreen.route,
        selectedIcon = Icons.Default.Task,
        unSelectedIcon = Icons.Outlined.Task
    ),
    GoalMainScreen(
        route = Screen.GoalScreen.route,
        selectedIcon = Icons.Default.EmojiEvents,
        unSelectedIcon = Icons.Outlined.EmojiEvents
    )
}
