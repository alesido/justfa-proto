package com.fusion.android.conversation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fusion.android.components.JustFaPageTop
import com.fusion.android.theme.Capitana

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationSetupPage(onSubmit: (serverIp: String, serverPort: String) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val ipAddress = remember { mutableStateOf(TextFieldValue()) }
        val ipPort = remember { mutableStateOf(TextFieldValue()) }

        val focusManager = LocalFocusManager.current

        // explanation
        Text(
            text = "Please enter IP address and port of the server to open conversation ",
            style = TextStyle(fontSize = 16.sp, fontFamily = Capitana),
            modifier = Modifier.padding(40.dp, 40.dp, 40.dp, 0.dp)
        )

        // input: IP Address
        Spacer(modifier = Modifier.height(40.dp))
        TextField(
            label = { Text(
                text = "Server IP Address", modifier = Modifier.padding(5.dp),
                style = TextStyle(fontSize = 14.sp, fontFamily = Capitana))},
            value = ipAddress.value,
            onValueChange = {
                // TODO Validate IP address
                ipAddress.value = it },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Number))

        // input: IP Port
        Spacer(modifier = Modifier.height(40.dp))
        TextField(
            label = { Text(
                text = "Server Port", modifier = Modifier.padding(5.dp),
                style = TextStyle(fontSize = 14.sp, fontFamily = Capitana))},
            value = ipPort.value,
            onValueChange = {
                // TODO Validate IP port
                ipPort.value = it
            },
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number)
        )

        // button: submit
        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.padding(40.dp, 40.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    // validate IP address and port
                    onSubmit(ipAddress.value.text, ipPort.value.text)
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Submit")
            }
        }
    }

    JustFaPageTop()
}