package com.rudraksha.documentone.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {
    @Serializable
    data object Home : Screen()
    @Serializable
    data object Extract : Screen()
    @Serializable
    data object Create : Screen()
    @Serializable
    data object View : Screen()
    @Serializable
    data object Settings : Screen()
}
