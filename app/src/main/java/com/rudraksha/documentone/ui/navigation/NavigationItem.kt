package com.rudraksha.documentone.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Close

data class NavigationItem(
    val activeIcon: ImageVector = Icons.Default.Close,
    val inActiveIcon: ImageVector = Icons.Outlined.Close,
    val label: String = "",
    val route: Screen,
    val contentDescription: String = ""
)