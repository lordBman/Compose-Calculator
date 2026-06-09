package com.bsoft.compose.solver.models

import com.bsoft.compose.solver.SolverUtils

import kotlin.math.E
import kotlin.math.PI
import kotlin.math.acos
import kotlin.math.acosh
import kotlin.math.asin
import kotlin.math.asinh
import kotlin.math.atan
import kotlin.math.atanh
import kotlin.math.cos
import kotlin.math.cosh
import kotlin.math.exp
import kotlin.math.sin
import kotlin.math.tan
import kotlin.math.ln
import kotlin.math.log
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sinh
import kotlin.math.sqrt
import kotlin.math.tanh

enum class ExpressionType{
    Number, Unitary, Binary, Bracket, Call, Modifier, Constant, Log, SquareRoot, End
}

abstract class Expression(val type: ExpressionType){
    abstract fun solve(mode: DRGMode): Float
    abstract fun display(space: String = ""): String
}

class NumberExpression(val number: Float, val power: Expression? = null): Expression(ExpressionType.Number){
    override fun solve(mode: DRGMode): Float {
        if(power == null){
            return number
        }
        return number.pow(power.solve(mode))
    }

    override fun display(space: String): String {
        return "$space$this"
    }

    override fun toString(): String {
        return "Number Expression($number)"
    }
}

class UnitaryExpression(val operator: String, val expression: Expression): Expression(ExpressionType.Unitary){
    override fun solve(mode: DRGMode): Float {
        return when(operator[0]){
            Constants.MINUS_SIGN -> - expression.solve(mode)
            Constants.PLUS_SIGN -> expression.solve(mode)
            else -> throw Error("unknown unitary operation:$operator")
        }
    }

    override fun display(space: String): String {
        return "${space}Unitary Expression{ operator: $operator}\n${expression.display("$space    ")}"
    }

    override fun toString(): String {
        return "Unitary Expression{ operator: $operator, expression: $expression }"
    }
}

class BinaryExpression(val operator: String, val left: Expression, val right: Expression): Expression(ExpressionType.Binary){
    override fun solve(mode: DRGMode): Float {
        return when(operator[0]){
            Constants.PLUS_SIGN -> left.solve(mode) + right.solve(mode)
            Constants.MINUS_SIGN -> left.solve(mode) - right.solve(mode)
            Constants.MULTIPLICATION_SIGN -> left.solve(mode) * right.solve(mode)
            Constants.DIVISION_SIGN -> {
                val rightResult = right.solve(mode)
                val leftResult = left.solve(mode)
                if(rightResult == 0.0f){
                    throw Error("math error: $leftResult is indivisible by 0")
                }
                left.solve(mode) / right.solve(mode)
            }
            Constants.PERMUTATION -> SolverUtils.permutation(left.solve(mode), right.solve(mode))
            Constants.COMBINATION -> SolverUtils.combination(left.solve(mode), right.solve(mode))
            else -> throw Error("unknown binary operation: $operator")
        }
    }

    override fun display(space: String): String {
        return "${space}Binary Expression{ operator: $operator}\n${left.display("$space    ")}\n${right.display("$space    ")}"
    }

    override fun toString(): String {
        return "Binary Expression{ operator: $operator, left: $left, right: $right }"
    }
}

class BracketExpression(val expression: Expression, val power: Expression? = null): Expression(ExpressionType.Bracket){
    override fun solve(mode: DRGMode): Float {
        if(power == null){
            return expression.solve(mode)
        }
        return expression.solve(mode).pow(power.solve(mode))
    }

    override fun display(space: String): String {
        return "${space}Bracket Expression\n${expression.display("$space    ")}"
    }

    override fun toString(): String {
        return "Bracket Expression(expression: $expression)"
    }
}

class CallExpression(val name: String, val expression: Expression, val power: Expression? = null): Expression(ExpressionType.Call){
    override fun solve(mode: DRGMode): Float {
        var powerResult = if (this.power == null) { 1f } else{ this.power.solve(mode) }
        val result = when(name){
            "sin" -> {
                if(powerResult < 0){
                    powerResult *= -1
                    SolverUtils.fromRadians(asin(expression.solve(mode)), mode)
                }else{
                    sin(SolverUtils.toRadians(expression.solve(mode), mode))
                }
            }
            "cos" -> {
                if(powerResult < 0){
                    powerResult *= -1
                    SolverUtils.fromRadians(acos(expression.solve(mode)), mode)
                }else{
                    cos(SolverUtils.toRadians(expression.solve(mode), mode))
                }
            }
            "tan" -> {
                if(powerResult < 0){
                    powerResult *= -1
                    SolverUtils.fromRadians(atan(expression.solve(mode)), mode)
                }
                tan(SolverUtils.toRadians(expression.solve(mode), mode))
            }
            "sinh" -> {
                if(powerResult < 0){
                    powerResult *= -1
                    SolverUtils.fromRadians(asinh(expression.solve(mode)), mode)
                }else{
                    sinh(SolverUtils.toRadians(expression.solve(mode), mode))
                }
            }
            "cosh" -> {
                if(powerResult < 0){
                    powerResult *= -1
                    SolverUtils.fromRadians(acosh(expression.solve(mode)), mode)
                }else{
                    cosh(SolverUtils.toRadians(expression.solve(mode), mode))
                }
            }
            "tanh" -> {
                if(powerResult < 0){
                    powerResult *= -1
                    SolverUtils.fromRadians(atanh(expression.solve(mode)), mode)
                }else{
                    tanh(SolverUtils.toRadians(expression.solve(mode), mode))
                }
            }
            "csc" -> {
                if(powerResult < 0){
                    powerResult *= -1
                    SolverUtils.fromRadians(asin(1 / expression.solve(mode)), mode)
                }else{
                    1 / sin(SolverUtils.toRadians(expression.solve(mode), mode))
                }
            }
            "sec" -> {
                if(powerResult < 0){
                    powerResult *= -1
                    SolverUtils.fromRadians(acos(1 / expression.solve(mode)), mode)
                }else{
                    1 / cos(SolverUtils.toRadians(expression.solve(mode), mode))
                }
            }
            "cot" -> {
                if(powerResult < 0){
                    powerResult *= -1
                    SolverUtils.fromRadians(atan(1 / expression.solve(mode)), mode)
                }else{
                    1 / tan(SolverUtils.toRadians(expression.solve(mode), mode))
                }
            }
            "csch" -> {
                if(powerResult < 0){
                    powerResult *= -1
                    SolverUtils.fromRadians(asinh(1 / expression.solve(mode)), mode)
                }else{
                    1 / sinh(SolverUtils.toRadians(expression.solve(mode), mode))
                }
            }
            "sech" -> {
                if(powerResult < 0){
                    powerResult *= -1
                    SolverUtils.fromRadians(acosh(1 / expression.solve(mode)), mode)
                }else{
                    1 / cosh(SolverUtils.toRadians(expression.solve(mode), mode))
                }
            }
            "coth" -> {
                if(powerResult < 0){
                    powerResult *= -1
                    SolverUtils.fromRadians(atanh(1 / expression.solve(mode)), mode)
                }else{
                    1 / tanh(SolverUtils.toRadians(expression.solve(mode), mode))
                }
            }
            "ln" -> {
                if(powerResult < 0){
                    powerResult *= -1
                    exp(expression.solve(mode))
                }else{
                    ln(expression.solve(mode))
                }
            }
            else -> throw Error("unknown math symbol: $name")
        }
        return result.pow(powerResult)
    }

    override fun display(space: String): String {
        return "${space}Call Expression{ name: $name}\n${expression.display("$space    ")}"
    }

    override fun toString(): String {
        return "Call Expression{ name: $name, arg: $expression }"
    }
}

class ModifierExpression(val name: String, val expression: Expression): Expression(ExpressionType.Modifier){
    override fun solve(mode: DRGMode): Float {
        return when(name[0]){
            Constants.PERCENT_SIGN -> (expression.solve(mode) * 0.01).toFloat()
            Constants.FACTORIAL_SYMBOL -> SolverUtils.factorial(expression.solve(mode).toInt())
            else -> throw Error("unknown modifier encountered: (${this.name})")
        }
    }

    override fun display(space: String): String {
        return "${space}Modifier Expression{ name: ${this.name}}${this.expression.display("$space    ")}"
    }

    override fun toString(): String {
        return "Modifier Expression{ name: $name, expression: $expression }"
    }
}

class ConstantExpression(val name: String, val power: Expression?): Expression(ExpressionType.Constant){
    override fun solve(mode: DRGMode): Float {
        val result = when(name[0]){
            Constants.PI_SYMBOL -> PI.toFloat()
            Constants.EXPONENTIAL_SYMBOL -> E.toFloat()
            else -> throw Error("unknown constant encountered: ${this.name}")
        }

        if(power == null){
            return result
        }
        return result.pow(power.solve(mode))
    }

    override fun display(space: String): String {
        return "${space}Constant Expression{ name: ${this.name}}}"
    }

    override fun toString(): String {
        return "Constant Expression{ name: ${this.name}}}"
    }
}

class LogExpression(val expression: Expression, val power: Expression?, val base: Float?): Expression(ExpressionType.Log){
    override fun solve(mode: DRGMode): Float {
        var powerResult: Float = if (this.power == null) { 1f } else { this.power.solve(mode) }
        val baseResult: Float = this.base ?: 10f
        val result = if(powerResult < 0){
            powerResult *= -1
            this.expression.solve(mode).pow(baseResult)
        }else{
            log(this.expression.solve(mode), baseResult)
        }
        return result.pow(powerResult)
    }

    override fun display(space: String): String {
        return "${space}Log Expression(Base: ${this.base})\n${this.expression.display("$space    ")}\nPower: ${this.power?.display("$space    ")}}"
    }
}

class SquareRootExpression(val expression: Expression, val base: Float = 2f): Expression(ExpressionType.SquareRoot){
    override fun solve(mode: DRGMode): Float {
        return this.expression.solve(mode).pow(1 / base)
    }

    override fun display(space: String): String {
        return "${space}SquareRoot Expression{ name: ${this.base}}\n${this.expression.display("$space    ")}"
    }
}

class EndExpression(): Expression(ExpressionType.End){
    override fun solve(mode: DRGMode): Float {
        return 0.0f
    }

    override fun display(space: String): String {
        return "${space}End Expression"
    }

    override fun toString(): String {
        return "End Expression"
    }
}