package com.bsoft.compose.solver.components

import com.bsoft.compose.solver.models.BinaryExpression
import com.bsoft.compose.solver.models.BracketExpression
import com.bsoft.compose.solver.models.CallExpression
import com.bsoft.compose.solver.models.ConstantExpression
import com.bsoft.compose.solver.models.Constants
import com.bsoft.compose.solver.models.EndExpression
import com.bsoft.compose.solver.models.Expression
import com.bsoft.compose.solver.models.ExpressionType
import com.bsoft.compose.solver.models.Input
import com.bsoft.compose.solver.models.InputType
import com.bsoft.compose.solver.models.LogExpression
import com.bsoft.compose.solver.models.ModifierExpression
import com.bsoft.compose.solver.models.NumberExpression
import com.bsoft.compose.solver.models.SquareRootExpression
import com.bsoft.compose.solver.models.Token
import com.bsoft.compose.solver.models.TokenType
import com.bsoft.compose.solver.models.UnitaryExpression

class Parser(data: List<Input>, var ans: Float? = null) {
    private val lexer: Lexer = Lexer(data)
    private var _current: Token? = null
    private val current: Token
        get(){
            if(this._current != null){
                return this._current!!
            }
            this._current = this.lexer.next()
            return this._current!!
        }
    private val hasNext: Boolean
        get()= this.current.type != TokenType.End

    private fun consumeToken(): Input{
        val init = current.input
        _current = lexer.next()

        return init
    }

    private fun checkForPowerExpression(): Expression?{
        var sup: Expression? = null
        if(this.current.input.type == InputType.Power){
            sup = this.parseExpression(InputType.Power)
        }
        return sup
    }

    private fun checkForBaseNumber(): NumberExpression?{
        var sup: NumberExpression? = null
        if(this.current.input.type == InputType.Base && this.current.type === TokenType.Number){
            sup = this.parseNumber()
        }
        return sup
    }

    fun parse(): Expression {
        if(hasNext){
            return parseExpression(InputType.Number)
        }
        return EndExpression()
    }

    private fun parseExpression(inputType: InputType, initLeft: Expression? = null): Expression {
        var left = parseMultiplier(inputType = inputType, initLeft = initLeft)
        while(this.current.input.type == inputType && arrayOf("${Constants.PLUS_SIGN}", "${Constants.MINUS_SIGN}").contains (current.input.value )) {
            val operator = consumeToken()
            left = if(operator.value == "${Constants.MINUS_SIGN}"){
                val init = UnitaryExpression(operator.value, parseFactor(inputType = inputType))
                if (current.type == TokenType.End){
                    BinaryExpression("${Constants.PLUS_SIGN}", left, init)
                }else{
                    BinaryExpression("${Constants.PLUS_SIGN}", left, parseMultiplier(inputType = inputType, init))
                }
            }else{
                BinaryExpression("${Constants.PLUS_SIGN}", left, parseMultiplier(inputType = inputType))
            }
        }
        return left
    }

    private fun parseMultiplier(inputType: InputType, initLeft: Expression? = null): Expression {
        var left = parseDivider(inputType, initLeft)
        while(this.current.input.type == inputType && current.input.value == "${Constants.MULTIPLICATION_SIGN}") {
            val operator = consumeToken()
            left = BinaryExpression(operator = operator.value, left = left, parseDivider(inputType = inputType))
        }
        return left
    }

    private fun parseDivider(inputType: InputType, initLeft: Expression? = null): Expression {
        var left = parsePC(inputType = inputType, initLeft = initLeft)
        while(this.current.input.type == inputType && current.input.value == "${Constants.DIVISION_SIGN}") {
            val operator = consumeToken()
            left = BinaryExpression(operator = operator.value, left = left, parsePC(inputType))
        }
        return left
    }

    private fun parsePC(inputType: InputType, initLeft: Expression? = null): Expression {
        var left = this.parseOf(inputType = inputType, initLeft = initLeft)
        while (this.current.input.type === inputType && arrayListOf("${Constants.PERMUTATION}", "${Constants.COMBINATION}").contains(this.current.input.value)){
            val operator = this.consumeToken()
            left = BinaryExpression(operator.value, left, this.parseOf(inputType = inputType))
        }
        return left
    }

    private fun parseOf(inputType: InputType, initLeft: Expression? = null): Expression{
        var left = this.parseConstants(inputType = inputType, initLeft = initLeft)
        val leftEligible = listOf(ExpressionType.Number, ExpressionType.Bracket, ExpressionType.Unitary, ExpressionType.Constant).contains(left.type)
        val currentEligible = this.current.input.type == inputType && (this.current.input.value == "(" || arrayOf(TokenType.Name, TokenType.SquareRoot).contains(this.current.type))
        if (leftEligible && currentEligible){
            left = BinaryExpression("${Constants.MULTIPLICATION_SIGN}", left, this.parseConstants(inputType = inputType))
        }
        return left
    }

    private fun parseConstants(inputType: InputType, initLeft: Expression? = null): Expression{
        var left = initLeft ?: parseFactor(inputType)
        if (this.current.input.type == inputType && this.current.type == TokenType.Constant){
            val constant = this.consumeToken()
            val power = this.checkForPowerExpression()
            val expression = ConstantExpression(constant.value, power)
            left = BinaryExpression("${Constants.MULTIPLICATION_SIGN}", left, expression)
        }
        return left
    }

    private fun parseFactor(inputType: InputType): Expression {
        if(this.current.input.type == inputType) {
            if(current.type == TokenType.Symbol && current.input.value == "("){
                return parseBracket(type = inputType)
            }

            if(current.type == TokenType.Operator && arrayOf("${Constants.PLUS_SIGN}", "${Constants.MINUS_SIGN}").contains(current.input.value)){
                val token = consumeToken()
                return UnitaryExpression(token.value, parseFactor(inputType = inputType))
            }

            if(current.type == TokenType.Number){
                val expression = this.parseNumber()
                if (this.hasNext && Constants.MODIFIERS.contains(this.current.input.value)) {
                    return ModifierExpression(this.consumeToken().value, expression)
                }
                return expression
            }

            if(this.current.type == TokenType.SquareRoot){
                return this.parseSquareRoot(inputType)
            }

            if (this.current.type == TokenType.Name) {
                val name = this.consumeToken()
                val base = if (name.type == InputType.Number) { this.checkForBaseNumber() } else { null }
                val power = if(name.type == InputType.Number) { this.checkForPowerExpression() } else { null }
                if(name.value == "ans"){
                    return NumberExpression(number = this.ans ?: 0f, power = power)
                }else{
                    when (this.current.input.type) {
                        inputType if this.current.type == TokenType.Number -> {
                            if (name.value == "log") {
                                return LogExpression(this.parseNumber(), power, base?.number)
                            }
                            return CallExpression(name.value, this.parseNumber(), power)
                        }
                        inputType if this.current.input.value == "(" -> {
                            if (name.value == "log") {
                                return LogExpression(this.parseBracket(inputType), power, base?.number)
                            }
                            return CallExpression(name.value, this.parseBracket(inputType), power)
                        }
                        else -> {
                            println("${name.value} { power: ${power}, base: $base }")
                            throw Error("expecting a Number or Open Bracket Token'(' but found ${this.current.input.value} instead")
                        }
                    }
                }
            }
        }

        if(inputType == InputType.Number && this.current.input.type == InputType.Power && this.current.type === TokenType.Number){
            val baseToken = this.consumeToken()
            if (this.current.input.value == "${Constants.SQUARE_ROOT_SIGN}" && this.current.input.type == InputType.Number) {
                return this.parseSquareRoot(inputType, baseToken)
            }
        }

        if (current.type == TokenType.End){
            return EndExpression()
        }
        throw Exception("expression parsing error: encountered unexpected token(${current.input.value})")
    }

    private fun parseBracket(type: InputType): BracketExpression{
        this.consumeToken()
        val expression = this.parseExpression(type)
        if(this.current.type == TokenType.Symbol && this.current.input.value == ")" || this.current.type == TokenType.End){
            this.consumeToken()
            val power = this.checkForPowerExpression()
            return BracketExpression(expression, power)
        }
        throw Error("missing closing bracket Token(')' but found ${this.current} instead)")
    }

    private fun parseNumber(): NumberExpression {
        val token = this.consumeToken()
        val value = token.value.toFloat()
        val power = if (token.type == InputType.Number) { this.checkForPowerExpression() } else { null }
        return NumberExpression(value, power)
    }

    private fun parseSquareRoot(type: InputType? = null, baseToken: Input? = null): SquareRootExpression {
        this.consumeToken()
        val base = baseToken?.value?.toFloat() ?: 2f
        return if (this.current.input.type === type && this.current.type == TokenType.Number) {
            SquareRootExpression(this.parseNumber(), base)
        }else if (this.current.input.type === type && this.current.input.value == "(") {
            SquareRootExpression(this.parseBracket(type), base)
        } else {
            println("${baseToken}: ${this.current}")
            throw Error("expecting a Square Root (${Constants.SQUARE_ROOT_SIGN}) Token'(' but found ${this.current.input.value} instead")
        }
    }
}