package com.bsoft.compose.calculator.ui.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bsoft.compose.calculator.ui.theme.ComposeCalculatorTheme
import com.bsoft.compose.calculator.utils.UIPreview
import com.bsoft.compose.solver.models.Constants
import com.bsoft.compose.solver.models.Input
import com.bsoft.compose.solver.models.InputType

@Composable
fun DisplayRow(modifier: Modifier = Modifier, inputs: List<Input> = emptyList(), solution: String? = null, mini: Boolean = false){
    val scrollState = rememberScrollState()

    LaunchedEffect(inputs){
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    val fontWeight = if (mini) FontWeight.Light else FontWeight.Normal
    Row(modifier = modifier.fillMaxWidth().height(if (mini) 26.dp else 36.dp).horizontalScroll(state = scrollState, enabled = true), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
        inputs.forEach {
            when(it.type){
                InputType.Base -> {
                    val fontSize = if (mini) 9.sp else 14.sp
                    Text(it.value, modifier = Modifier.align(Alignment.Bottom), fontSize = fontSize, fontWeight = fontWeight)
                }
                InputType.Power -> {
                    val fontSize = if (mini) 9.sp else 14.sp
                    Text(it.value, modifier = Modifier.align(Alignment.Top), fontSize = fontSize, fontWeight = fontWeight)
                }
                else -> {
                    val fontSize = if (mini) 16.sp else 24.sp
                    Text(it.value, modifier = Modifier.align(Alignment.CenterVertically).padding(start = 4.dp), fontSize = fontSize, fontWeight = fontWeight)
                }
            }
        }
        if(solution != null){
            Text(" = $solution", fontSize = if (mini) 16.sp else 24.sp, fontWeight = fontWeight)
        }
    }
}

@UIPreview
@Composable
fun DisplayRowPreview(){
    val inputs = listOf(
        Input("2"), Input("3", type = InputType.Power), Input("("), Input("5"), Input("${Constants.MULTIPLICATION_SIGN}"),
        Input("2"), Input(")"), Input("${Constants.MINUS_SIGN}"), Input("4"), Input("${Constants.FACTORIAL_SYMBOL}")
    )
    ComposeCalculatorTheme{
        Surface{
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)){
                DisplayRow(inputs = inputs, solution = "8", mini = true)
                DisplayRow(inputs = listOf(Input(value = "22222212344545665612223233347778090")))
                DisplayRow(inputs = listOf(
                    Input("log"), Input("2", type = InputType.Base), Input("8")
                ))
            }
        }
    }
}