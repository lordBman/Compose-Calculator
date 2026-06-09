package com.bsoft.compose.solver.models

import kotlinx.serialization.Serializable

@Serializable
enum class InputType(val value: String){
    Power("power"), Base("base"), Number("number")
}

@Serializable
data class Input(val value: String, val type: InputType = InputType.Number)

enum class TokenType{
    Number, Name, Operator, Symbol, Modifier, Constant, SquareRoot, End
}

open class Token(val type: TokenType, val input: Input){
    override fun toString(): String {
        return "Token{ type: $type, value: $input }"
    }
}