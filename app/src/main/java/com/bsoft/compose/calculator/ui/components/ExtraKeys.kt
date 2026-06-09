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
import com.bsoft.compose.solver.models.HypMode
import com.bsoft.compose.solver.models.InputType
import com.bsoft.compose.solver.models.ModesData

@Composable
fun ExtraKeys(modifier: Modifier = Modifier, modes: ModesData = ModesData(), toggleHyp: ()-> Unit = {}, toggleDRG: ()-> Unit = {}, clicked: (String, InputType) -> Unit){
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp) ) {
            Key(modifier = Modifier.weight(1f),  label = "${Constants.EXPONENTIAL_SYMBOL}", mode = modes.key, onClicked = { label, type -> clicked(label, type) })
            Key(modifier = Modifier.weight(1f), label = "${Constants.SQUARE_ROOT_SIGN}", mode = modes.key, onClicked = { label, type -> clicked(label, type) })
            Key(modifier = Modifier.weight(1f),  label = "${Constants.PI_SYMBOL}", mode = modes.key, onClicked = { label, type -> clicked(label, type) })
            ControlKey(modifier = Modifier.weight(1f),  label = "DRG", onClicked = { toggleDRG() })
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp) ) {
            Key(modifier = Modifier.weight(1f),  label = if(modes.hyp == HypMode.On) "sinh" else "sin", mode = modes.key, onClicked = { label, type -> clicked(label, type) })
            Key(modifier = Modifier.weight(1f),  label = if(modes.hyp == HypMode.On) "cosh" else "cos", mode = modes.key, onClicked = { label, type -> clicked(label, type) })
            Key(modifier = Modifier.weight(1f),  label = if(modes.hyp == HypMode.On) "tanh" else "tan", mode = modes.key, onClicked = { label, type -> clicked(label, type) })
            ToggleKey(modifier = Modifier.weight(1f), label = "hyp", active = modes.hyp == HypMode.On) { toggleHyp() }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp) ) {
            Key(modifier = Modifier.weight(1f),  label = if(modes.hyp == HypMode.On) "csch" else "csc", mode = modes.key, onClicked = { label, type -> clicked(label, type) })
            Key(modifier = Modifier.weight(1f),  label = if(modes.hyp == HypMode.On) "sech" else "sec", mode = modes.key, onClicked = { label, type -> clicked(label, type) })
            Key(modifier = Modifier.weight(1f),  label = if(modes.hyp == HypMode.On) "coth" else "cot", mode = modes.key, onClicked = { label, type -> clicked(label, type) })
            Key(modifier = Modifier.weight(1f),  label = "log", mode = modes.key, onClicked = { label, type -> clicked(label, type) })
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp) ) {
            Key(modifier = Modifier.weight(1f),  label = "n${Constants.FACTORIAL_SYMBOL}", value = "${Constants.FACTORIAL_SYMBOL}", onClicked = { label, type -> clicked(label, type) })
            Key(modifier = Modifier.weight(1f),  label = "n${Constants.PERMUTATION}r", value = "${Constants.PERMUTATION}", onClicked = { label, type -> clicked(label, type) })
            Key(modifier = Modifier.weight(1f),  label = "n${Constants.COMBINATION}r", value = "${Constants.COMBINATION}", onClicked = { label, type -> clicked(label, type) })
            Key(modifier = Modifier.weight(1f),  label = "ln", onClicked = { label, type -> clicked(label, type) })
        }
    }
}


@UIPreview
@Composable
private fun ExtraKeysPreview(){
    ComposeCalculatorTheme {
        ExtraKeys{ label, type ->
            Log.d("ExtraKeysPreview", "clicked: $label of type(${type})")
        }
    }
}