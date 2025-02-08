package com.rudraksha.documentone.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.DocumentScanner
import com.rudraksha.documentone.ui.navigation.NavigationItem
import com.rudraksha.documentone.ui.navigation.Screen

val navigationItemList: List<NavigationItem> = listOf(
    NavigationItem(
        activeIcon = Icons.Filled.Description,
        inActiveIcon = Icons.Outlined.Description,
        label = "Home",
        route = Screen.Home
    ),
    NavigationItem(
        activeIcon = Icons.Filled.Create,
        inActiveIcon = Icons.Outlined.Create,
        label = "Create",
        route = Screen.Create
    ),
    NavigationItem(
        activeIcon = Icons.Filled.DocumentScanner,
        inActiveIcon = Icons.Outlined.DocumentScanner,
        label = "Extract",
        route = Screen.Extract
    ),

)