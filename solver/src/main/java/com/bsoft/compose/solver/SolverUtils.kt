package com.bsoft.compose.solver

import com.bsoft.compose.solver.components.Lexer
import com.bsoft.compose.solver.models.DRGMode
import com.bsoft.compose.solver.models.Datum
import com.bsoft.compose.solver.models.History
import com.bsoft.compose.solver.models.Input
import java.util.Calendar
import kotlin.math.PI

object SolverUtils {
    fun expression(inputs: List<Input>): List<Input>{
        if(inputs.isNotEmpty()) {
            val init = mutableListOf<Input>()
            val lexer = Lexer(inputs)
            while (lexer.hasNext){
                init.add(lexer.next().input)
            }
            return init
        }
        return listOf(Input("0"))
    }

    fun factorial(input: Int): Float{
        var result = 1f
        for(i in 1..input){
            result *= i
        }
        return result
    }

    fun fromRadians(input: Float, mode: DRGMode): Float{
        return when(mode){
            DRGMode.Rad -> input
            DRGMode.Deg -> input * 180 / PI
            DRGMode.Grad -> input * 200 / PI
        }.toFloat()
    }

    fun toRadians(input: Float, mode: DRGMode): Float{
        return when(mode){
            DRGMode.Rad -> input
            DRGMode.Deg -> input * PI / 180
            DRGMode.Grad -> input * PI / 200
        }.toFloat()
    }

    fun permutation(count: Float, radius: Float): Float{
        return factorial(count.toInt())/ factorial(count.toInt() - radius.toInt())
    }

    fun combination(count: Float, radius: Float): Float{
        return permutation(count, radius)/ factorial(radius.toInt())
    }

    fun isBaseCompatible(label: String): Boolean {
        return label.first().isDigit()
    }

    fun isToday(history: History): Boolean{
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        return !(year > history.year || month > history.month || day > history.day)
    }

    fun formatDate(value: Int): String{
        if(value in 11..20){
            return "${value}th"
        }
        return when(value % 10){
            1 -> "${value}st"
            2 -> "${value}nd"
            3 -> "${value}rd"
            else -> "${value}th"
        }
    }

    fun formatMonth(value: Int): String{
        return when(value){
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            else -> "December"
        }
    }

    fun formatDate(history: History): String{
        val calendar = Calendar.getInstance()
        if(calendar.get(Calendar.YEAR) > history.year || calendar.get(Calendar.MONTH) > history.month){
            return "${formatDate(history.day)} ${formatMonth(history.month)}"
        }

        if(calendar.get(Calendar.DAY_OF_MONTH) - history.day == 0){
            return "Today"
        }

        if(calendar.get(Calendar.DAY_OF_MONTH) - history.day == 1){
            return "Yesterday"
        }
        return formatDate(history.day)
    }

    fun today(histories: List<History>): List<Datum>{
        val init = histories.find{ history -> isToday(history) }

        return init?.data ?: emptyList()
    }
}