package com.bsoft.compose.calculator.ui.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bsoft.compose.calculator.stores.Settings
import com.bsoft.compose.calculator.stores.SettingsStore
import com.bsoft.compose.calculator.stores.ThemeMode
import com.bsoft.compose.calculator.ui.components.CustomTopBar
import com.bsoft.compose.calculator.ui.theme.ComposeCalculatorTheme
import com.bsoft.compose.calculator.utils.UIPreview
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
private fun SettingsSurface(modifier: Modifier = Modifier, color: Color? = null, content: @Composable ()-> Unit){
    val containerColor = color ?: MaterialTheme.colorScheme.surfaceContainer
    Surface(modifier = modifier.fillMaxWidth(), color = containerColor, shape = RoundedCornerShape(10.dp)) {
        Box(modifier = Modifier.padding(20.dp)){
            content()
        }
    }
}

@Composable
private fun SettingsTitle(modifier: Modifier = Modifier, title: String){
    Text(title, modifier = modifier, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
}

@Composable
fun Settings(modifier: Modifier = Modifier, settings: Settings = Settings(), updateTheme: (ThemeMode) -> Unit = {}, updateSave: (Boolean) -> Unit = {}, updateDynamicTheme: (Boolean) -> Unit = {}, back: () -> Unit = {}){

    Scaffold(modifier = modifier.fillMaxSize(),
        topBar = {
            CustomTopBar(title = "Settings", showBack = true, back = back)
        }
    ){ innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            LazyColumn(contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)){
                item { SettingsTitle(title = "General") }
                item {
                    SettingsSurface {
                        Column {
                            Text("Theme Mode", color = MaterialTheme.colorScheme.primary, fontSize = 16.sp, fontWeight = FontWeight.W500)
                            Row(verticalAlignment = Alignment.CenterVertically){
                                RadioButton(selected = settings.themeMode == ThemeMode.System, onClick = { updateTheme(ThemeMode.System) })
                                Text("System")
                            }
                            Row(verticalAlignment = Alignment.CenterVertically){
                                RadioButton(selected = settings.themeMode == ThemeMode.Light, onClick = { updateTheme(ThemeMode.Light) })
                                Text("Light")
                            }
                            Row(verticalAlignment = Alignment.CenterVertically){
                                RadioButton(selected = settings.themeMode == ThemeMode.Dark, onClick = { updateTheme(ThemeMode.Dark) })
                                Text("Dark")
                            }
                        }
                    }
                }

                item {
                    SettingsSurface {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Switch(checked = settings.dynamicTheme, onCheckedChange = { updateDynamicTheme(it) })
                            Text("Use Dynamic Theme", fontSize = 20.sp)
                        }
                    }
                }

                item {
                    SettingsSurface {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Switch(checked = settings.save, onCheckedChange = { updateSave(it) })
                            Text("Save history on exit", fontSize = 20.sp)
                        }
                    }
                }

                item { SettingsTitle(title = "About") }
                item {
                    SettingsSurface(color = MaterialTheme.colorScheme.onSecondaryContainer){
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)){
                            Text(text = "Compose Calculator is solely developed and maintained by Nobel owned by Bsoft Limited", fontSize = 18.sp, fontWeight = FontWeight.W300, color = MaterialTheme.colorScheme.onSecondary)
                            Text("\u24B8 ${Calendar.getInstance().get(Calendar.YEAR)} Bsoft Limited. All rights reserved", fontSize = 18.sp, fontWeight = FontWeight.W200, letterSpacing = 1.4.sp, color = MaterialTheme.colorScheme.onSecondary)
                            Text(text = "version: 1.0.0", modifier = Modifier.align(Alignment.End), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondary)
                        }
                    }
                }
            }
        }
    }
}

@UIPreview
@Composable
fun SettingsPreview(){
    ComposeCalculatorTheme {
        Settings()
    }
}