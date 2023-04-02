package com.fusion.android.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fusion.android.framework.findActivity
import com.fusion.android.theme.Capitana

@Composable
fun JustFaFailureScreen(text: String?,
                        configure: (() -> Unit)? = null,
                        retry: (() -> Unit)? = null) {
    val activity = LocalContext.current.findActivity()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = text?: "",
            color = Color.Red,
            style = TextStyle(fontSize = 16.sp, fontFamily = Capitana, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(40.dp, 40.dp, 40.dp, 0.dp)
        )
        configure?.let {
            Box(modifier = Modifier.padding(40.dp, 40.dp, 40.dp, 0.dp)) {
                Button(
                    onClick = { configure() },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().height(50.dp) ) {
                    Text(text = "Change Configuration")
                }
            }
        }
        retry?.let {
            Box(modifier = Modifier.padding(40.dp, 20.dp, 40.dp, 0.dp)) {
                Button(
                    onClick = { retry() },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth().height(50.dp) ) {
                    Text(text = "Retry")
                }
            }
        }
        Box(modifier = Modifier.padding(40.dp, 20.dp, 40.dp, 0.dp)) {
            Button(
                onClick = { activity?.finish() },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth().height(50.dp) ) {
                Text(text = "Cancel")
            }
        }
    }

    JustFaPageTop()
}
