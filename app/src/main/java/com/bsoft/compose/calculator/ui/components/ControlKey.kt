package com.bsoft.compose.calculator.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bsoft.compose.calculator.R
import com.bsoft.compose.calculator.ui.theme.ComposeCalculatorTheme
import com.bsoft.compose.calculator.utils.UIPreview

@Composable
fun ControlKey(modifier: Modifier = Modifier, label: String = "", @DrawableRes icon: Int? = null, onClicked: ()-> Unit){
    Button(modifier = modifier.height(40.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
        elevation =  ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp),
        onClick = onClicked) {
        if (icon != null){
            Icon(imageVector = ImageVector.vectorResource(icon), contentDescription = null)
        }else{
            Text(label, fontWeight = FontWeight.Normal, fontSize = 18.sp)
        }
    }
}

@UIPreview
@Composable
private fun ControlKeyPreview(){
    ComposeCalculatorTheme{
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            ControlKey(label = "4"){}
            ControlKey(icon = R.drawable.f7__delete_left){}
        }
    }
}