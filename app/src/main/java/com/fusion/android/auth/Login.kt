package com.fusion.android.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fusion.android.components.JustFaPageTop
import com.fusion.android.conversation.ConversationActivity
import com.fusion.android.framework.FunctionalityNotAvailablePopup
import com.fusion.android.framework.findActivity
import com.fusion.android.theme.Capitana
import com.fusion.android.theme.JBlue
import com.fusion.android.theme.JustFaStarterTheme
import com.fusion.shared.presenters.user.session.UserSessionPresenter
import com.fusion.shared.presenters.user.session.UserSessionStage.*
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.get

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(
    userSessionViewModel: UserSessionPresenter = get()  // koin-injected
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = userSessionViewModel) {
        userSessionViewModel.sessionStateFlow.collectLatest {
            when (it.stage) {
                INITIAL -> {}
                AUTHORIZATION -> TODO()
                ESTABLISHED -> {
                    context.findActivity()?.let { a ->
                        ConversationActivity.start(a)
                        a.finish()
                    }
                }
                FAILED -> TODO()
            }
        }
    }

    var functionalityNotAvailablePopupShown by remember { mutableStateOf(false) }
    if (functionalityNotAvailablePopupShown) {
        FunctionalityNotAvailablePopup { functionalityNotAvailablePopupShown = false }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val username = remember { mutableStateOf(TextFieldValue()) }
        val password = remember { mutableStateOf(TextFieldValue()) }
        val focusManager = LocalFocusManager.current

        Text(text = "Please sign in to your JustFA account with your e-mail and password",
            style = TextStyle(fontSize = 16.sp, fontFamily = Capitana),
            modifier = Modifier.padding(40.dp, 40.dp, 40.dp, 0.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))
        TextField(
            label = { Text(text = "E-mail", modifier = Modifier.padding(5.dp), style = TextStyle(
                fontSize = 14.sp, fontFamily = Capitana))},
            value = username.value,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            onValueChange = { username.value = it })

        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            label = { Text(text = "Password", modifier = Modifier.padding(5.dp), style = TextStyle(
                fontSize = 14.sp, fontFamily = Capitana))},
            value = password.value,
            visualTransformation = PasswordVisualTransformation(),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password),
            onValueChange = { password.value = it },
        )

        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier.padding(40.dp, 40.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    context.findActivity()?.let {
                        userSessionViewModel.login(
                            username.value.text, password.value.text)
                    }
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Login")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        ClickableText(
            text = AnnotatedString("Forgot password?"),
            onClick = { functionalityNotAvailablePopupShown = true },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = Capitana
            )
        )
    }

    JustFaPageTop()
    PageFooter()
}

@Composable
fun PageFooter() {

    var functionalityNotAvailablePopupShown by remember { mutableStateOf(false) }
    if (functionalityNotAvailablePopupShown) {
        FunctionalityNotAvailablePopup { functionalityNotAvailablePopupShown = false }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        val signUpTextWithLink = signUpTextWithLink()
        ClickableText(
            text = signUpTextWithLink,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(40.dp),
            style = TextStyle(
                fontFamily = Capitana,
                fontSize = 14.sp,
                color = if (isSystemInDarkTheme()) Color.White else Color.Black),
            onClick = {
                signUpTextWithLink
                    .getStringAnnotations("ACTION", it, it)
                    .firstOrNull()?.let { functionalityNotAvailablePopupShown = true }
            }
        )
    }
}

private fun signUpTextWithLink(): AnnotatedString {
    val source = "Don't have JustFA account? Create"
    val segment = "Create"

    val builder = AnnotatedString.Builder()
    builder.append(source)

    val start = source.indexOf(segment)
    val end = start + segment.length

    val hyperlinkStyle = SpanStyle(color = JBlue, textDecoration = TextDecoration.Underline)

    builder.addStyle(hyperlinkStyle, start, end) // style "my website" to make it look like a link
    builder.addStringAnnotation("ACTION", source, start, end) // attach the link to the span. We can then access it via the TAG_URL

    return builder.toAnnotatedString()
}

@Preview
@Composable
fun LoginPagePreview() {
    JustFaStarterTheme {
        Surface(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
        ) {
            LoginPage()
        }
    }
}
