package com.bsoft.compose.solver

import com.bsoft.compose.solver.models.DRGMode
import com.bsoft.compose.solver.models.Input
import org.junit.Test

import org.junit.Assert.*

class SolverUtilsUnitTest {
    val emptyArray: List<Input> = emptyList()
    val numberArray: List<Input> = listOf(Input("2"), Input("3"), Input("4"))
    val binaryExpressionArray: List<Input> = listOf(Input("1"), Input("2"), Input("+"), Input("3"), Input("."), Input("2"))
    val functionCallArray: List<Input> = listOf(Input("sin"), Input("2"), Input("."), Input("4"))

    @Test
    fun `expression returns 0 for empty string array`() {
        val result = SolverUtils.expression(emptyArray)

        assertEquals(1, result.size)
        assertEquals("0", result[0].value)
    }

    @Test
    fun `expression returns 234 for string array`(){
        val result = SolverUtils.expression(numberArray)

        assertEquals(1, result.size)
        assertEquals("234", result[0].value)
    }

    @Test
    fun `expression returns binary expression for string array`(){
        val expected = listOf(Input("12"), Input("+"), Input("3.2"))
        val result = SolverUtils.expression(binaryExpressionArray)

        assertEquals(3, result.size)
        for(i in 0..<expected.size) {
            assertEquals(expected[i].type, result[i].type)
            assertEquals(expected[i].value, result[i].value)
        }
    }

    @Test
    fun `expression returns unction call expression for string array`(){
        val result = SolverUtils.expression(functionCallArray)
        val expected = listOf(Input("sin"), Input("2.4"))
        assertEquals(2, result.size)
        for(i in 0..<expected.size){
            assertEquals(expected[i].type, result[i].type)
            assertEquals(expected[i].value, result[i].value)
        }
    }

    @Test
    fun `factorial returns 120 for 5`(){
        assertEquals(120.0f, SolverUtils.factorial(5), 0.0f)
    }

    @Test
    fun `permutation returns 20 for 5P2`() {
        assertEquals(20.0f, SolverUtils.permutation(5f, 2f), 0.0f)
    }

    @Test
    fun `combination returns 10 for 5C2`() {
        assertEquals(10.0f, SolverUtils.combination(5f, 2f), 0.0f)
    }

    @Test
    fun `fromRadians returns 45 for 1 quarter of PI in Degree mode`(){
        assertEquals(45.0f, SolverUtils.fromRadians((Math.PI / 4).toFloat(), DRGMode.Deg), 0.0f)
    }

    @Test
    fun `fromRadians returns 100 for half of PI in Gradient mode` (){
        assertEquals(100.0f, SolverUtils.fromRadians((Math.PI / 2).toFloat(), DRGMode.Grad), 0.0f)
    }

    @Test
    fun `fromRadians returns 10 for 10 in Radian mode`() {
        assertEquals(10.0f, SolverUtils.fromRadians(10.0f, DRGMode.Rad), 0.0f)
    }

    @Test
    fun `toRadians returns 1 quarter of PI for 45 in Degree mode`(){
        assertEquals((Math.PI / 4).toFloat(), SolverUtils.toRadians(45.0f, DRGMode.Deg), 0.0f)
    }

    @Test
    fun `toRadians returns half of PI for 100 in Gradient mode`(){
        assertEquals((Math.PI / 2).toFloat(), SolverUtils.toRadians(100.0f, DRGMode.Grad), 0.0f)
    }

    @Test
    fun `toRadians returns 10 for 10 in Radian mode` (){
        assertEquals(10.0f, SolverUtils.toRadians(10.0f, DRGMode.Rad), 0.0f)
    }
}