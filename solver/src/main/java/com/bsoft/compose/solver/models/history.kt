package com.bsoft.compose.solver.models

import kotlinx.serialization.Serializable
import java.util.Calendar

@Serializable
data class History(
    val data: List<Datum>,
    val year: Int = Calendar.getInstance().get(Calendar.YEAR),
    val month: Int = Calendar.getInstance().get(Calendar.MONTH),
    val day: Int = Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
