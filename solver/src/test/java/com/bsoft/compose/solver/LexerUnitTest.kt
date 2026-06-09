package com.bsoft.compose.solver

import com.bsoft.compose.solver.components.Lexer
import com.bsoft.compose.solver.models.Constants
import com.bsoft.compose.solver.models.Input
import com.bsoft.compose.solver.models.InputType
import com.bsoft.compose.solver.models.TokenType
import org.junit.Test
import org.junit.Assert.*

class LexerUnitTest {
    @Test
    fun `hasNext returns false for empty string`(){
        val lexer = Lexer(listOf())
        assertEquals(false, lexer.hasNext)
    }

    @Test
    fun `hasNext returns false for 4 string`(){
        val lexer = Lexer(listOf(Input("4")))
        assertEquals(true, lexer.hasNext)
    }

    @Test
    fun `hasNext returns true for (2 + 2)`(){
        val lexer = Lexer(listOf(Input("2"), Input("+"), Input("2")))
        assertEquals(true, lexer.hasNext)
    }

    @Test
    fun `next returns EndToken for empty string`(){
        val lexer = Lexer(listOf())
        val token = lexer.next()
        assertEquals(TokenType.End, token.type)
    }

    @Test
    fun `next returns NumberToken with value 2 for (2)`(){
        val lexer = Lexer(listOf(Input("2")))
        var token = lexer.next()
        assertEquals(TokenType.Number, token.type)
        assertEquals("2", token.input.value)

        token = lexer.next()
        assertEquals(TokenType.End, token.type)
    }

    @Test
    fun `next returns NumberTokens with value 24 for (2 4)`(){
        val lexer = Lexer(listOf(Input("2"), Input("4")))
        var token = lexer.next()
        assertEquals(TokenType.Number, token.type)
        assertEquals("24", token.input.value)

        token = lexer.next()
        assertEquals(TokenType.End, token.type)
    }

    @Test
    fun `next returns OperatorTokens`(){
        val lexer = Lexer(listOf(Input("${Constants.PLUS_SIGN}"), Input("${Constants.MULTIPLICATION_SIGN}")))
        var token = lexer.next()
        assertEquals(TokenType.Operator, token.type)
        assertEquals(Constants.PLUS_SIGN, token.input.value.first())

        token = lexer.next()
        assertEquals(TokenType.Operator, token.type)
        assertEquals(Constants.MULTIPLICATION_SIGN, token.input.value.first())

        token = lexer.next()
        assertEquals(TokenType.End, token.type)
    }

    @Test
    fun `next returns NameToken with values Bobby and Nobel for (Bobby Nobel)`() {
        val lexer = Lexer(listOf(Input("Bobby"), Input("Nobel")))
        var token = lexer.next()
        assertEquals(TokenType.Name, token.type)
        assertEquals("Bobby", token.input.value)

        token = lexer.next()
        assertEquals(TokenType.Name, token.type)
        assertEquals("Nobel", token.input.value)

        token = lexer.next()
        assertEquals(TokenType.End, token.type)
    }

    @Test
    fun `next returns SymbolToken with values ( and ) for ()`(){
        val lexer = Lexer(listOf(Input("("), Input(")") ))
        var token = lexer.next()
        assertEquals(TokenType.Symbol, token.type)
        assertEquals("(", token.input.value)

        token = lexer.next()
        assertEquals(TokenType.Symbol, token.type)
        assertEquals(")", token.input.value)

        token = lexer.next()
        assertEquals(TokenType.End, token.type)
    }

    @Test
    fun `next returns NameToken, OperationToken and NumberToken `(){
        val lexer = Lexer(listOf(Input("Bobby"), Input("+"), Input("2")))
        var token = lexer.next()
        assertEquals(TokenType.Name, token.type)
        assertEquals("Bobby", token.input.value)

        token = lexer.next()
        assertEquals(TokenType.Operator, token.type)
        assertEquals("+", token.input.value)

        token = lexer.next()
        assertEquals(TokenType.Number, token.type)
        assertEquals("2", token.input.value)

        token = lexer.next()
        assertEquals(TokenType.End, token.type)
    }

    @Test
    fun `next returns NameToken with inverse sin`(){
        val lexer = Lexer(listOf(Input("sin"), Input("-", type = InputType.Power), Input("1", type = InputType.Power), Input("(")))
        var token = lexer.next()
        assertEquals(TokenType.Name, token.type)
        assertEquals(null , token.input.type)
        assertEquals("sin", token.input.value)

        token = lexer.next()
        assertEquals(TokenType.Operator, token.type)
        assertEquals(InputType.Power , token.input.type)
        assertEquals("-", token.input.value)

        token = lexer.next()
        assertEquals(TokenType.Number, token.type)
        assertEquals(InputType.Power , token.input.type)
        assertEquals("1", token.input.value)

        token = lexer.next()
        assertEquals(TokenType.Symbol, token.type)
        assertEquals(null , token.input.type)
        assertEquals("(",  token.input.value)

        token = lexer.next()
        assertEquals(TokenType.End, token.type)
    }
}