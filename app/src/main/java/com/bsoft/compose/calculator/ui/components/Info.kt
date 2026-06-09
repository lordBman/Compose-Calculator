package com.bsoft.compose.calculator.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bsoft.compose.calculator.ui.theme.ComposeCalculatorTheme
import com.bsoft.compose.calculator.utils.UIPreview
import java.util.Calendar

@Composable
fun Info(modifier: Modifier = Modifier){
    Column(modifier = modifier){
        Column(modifier.weight(1f).padding(start = 20.dp, end = 20.dp, top = 80.dp), verticalArrangement = Arrangement.spacedBy(10.dp)){
            Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(4.dp)){
                Text("Compose Calculator", fontSize = 24.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Text(text = "v1.0.0", fontSize = 12.sp, fontWeight = FontWeight.ExtraLight)
            }
            Text(text = "Compose Calculator is solely developed and maintained by Nobel owned by Bsoft Limited", fontSize = 18.sp, fontWeight = FontWeight.ExtraLight)
            Text(text = "Email: Bsoftlimited@gmail.com", fontSize = 16.sp)
        }
        Text("\u24B8 ${Calendar.getInstance().get(Calendar.YEAR)} Bsoft Limited. All rights reserved", modifier = Modifier.align(Alignment.CenterHorizontally), fontSize = 12.sp, fontWeight = FontWeight.W200, letterSpacing = 1.4.sp)
    }
    // Sheet content
    /*Button(onClick = {
        scope.launch { sheetState.hide() }.invokeOnCompletion {
            if (!sheetState.isVisible) {
                showBottomSheet = false
            }
        }
    }) {
        Text("Hide bottom sheet")
    }*/
}

@UIPreview
@Composable
private fun InfoPreview(){
    ComposeCalculatorTheme {
        Surface{
            Info()
        }
    }
}