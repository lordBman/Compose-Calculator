package com.bsoft.compose.calculator.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.bsoft.compose.calculator.routes.Route
import com.bsoft.compose.calculator.stores.Settings
import com.bsoft.compose.calculator.stores.SettingsStore
import com.bsoft.compose.calculator.stores.ThemeMode
import com.bsoft.compose.calculator.ui.pages.HistoryPage
import com.bsoft.compose.calculator.ui.pages.Home
import com.bsoft.compose.calculator.ui.theme.ComposeCalculatorTheme
import com.bsoft.compose.calculator.utils.UIPreview
import com.bsoft.compose.solver.models.SolverData
import com.bsoft.compose.solver.viewmodels.SolverViewModel

@Composable
fun Main(modifier: Modifier = Modifier, viewModel: SolverViewModel = viewModel()){
    val rootBackStack = rememberNavBackStack(Route.Home)

    val coroutineScope = rememberCoroutineScope()

    val solverData: SolverData by viewModel.state.collectAsState()
    val modesData  by viewModel.modes.collectAsState()
    val histories by viewModel.histories.collectAsState(initial = emptyList(), coroutineScope.coroutineContext)

    NavDisplay(
        backStack = rootBackStack,
        onBack = { rootBackStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator{ false }
        ),
        entryProvider = entryProvider {
            entry<Route.Home> {
                Home(modifier = modifier,
                    histories = histories,
                    solverData = solverData,
                    modes = modesData,
                    toggleHyp = { viewModel.toggleHyp() },
                    toggleDRG = { viewModel.toggleDRG() },
                    toggleKeyMode = { viewModel.toggleKeyMode() },
                    clear = { viewModel.clear() },
                    solve = { viewModel.solve() },
                    chosen = { viewModel.chosen(it) },
                    delete = { viewModel.delete() },
                    toHistory = { rootBackStack.add(Route.History) },
                    clicked = { label, type -> viewModel.clicked(value = label, type = type) }
                )
            }
            entry<Route.History>{
                HistoryPage(
                    modifier = modifier,
                    histories = histories,
                    clear = { viewModel.clearHistory() },
                    back = { rootBackStack.removeLastOrNull() },
                )
            }
        }
    )
}

@UIPreview
@Composable
private fun MainPreview(){
    ComposeCalculatorTheme {
        Main()
    }
}