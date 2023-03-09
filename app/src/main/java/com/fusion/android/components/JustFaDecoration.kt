package com.fusion.android.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.fusion.android.R

@Composable
fun JustFaPageTop() {
    Box(modifier = Modifier
        .fillMaxSize()
        .systemBarsPadding()
        .padding(58.dp, 32.dp, 0.dp, 0.dp)) {
        Icon(
            painter = painterResource(id = if (isSystemInDarkTheme())
                R.drawable.app_logo_long_on_dark else R.drawable.app_logo_long_on_light),
            tint = Color.Unspecified,
            contentDescription = null,
        )
    }
}