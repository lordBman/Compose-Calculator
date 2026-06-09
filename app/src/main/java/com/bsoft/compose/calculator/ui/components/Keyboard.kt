package com.bsoft.compose.calculator.ui.components

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bsoft.compose.calculator.ui.theme.ComposeCalculatorTheme
import com.bsoft.compose.calculator.utils.UIPreview
import com.bsoft.compose.calculator.R.drawable.f7__delete_left
import com.bsoft.compose.solver.models.InputType
import com.bsoft.compose.solver.models.KeyMode
import com.bsoft.compose.solver.models.ModesData
import com.bsoft.compose.solver.models.SolverData
import com.bsoft.compose.solver.viewmodels.SolverViewModel

@Composable
fun isTablet(): Boolean {
    val configuration = LocalConfiguration.current
    // Common threshold: tablets usually have the smallest width of 600dp or more
    return configuration.smallestScreenWidthDp > 700
}

@Composable
fun isLarge(): Boolean {
    val configuration = LocalConfiguration.current
    // Common threshold: tablets usually have the smallest width of 600dp or more
    return isTablet() && configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

@Composable
fun KeyBoard(
    modifier: Modifier = Modifier, state: SolverData = SolverData(), modes: ModesData = ModesData(),
    toggleDRG: ()-> Unit = {}, toggleKeyMode: ()-> Unit = {}, toggleHyp: ()-> Unit = {},
    solve: ()-> Unit = {}, delete: ()-> Unit = {}, clear: ()-> Unit = {},
    clicked: (String, InputType) -> Unit
){
    val pagerState = rememberPagerState(pageCount = { 2 })

    fun toggleLabel(): String{
        val power = "\u02E3"
        val base = "\u2093"
        return when(modes.key){
            KeyMode.Base -> "n${power}"
            KeyMode.Power -> "n"
            KeyMode.Normal -> {
                if(state.current.inputs.isNotEmpty() && state.current.inputs.last().value == "log"){
                    "n${base}"
                }else{
                    "n${power}"
                }
            }
        }
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp) ) {
            Row(modifier = Modifier.weight(if (isLarge()) 2f else 1f), horizontalArrangement = Arrangement.spacedBy(10.dp) ) {
                Key(modifier = Modifier.weight(1f), label = "ans", mode = modes.key, background = MaterialTheme.colorScheme.tertiary, onClicked = clicked)
                ToggleKey(modifier = Modifier.weight(1f), label = toggleLabel(), active = modes.key != KeyMode.Normal, onClicked = { toggleKeyMode() })
            }
            if (isTablet()){
                Surface(modifier = Modifier.weight(if (isLarge()) 5f else 2f)) { }
            }
            Row(modifier = Modifier.weight(if (isLarge()) 2f else 1f), horizontalArrangement = Arrangement.spacedBy(10.dp) ) {
                ControlKey(modifier = Modifier.weight(1f), label = "C", onClicked = { clear() })
                ControlKey(modifier = Modifier.weight(1f), icon = f7__delete_left, onClicked = { delete() })
            }
        }
        if(isTablet()){
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                MainKeys(modifier = Modifier.weight(4f), modes = modes, clicked = clicked)
                ExtraKeys(modifier = Modifier.weight(4f), modes = modes, toggleDRG = toggleDRG, toggleHyp = toggleHyp, clicked = clicked)
                if(isLarge()){
                    Column(modifier = modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(10.dp)){
                        Key(modifier = modifier.fillMaxWidth(), label = "(", onClicked = { label, type -> clicked(label, type) })
                        Key(modifier = modifier.fillMaxWidth(), label = ")", onClicked = { label, type -> clicked(label, type) })
                        ControlKey(modifier = modifier.fillMaxWidth().height(90.dp), label = "=", onClicked = { solve() })
                    }
                }
            }
        }else{
            HorizontalPager(pagerState, modifier = Modifier.fillMaxWidth()) {
                if(it == 0){
                    MainKeys(modes = modes, clicked = clicked)
                }else{
                    ExtraKeys(modes = modes, toggleDRG = toggleDRG, toggleHyp = toggleHyp, clicked = clicked)
                }
            }
        }
        if(!isLarge()){
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp) ) {
                Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(10.dp) ) {
                    Key(modifier = Modifier.weight(1f),  label = "(", onClicked = { label, type -> clicked(label, type) })
                    Key(modifier = Modifier.weight(1f),  label = ")", onClicked = { label, type -> clicked(label, type) })
                }
                ControlKey(modifier = Modifier.weight(1f), label = "=", onClicked = { solve() })
            }
        }
    }
}


@UIPreview
@Composable
private fun KeyBoardPreview(){
    ComposeCalculatorTheme {
       KeyBoard{ label, type ->
           Log.d("KeyBoardPreview", "clicked: $label of type(${type})")
       }
    }
}