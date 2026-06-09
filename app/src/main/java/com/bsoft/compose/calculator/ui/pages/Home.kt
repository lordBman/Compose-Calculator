package com.bsoft.compose.calculator.ui.pages

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bsoft.compose.calculator.R
import com.bsoft.compose.calculator.ui.components.CustomTopBar
import com.bsoft.compose.calculator.ui.components.Info
import com.bsoft.compose.calculator.ui.components.KeyBoard
import com.bsoft.compose.calculator.ui.components.Screen
import com.bsoft.compose.calculator.ui.theme.ComposeCalculatorTheme
import com.bsoft.compose.calculator.utils.UIPreview
import com.bsoft.compose.solver.models.History
import com.bsoft.compose.solver.models.InputType
import com.bsoft.compose.solver.models.ModesData
import com.bsoft.compose.solver.models.SolverData
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    modifier: Modifier = Modifier, histories: List<History> = emptyList(), solverData: SolverData = SolverData(), toHistory: () -> Unit = {},
    modes: ModesData = ModesData(), toggleDRG: ()-> Unit = {}, toggleKeyMode: ()-> Unit = {}, toggleHyp: ()-> Unit = {},
    solve: ()-> Unit = {}, delete: ()-> Unit = {}, clear: ()-> Unit = {}, chosen: (Int) -> Unit = {}, clicked: (String, InputType) -> Unit
){
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    Scaffold(modifier = modifier.fillMaxSize(),
        topBar = {
            CustomTopBar(title = "Compose Calculator", icon = R.drawable.arcticons__calculator){
                IconButton(onClick = toHistory){
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.hugeicons__transaction_history), contentDescription = "history")
                }
                IconButton(onClick = { showBottomSheet = true }) {
                    Icon(imageVector = ImageVector.vectorResource(R.drawable.heroicons__information_circle), contentDescription = "settings")
                }
            }
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            Column(modifier = Modifier.padding(horizontal = 10.dp,), horizontalAlignment = Alignment.CenterHorizontally){
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(modes.drg.title, fontSize = 12.sp, fontWeight = FontWeight.ExtraLight, color = MaterialTheme.colorScheme.primary)
                    Text(modes.key.value, fontSize = 12.sp, fontWeight = FontWeight.ExtraLight, color = MaterialTheme.colorScheme.primary)
                    Text(modes.hyp.title, fontSize = 12.sp, fontWeight = FontWeight.ExtraLight, color = MaterialTheme.colorScheme.primary)
                }
                Screen(histories = histories, solverData = solverData, chosen = chosen)
                KeyBoard(state = solverData, modes = modes, toggleHyp = toggleHyp, toggleDRG = toggleDRG, toggleKeyMode = toggleKeyMode, solve = solve, delete = delete, clear = clear, clicked = clicked)
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(onDismissRequest = { showBottomSheet = false }, sheetState = sheetState) {
                Info()
            }
        }
    }
}

@UIPreview
@Composable
private fun HomePreview(){
    ComposeCalculatorTheme {
        Home(){ label, type ->
            Log.d("HomePreview", "clicked: $label of type(${type})")
        }
    }
}