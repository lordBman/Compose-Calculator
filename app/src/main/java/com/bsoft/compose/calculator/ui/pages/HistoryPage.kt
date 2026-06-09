package com.bsoft.compose.calculator.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bsoft.compose.calculator.R
import com.bsoft.compose.calculator.ui.components.CustomTopBar
import com.bsoft.compose.calculator.ui.components.DisplayRow
import com.bsoft.compose.calculator.ui.theme.ComposeCalculatorTheme
import com.bsoft.compose.calculator.utils.UIPreview
import com.bsoft.compose.solver.SolverUtils
import com.bsoft.compose.solver.models.History

@Composable
fun HistoryPage(modifier: Modifier = Modifier, histories: List<History> = emptyList(), clear: () -> Unit = {}, back: () -> Unit = {}){
    Scaffold(modifier = modifier.fillMaxSize(),
        topBar = {
            CustomTopBar(title = "History", showBack = true, back = back){
                if(histories.isNotEmpty()){
                    IconButton(onClick = { clear() }){
                        Icon(modifier = Modifier.size(24.dp), imageVector = ImageVector.vectorResource(R.drawable.qlementine_icons__paint_brush_large_16), contentDescription = "wipe history")
                    }
                }
            }
        }
    ){ innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
                if(histories.isNotEmpty()){
                    LazyColumn(modifier =  Modifier.fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        histories.forEach { history ->
                            item {
                                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    Text(SolverUtils.formatDate(history), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.End){
                                        history.data.forEach { datum ->
                                            DisplayRow(inputs = datum.inputs, solution = datum.solution, mini = true)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }else{
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No Calculations found")
                    }
                }
        }
    }
}

@UIPreview
@Composable
fun HistoryPagePreview(){
    ComposeCalculatorTheme {
        HistoryPage()
    }
}