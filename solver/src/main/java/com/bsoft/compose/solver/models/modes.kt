package com.bsoft.compose.solver.models

import kotlinx.serialization.Serializable

@Serializable
enum class KeyMode(val value: String){
    Normal("Number"), Power("Power"), Base( "Base")
}

@Serializable
enum class DRGMode(val title: String){
    Deg("Deg"), Rad("Rad"), Grad("Grad")
}

@Serializable
enum class HypMode(val title: String){
    On("Hyp"), Off("")
}

@Serializable
data class ModesData(val drg: DRGMode = DRGMode.Deg, val key: KeyMode = KeyMode.Normal, val hyp: HypMode = HypMode.Off)