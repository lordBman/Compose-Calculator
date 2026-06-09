package com.bsoft.compose.solver.models

import kotlinx.serialization.Serializable

@Serializable
data class Datum(val inputs: List<Input> = listOf(), val solution: String? = null)

data class SolverData(
    val current: Datum = Datum(),
    val ans: Float? = null,
    val error: String? = null)