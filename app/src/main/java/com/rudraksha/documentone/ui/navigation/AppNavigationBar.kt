package com.rudraksha.documentone.ui.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rudraksha.documentone.utils.navigationItemList

@Preview
@Composable
fun AppNavigationBar(
    items: List<NavigationItem> = navigationItemList,
    onItemClick: (Screen) -> Unit = {},
    modifier: Modifier = Modifier,
    currentRoute: String? = null
) {
    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        tonalElevation = 4.dp,
        windowInsets = NavigationBarDefaults.windowInsets
    ) {
        items.forEach { item ->
            val isSelected = item.route.toString() == currentRoute
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    onItemClick(item.route)
                },
                icon = {
                    Icon(
                        imageVector = item.activeIcon,
                        contentDescription = null,
                        tint = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onPrimary
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                }
            )
        }
    }
}