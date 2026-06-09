package com.bsoft.compose.calculator.routes

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface Route: NavKey {
    @Serializable
    data object Home : Route

    @Serializable
    data object History: Route
}