package com.bsoft.compose.calculator.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bsoft.compose.calculator.ui.theme.ComposeCalculatorTheme
import com.bsoft.compose.calculator.utils.UIPreview
import com.bsoft.compose.solver.SolverUtils
import com.bsoft.compose.solver.models.History
import com.bsoft.compose.solver.models.SolverData
import com.bsoft.compose.solver.viewmodels.SolverViewModel

@Composable
fun ColumnScope.Screen(modifier: Modifier = Modifier, histories: List<History> = emptyList(), solverData: SolverData = SolverData(), chosen: (Int) -> Unit = {}){
    val state: LazyListState = rememberLazyListState()
    val data = SolverUtils.today(histories = histories)

    LaunchedEffect(data) {
        if(data.isNotEmpty()){
            state.animateScrollToItem(data.lastIndex)
        }
    }

    Surface(modifier = modifier.weight(1f).fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)){
            LazyColumn(modifier = Modifier.fillMaxWidth().weight(1f), state = state, horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.Bottom) {
                data.forEach {
                   item {
                       DisplayRow(modifier = Modifier.clickable(enabled = true, onClick = { chosen(data.indexOf(it)) }),
                           inputs = SolverUtils.expression(it.inputs), solution = it.solution, mini = true)
                   }
                }
            }
            if(solverData.current.solution != null){
                Column(horizontalAlignment = Alignment.End){
                    DisplayRow(inputs = SolverUtils.expression(solverData.current.inputs), mini = true)
                    Text("${solverData.current.solution}", color = MaterialTheme.colorScheme.onSurface, fontSize = 36.sp, fontWeight = FontWeight.ExtraBold)
                }
            }else{
                DisplayRow(inputs = SolverUtils.expression(solverData.current.inputs))
            }
        }
    }
}

@UIPreview
@Composable
private fun ScreenPreview(){
    ComposeCalculatorTheme {
        Column{
            Screen()
        }
    }
}