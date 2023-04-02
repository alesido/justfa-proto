package com.fusion.android.conversation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
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
import com.fusion.android.components.JustFaPageTop
import com.fusion.android.framework.findActivity
import com.fusion.android.theme.Capitana

@Composable
fun ProgressScreen(text: String?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(32.dp))
        LinearProgressIndicator()
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = text?: "",
            style = TextStyle(
                fontSize = 16.sp, fontFamily = Capitana, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(40.dp, 40.dp, 40.dp, 0.dp)
        )
    }

    JustFaPageTop()
}

@Composable
fun ConversationClosedScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Conversation session ended.",
            style = TextStyle(
                fontSize = 16.sp, fontFamily = Capitana, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(40.dp, 40.dp, 40.dp, 0.dp)
        )
    }

    JustFaPageTop()
}