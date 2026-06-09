package com.bsoft.compose.calculator.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.bsoft.compose.calculator.ui.theme.ComposeCalculatorTheme
import com.bsoft.compose.calculator.utils.UIPreview
import com.bsoft.compose.solver.models.Constants
import com.bsoft.compose.solver.models.InputType
import com.bsoft.compose.solver.models.ModesData

@Composable
fun MainKeys(modifier: Modifier = Modifier, modes: ModesData = ModesData(), clicked: (String, InputType) -> Unit){
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp) ) {
            Key(modifier = Modifier.weight(1f), label = "7", mode = modes.key, onClicked = { label, type -> clicked(label, type) })
            Key(modifier = Modifier.weight(1f),  label = "8", mode = modes.key, onClicked = { label, type -> clicked(label, type) })
            Key(modifier = Modifier.weight(1f),  label = "9", mode = modes.key, onClicked = { label, type -> clicked(label, type) })
            Key(modifier = Modifier.weight(1f),  label = "${Constants.PLUS_SIGN}", mode = modes.key, onClicked = { label, type -> clicked(label, type) })
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp) ) {
            Key(modifier = Modifier.weight(1f),  label = "4", mode = modes.key, onClicked = { label, type -> clicked(label, type) })
            Key(modifier = Modifier.weight(1f),  label = "5", mode = modes.key, onClicked = { label, type -> clicked(label, type) })
            Key(modifier = Modifier.weight(1f),  label = "6", mode = modes.key, onClicked = { label, type -> clicked(label, type) })
            Key(modifier = Modifier.weight(1f),  label = "${Constants.MINUS_SIGN}", mode = modes.key, onClicked = { label, type -> clicked(label, type) })
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp) ) {
            Key(modifier = Modifier.weight(1f),  label = "1", mode = modes.key, onClicked = { label, type -> clicked(label, type) })
            Key(modifier = Modifier.weight(1f),  label = "2", mode = modes.key, onClicked = { label, type -> clicked(label, type) })
            Key(modifier = Modifier.weight(1f),  label = "3", mode = modes.key, onClicked = { label, type -> clicked(label, type) })
            Key(modifier = Modifier.weight(1f),  label = "${Constants.DIVISION_SIGN}", mode = modes.key, onClicked = { label, type -> clicked(label, type) })
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp) ) {
            Key(modifier = Modifier.weight(1f),  label = ".", mode = modes.key, onClicked = { label, type -> clicked(label, type) })
            Key(modifier = Modifier.weight(1f),  label = "0", mode = modes.key, onClicked = { label, type -> clicked(label, type) })
            Key(modifier = Modifier.weight(1f),  label = "%", mode = modes.key, onClicked = { label, type -> clicked(label, type) })
            Key(modifier = Modifier.weight(1f),  label = "${Constants.MULTIPLICATION_SIGN}", mode = modes.key, onClicked = { label, type -> clicked(label, type) })
        }
    }
}

@UIPreview
@Composable
private fun MainKeysPreview(){
    ComposeCalculatorTheme {
        MainKeys{ label, type ->
            Log.d("MainKeysPreview", "clicked: $label of type(${type})")
        }
    }
}