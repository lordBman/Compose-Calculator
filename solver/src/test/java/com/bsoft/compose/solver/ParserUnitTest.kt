package com.bsoft.compose.solver

import com.bsoft.compose.solver.components.Parser
import com.bsoft.compose.solver.models.BinaryExpression
import com.bsoft.compose.solver.models.Constants
import com.bsoft.compose.solver.models.DRGMode
import com.bsoft.compose.solver.models.ExpressionType
import com.bsoft.compose.solver.models.Input
import com.bsoft.compose.solver.models.InputType
import com.bsoft.compose.solver.models.NumberExpression
import org.junit.Test
import org.junit.Assert.*
import kotlin.math.asin
import kotlin.math.sin

class ParserUnitTest {
    @Test
    fun `parse returns EndExpression for empty input array`(){
        val parser = Parser(listOf())
        assertEquals(ExpressionType.End, parser.parse().type)
    }

    @Test
    fun `parse throws exception for whitespace`(){
        val parser = Parser(listOf(Input(" ")))
        val exception = assertThrows(Exception::class.java){
            parser.parse()
        }
        assertEquals("expression parsing error: encountered unexpected token( )", exception.message)
    }

    @Test
    fun `parse returns NumberExpression for (2)`(){
        val parser = Parser(listOf(Input("2")))
        val expression = parser.parse()
        assertEquals(ExpressionType.Number, expression.type)
        assertEquals(2.0f, expression.solve(DRGMode.Deg), 0.0f)
    }

    @Test
    fun `parse returns UnitaryExpression for (-2)`(){
        val parser = Parser(listOf(Input("-"), Input("2")))
        val expression = parser.parse()
        assertEquals(ExpressionType.Unitary, expression.type)
        assertEquals(-2.0f, expression.solve(DRGMode.Deg), 0.0f)
    }

    @Test
    fun `parse returns NumberExpression for 2 power 3`() {
        val parser = Parser(listOf(Input("2"), Input("3", type = InputType.Power)))
        val expression = parser.parse()
        assertEquals(ExpressionType.Number, expression.type)
        assertEquals(2.0f, (expression as NumberExpression).number, 0f)

        assertEquals(ExpressionType.Number, expression.power?.type)
        assertEquals(3f, (expression.power as NumberExpression).number, 0f)
        assertEquals(8f, expression.solve(DRGMode.Deg), 0f)
    }

    @Test
    fun `parse() returns NumberExpression for  2 power (3 - 1)`(){
        val parser = Parser(listOf(Input("2"), Input("3", type = InputType.Power), Input("${Constants.MINUS_SIGN}", type = InputType.Power), Input("1", type = InputType.Power)))
        val expression = parser.parse()
        assertEquals(ExpressionType.Number, expression.type)
        assertEquals(2f, (expression as NumberExpression).number, 0f)

        assertEquals(ExpressionType.Binary, expression.power?.type)
        assertEquals(2f, (expression.power as BinaryExpression).solve(DRGMode.Deg), 0f)
        assertEquals(4f, expression.solve(DRGMode.Deg), 0f)
    }

    @Test
    fun `parse() returns NumberExpression for 2 power 3 plus 4`() {
        val parser = Parser(listOf(Input("2"), Input("3", type = InputType.Power), Input("${Constants.PLUS_SIGN}"), Input("4")))
        val expression = parser.parse()
        assertEquals(ExpressionType.Binary, expression.type)
        assertEquals(12f, expression.solve(DRGMode.Deg), 0f)
    }

    @Test
    fun `parse returns BinaryExpression`(){
        val parser = Parser(listOf(Input("2"), Input("${Constants.PLUS_SIGN}"), Input("4")))
        val expression = parser.parse()
        assertEquals(ExpressionType.Binary, expression.type)
        assertEquals(6.0f, expression.solve(DRGMode.Deg), 0.0f)
    }

    @Test
    fun `parse returns BracketExpression for (2 multiplied by 4)`(){
        val parser = Parser(listOf(Input("("), Input("2"), Input("${Constants.MULTIPLICATION_SIGN}"), Input("4"), Input(")")))
        val expression = parser.parse()
        assertEquals(ExpressionType.Bracket, expression.type)
        assertEquals(8f, expression.solve(DRGMode.Deg), 0f)
    }

    @Test
    fun `parse returns BinaryExpression and result 8`(){
        val parser = Parser(listOf(Input("2"), Input("${Constants.MULTIPLICATION_SIGN}"), Input("4")))
        val expression = parser.parse()
        assertEquals(ExpressionType.Binary, expression.type)
        assertEquals(8.0f, expression.solve(DRGMode.Deg), 0.0f)
    }

    @Test
    fun `parse returns CallExpression for sin arithmetic`(){
        val inputs = listOf(Input("sin"), Input("("), Input("2"), Input("${Constants.MULTIPLICATION_SIGN}"), Input("4"), Input(")"))
        val parser = Parser(inputs)
        val expression = parser.parse()
        assertEquals(ExpressionType.Call, expression.type)
        assertEquals(sin(SolverUtils.toRadians(8.0f, DRGMode.Deg)), expression.solve(DRGMode.Deg), 0.0f)
    }

    @Test
    fun `parse returns CallExpression for sin 2 multiplied by 4`() {
        val inputs = listOf(Input("sin"), Input("2"), Input("${Constants.MULTIPLICATION_SIGN}"), Input("4"))
        val parser = Parser(inputs)
        val expression = parser.parse()
        assertEquals(ExpressionType.Binary, expression.type)
        assertEquals(sin(SolverUtils.toRadians(2f, DRGMode.Deg)) * 4, expression.solve(DRGMode.Deg), 0f)
    }

    @Test
    fun `parse returns CallExpression for 30 degrees sin arithmetic`(){
        val inputs = listOf(
            Input("sin"), Input("("), Input("3"), Input("0"), Input(")")
        )
        val parser = Parser(inputs)
        val expression = parser.parse()
        assertEquals(ExpressionType.Call, expression.type)
        assertEquals(0.5f, expression.solve(DRGMode.Deg), 0.0f)
    }

    @Test
    fun `parse() returns CallExpression for inverse sin expression`(){
        val inputs = listOf(
            Input("sin"), Input("${Constants.MINUS_SIGN}", type = InputType.Power), Input("1", type = InputType.Power),
            Input("0"), Input("."), Input("6")
        )
        val parser = Parser(inputs)
        val expression = parser.parse()
        assertEquals(ExpressionType.Call, expression.type)
        assertEquals(SolverUtils.fromRadians(asin(0.6f), DRGMode.Deg), expression.solve(DRGMode.Deg), 0.0f)
    }

    @Test
    fun `parse() returns CallExpression for square root of 144`() {
        val inputs = listOf(
            Input("${Constants.SQUARE_ROOT_SIGN}"), Input("("), Input("1"), Input("4"), Input("4"), Input(")")
        )
        val parser =  Parser(inputs)
        val expression = parser.parse()
        assertEquals(ExpressionType.SquareRoot, expression.type)
        assertEquals(12.0f, expression.solve(DRGMode.Deg), 0.0f)
    }

    @Test
    fun `parse() returns CallExpression for square root of 144 multiplied by 5` (){
        val inputs = listOf(
            Input("${Constants.SQUARE_ROOT_SIGN}"), Input("1"), Input("4"), Input("4"), Input("${Constants.MULTIPLICATION_SIGN}"), Input("5")
        )
        val parser =  Parser(inputs)
        val expression = parser.parse()
        assertEquals(ExpressionType.Binary, expression.type)
        assertEquals(60.0f, expression.solve(DRGMode.Deg), 0.0f)
    }

    @Test
    fun `parse() returns ModifiersExpression for 10 percent` () {
        val inputs = listOf(
            Input("1"), Input("0"), Input("${Constants.PERCENT_SIGN}")
        )
        val parser =  Parser(inputs)
        val expression = parser.parse()
        assertEquals(ExpressionType.Modifier, expression.type)
        assertEquals(0.1f, expression.solve(DRGMode.Deg), 0.00f)
    }

    @Test
    fun `parse() returns ModifiersExpression for 5!` () {
        val inputs = listOf(
            Input("5"), Input("${Constants.FACTORIAL_SYMBOL}")
        )
        val parser =  Parser(inputs)
        val expression = parser.parse()
        assertEquals(ExpressionType.Modifier, expression.type)
        assertEquals(120.0f, expression.solve(DRGMode.Deg), 0.0f)
    }

    @Test
    fun `parse() returns BinaryExpression for complex expression with multiplication root`(){
        val inputs = listOf(
            Input("2"), Input("3", type = InputType.Power), Input("${Constants.MULTIPLICATION_SIGN}"),
            Input("sin"), Input("30")
        )
        val parser = Parser(inputs)
        val expression = parser.parse()

        assertEquals(ExpressionType.Binary, expression.type)
        assertEquals(Constants.MULTIPLICATION_SIGN, (expression as BinaryExpression).operator[0])
        assertEquals(4.0f, expression.solve(DRGMode.Deg), 0.0f)
    }

    @Test
    fun `parse() returns BinaryExpression for complex expression 2 with multiplication root`(){
        val inputs = listOf(
            Input("2"), Input("3", type = InputType.Power), Input("sin"), Input("30")
        )
        val parser = Parser(inputs)
        val expression = parser.parse()

        assertEquals(ExpressionType.Binary, expression.type)
        assertEquals(Constants.MULTIPLICATION_SIGN, (expression as BinaryExpression).operator[0])
        assertEquals(4.0f, expression.solve(DRGMode.Deg), 0.0f)
    }

    @Test
    fun `parse() returns BinaryExpression for complex expression 2 with Power root`(){
        val inputs = listOf(
            Input("2"), Input("(", type = InputType.Power), Input("4", type = InputType.Power),
            Input("${Constants.MULTIPLICATION_SIGN}", type = InputType.Power), Input("sin", type = InputType.Power),
            Input("3", type = InputType.Power), Input("0", type = InputType.Power), Input(")")
        )
        val parser = Parser(inputs)
        val expression = parser.parse()
        assertEquals(ExpressionType.Number, expression.type)
        assertEquals(4.0f, expression.solve(DRGMode.Deg), 0.0f)
    }

    @Test
    fun `parse() returns BinaryExpression for complex binary expression`() {
        val inputs = listOf(
            Input("2"), Input("${Constants.PLUS_SIGN}"), Input("3"), Input("("), Input("5"), Input("${Constants.MULTIPLICATION_SIGN}"),
            Input("2"), Input(")"), Input("${Constants.MINUS_SIGN}"), Input("4"), Input("${Constants.FACTORIAL_SYMBOL}")
        )
        val parser = Parser(inputs)
        val  expression = parser.parse()

        assertEquals(ExpressionType.Binary, expression.type)
        assertEquals(8.0f, expression.solve(DRGMode.Deg), 0.0f)
    }

    @Test
    fun `parse returns LogExpression for log 100`(){
        val inputs = listOf(
            Input("log"), Input( "1"), Input("0"), Input("0")
        )
        val parser = Parser(inputs)
        val expression = parser.parse()

        assertEquals(ExpressionType.Log, expression.type)
        assertEquals(2f, expression.solve(DRGMode.Deg), 0f)
    }

    @Test
    fun `parse returns BinaryExpression for 5P2`(){
        val inputs = listOf(
            Input("5"), Input("P"), Input("2")
        )
        val parser = Parser(inputs)
        val expression = parser.parse()

        assertEquals(ExpressionType.Binary, expression.type)
        assertEquals(20f, expression.solve(DRGMode.Deg), 0f)
    }

    @Test
    fun `parse returns BinaryExpression for 5C2`(){
        val inputs = listOf(
            Input("5"), Input("C"), Input("2")
        )
        val parser = Parser(inputs)
        val expression = parser.parse()

        assertEquals(ExpressionType.Binary, expression.type)
        assertEquals(10f, expression.solve(DRGMode.Deg), 0f)
    }

    @Test
    fun `parse() returns LogExpression for log 8 with base 2`(){
        val inputs = listOf(
            Input("log"), Input("2", type = InputType.Base), Input("8")
        )
        val parser = Parser(inputs)
        val expression = parser.parse()

        assertEquals(ExpressionType.Log, expression.type)
        assertEquals(3f, expression.solve(DRGMode.Deg), 0f)
    }

    @Test
    fun `parse returns SquareRootExpression SquareRootSign of 8 with base 3`(){
        val inputs = listOf(
            Input("3", type = InputType.Power), Input("${Constants.SQUARE_ROOT_SIGN}"), Input("8")
        )
        val parser = Parser(inputs)
        val expression = parser.parse()

        assertEquals(ExpressionType.SquareRoot, expression.type)
        assertEquals(2f, expression.solve(DRGMode.Deg), 0f)
    }

    @Test
    fun `parse() returns BinaryExpression for 2 multiplied by SquareRoot of 8 with base 3`(){
        val inputs = listOf(
            Input("2"), Input("${Constants.MULTIPLICATION_SIGN}"), Input("3", type = InputType.Power), Input("${Constants.SQUARE_ROOT_SIGN}"), Input("8")
        )
        val parser = Parser(inputs)
        val expression = parser.parse()

        assertEquals(ExpressionType.Binary, expression.type)
        assertEquals(4f, expression.solve(DRGMode.Deg), 0f)
    }

    @Test
    fun `parse() ans should return 2`(){
        val inputs = listOf(
            Input(value = "ans")
        )

        val parser = Parser(inputs, 2f)
        val expression = parser.parse()

        assertEquals(ExpressionType.Number, expression.type)
        assertEquals(2f, expression.solve(DRGMode.Deg), 0f)
    }

    @Test
    fun `parse() ans ${MultiplicationSign} 2 should return 20`(){
        val inputs = listOf(
            Input(value = "ans"), Input("${Constants.MULTIPLICATION_SIGN}"), Input(value = "2")
        )

        val parser = Parser(inputs, 10f)
        val expression = parser.parse()

        assertEquals(ExpressionType.Binary, expression.type)
        assertEquals(20f, expression.solve(DRGMode.Deg), 0f)
    }

    @Test
    fun `parse() 2ans should return 20` (){
        val inputs = listOf(
            Input(value = "2"), Input(value = "ans")
        )

        val parser = Parser(inputs, 10f)
        val expression = parser.parse()

        assertEquals(ExpressionType.Binary, expression.type)
        assertEquals(20f, expression.solve(DRGMode.Deg), 0f)
    }

    @Test
    fun `parse() ans power 2 should return 9` (){
        val inputs = listOf(
            Input(value = "ans"), Input(value = "2", type = InputType.Power)
        )

        val parser = Parser(inputs, 3f)
        val expression = parser.parse()

        assertEquals(ExpressionType.Number, expression.type)
        assertEquals(9f, expression.solve(DRGMode.Deg), 0f)
    }

    @Test
    fun `parse() 2 power ans should return 8`(){
        val inputs = listOf(
            Input(value = "2"), Input(value = "ans", type = InputType.Power)
        )

        val parser = Parser(inputs, 3f)
        val expression = parser.parse()

        assertEquals(ExpressionType.Number, expression.type)
        assertEquals(8f, expression.solve(DRGMode.Deg), 0f)
    }

    @Test
    fun `parse() ans(2 ${PlusSign} 1)  should return 9` (){
        val inputs = listOf(
                Input(value = "ans"), Input(value = "("), Input(value = "2"), Input(value = "${Constants.PLUS_SIGN}"), Input(value = "1"), Input(value = ")")
        )

        val parser = Parser(inputs, 3f)
        val expression = parser.parse()

        assertEquals(ExpressionType.Binary, expression.type)
        assertEquals(9f, expression.solve(DRGMode.Deg), 0f)
    }

    @Test
    fun `parse() 3(ans ${PlusSign} 1)  should return 12` (){
        val inputs = listOf(
            Input(value = "3"),Input(value = "("), Input(value = "ans"), Input(value = "${Constants.PLUS_SIGN}"), Input(value = "1"), Input(value = ")")
        )

        val parser = Parser(inputs, 3f)
        val expression = parser.parse()

        assertEquals(ExpressionType.Binary, expression.type)
        assertEquals(12f, expression.solve(DRGMode.Deg), 0f)
    }
}