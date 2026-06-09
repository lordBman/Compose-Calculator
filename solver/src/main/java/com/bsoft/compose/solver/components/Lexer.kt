package com.bsoft.compose.solver.components

import com.bsoft.compose.solver.models.Constants
import com.bsoft.compose.solver.models.Input
import com.bsoft.compose.solver.models.Token
import com.bsoft.compose.solver.models.TokenType

class Lexer(val data: List<Input>){
    private var currentIndex = 0

    val hasNext: Boolean
        get() = this.currentIndex < this.data.size

    private val current: Input
        get() {
            if(this.hasNext){
                return this.data[this.currentIndex]
            }
            return Input("")
        }


    private fun consume(): Input{
        val init = this.current
        this.currentIndex += 1

        return init
    }

    fun next(): Token {
        if(hasNext){
            if(this.current.value.length == 1 && this.current.value[0] == Constants.SQUARE_ROOT_SIGN){
                return Token(TokenType.SquareRoot, this.consume())
            }else if(Constants.OPERATORS.contains(current.value)){
                return Token(TokenType.Operator, this.consume())
            }else if(Constants.CONSTANTS.contains(current.value)){
                return Token(TokenType.Constant, this.consume())
            }else if(Constants.MODIFIERS.contains(current.value)){
                return Token(TokenType.Modifier, this.consume())
            }else if(current.value.first() == '.' || current.value.first().isDigit()){
                return Token(TokenType.Number,getNumber())
            }else if(current.value.first().isLetter()){
                return Token(TokenType.Name, getName())
            }else if(hasNext){
                return Token(TokenType.Symbol, this.consume())
            }
        }
        return Token(TokenType.End, Input(""))
    }

    private fun getNumber(): Input{
        val inputType = this.current.type
        var builder =  this.consume().value
        while (this.hasNext && this.current.type === inputType && (this.current.value === "." || this.current.value.first().isDigit())) {
            builder += this.consume().value
        }
        return Input(builder, inputType)
    }

    private fun getName(): Input{
        val inputType = this.current.type
        val builder = this.consume().value

        return Input(builder, inputType)
    }
}