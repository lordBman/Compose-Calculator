package com.bsoft.compose.calculator.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bsoft.compose.calculator.ui.theme.ComposeCalculatorTheme
import com.bsoft.compose.calculator.utils.UIPreview

@Composable
fun ToggleKey(modifier: Modifier = Modifier, label: String, active: Boolean = false,  onClicked: ()-> Unit){
    val containerColor = if (active) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSecondary
    val contentColor = if(active) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.secondary

    Button(modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor, contentColor = contentColor),
        elevation =  ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.secondaryContainer),
        onClick = onClicked) {
        Text(label, fontWeight = FontWeight.Normal, fontSize = 18.sp)
    }
}

@UIPreview
@Composable
fun ToggleKeyPreview(){
    ComposeCalculatorTheme{
        Surface {
            ToggleKey(label = "DRG") {

            }
        }
    }
}