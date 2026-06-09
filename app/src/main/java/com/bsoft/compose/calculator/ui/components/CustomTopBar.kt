package com.bsoft.compose.calculator.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bsoft.compose.calculator.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(state: TopAppBarState = rememberTopAppBarState(), title: String, @DrawableRes icon: Int? = null, showBack: Boolean = false, back: () -> Unit = {}, actions: @Composable RowScope.() -> Unit = {}){
    val colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
    TopAppBar(
        scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state),
        colors = colors,
        title = { Text(title) },
        navigationIcon = {
                if(showBack){
                    IconButton(onClick = back) {
                        Icon(imageVector = ImageVector.vectorResource(R.drawable.ic__round_arrow_back_ios), contentDescription = "")
                    }
                }
                if(icon != null){
                    Icon(modifier = Modifier.size(36.dp), imageVector = ImageVector.vectorResource(icon),
                        contentDescription = "")
                }
        },
        actions = actions
    )
}