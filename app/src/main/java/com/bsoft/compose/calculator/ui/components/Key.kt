package com.bsoft.compose.calculator.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bsoft.compose.calculator.ui.theme.ComposeCalculatorTheme
import com.bsoft.compose.calculator.utils.UIPreview
import com.bsoft.compose.solver.SolverUtils
import com.bsoft.compose.solver.models.InputType
import com.bsoft.compose.solver.models.KeyMode

@Composable
fun Key(modifier: Modifier = Modifier, label: String, value: String? = null, mode: KeyMode = KeyMode.Normal, background: Color = MaterialTheme.colorScheme.primary, onClicked: (label: String, type: InputType)-> Unit){
    val init: String = value ?: label
    Button(modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(8.dp),
        elevation =  ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
        colors = ButtonDefaults.buttonColors(containerColor = background),
        onClick = {
            when (mode) {
                KeyMode.Power -> {
                    onClicked(init, InputType.Power)
                }
                KeyMode.Base if SolverUtils.isBaseCompatible(label) -> {
                    onClicked(init, InputType.Base)
                }
                else -> {
                    onClicked(init, InputType.Number)
                }
            }
        }) {
        when(mode){
            KeyMode.Power ->{
                Row(modifier = Modifier.height(24.dp)){
                    Text("n", modifier = Modifier.align(Alignment.CenterVertically), fontWeight = FontWeight.Normal, fontSize = 18.sp)
                    Text(label, modifier = Modifier.align(Alignment.Top), fontWeight = FontWeight.Normal, fontSize = 10.sp)
                }
            }
            KeyMode.Base if SolverUtils.isBaseCompatible(label) ->{
                Row(modifier = Modifier.height(24.dp)){
                    Text("n", modifier = Modifier.align(Alignment.CenterVertically), fontWeight = FontWeight.Normal, fontSize = 18.sp)
                    Text(label, modifier = Modifier.align(Alignment.Bottom), fontWeight = FontWeight.Normal, lineHeight = 1.sp, fontSize = 10.sp)
                }
            }
            else -> {
                Text(label, fontWeight = FontWeight.Normal, fontSize = 18.sp)
            }
        }
    }
}

@UIPreview
@Composable
private fun KeyPreview(){
    ComposeCalculatorTheme{
        Key(
            label = "4",
            mode = KeyMode.Base,
            onClicked = { _, _ ->

            }
        )
    }
}