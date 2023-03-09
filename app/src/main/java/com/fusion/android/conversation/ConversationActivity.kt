package com.fusion.android.conversation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.fusion.android.data.exampleUiState
import com.fusion.android.theme.JustFaStarterTheme

class ConversationActivity : ComponentActivity() {

    private val viewModel by viewModels<ConversationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Turn off the decor fitting system windows to be able to handle insets, including IME animations
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        setContent {
            CompositionLocalProvider(
                LocalBackPressedDispatcher provides
                        this@ConversationActivity.onBackPressedDispatcher) {

                JustFaStarterTheme {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background,

                    ) {
                        //setSystemBarsColor(color = Color.White) -- FIXME correct system bar color for light/dark theme

                        val wsConversationState by viewModel.conversationFlow.collectAsState()
                        when(wsConversationState.screen) {

                            DestinationScreen.CONVERSATION_SETUP_SCREEN -> {
                                ConversationSetupPage(onSubmit = { ip, port ->
                                    viewModel.onServerAddressProvided(ip, port)
                                })
                            }

                            DestinationScreen.PROGRESS_SCREEN -> {
                                ProgressScreen(wsConversationState.connection.text)
                            }

                            DestinationScreen.CONVERSATION_SCREEN -> {
                                ConversationPage(
                                    state = wsConversationState,
                                    onMessageSubmitted = viewModel::omMessageSubmitted,
                                    modifier = Modifier.windowInsetsPadding(
                                        WindowInsets.navigationBars
                                            .only(WindowInsetsSides.Horizontal
                                                    + WindowInsetsSides.Top)
                                    )
                                )
                            }

                            DestinationScreen.FAILURE_SCREEN -> {
                                FailureScreen(
                                    wsConversationState.connection.text,
                                    configure = { viewModel.configure() },
                                    retry = { viewModel.retry() })
                            }

                            DestinationScreen.CLOSED_SCREEN -> {
                                ConversationClosedScreen()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.closeConversation()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.shutdown()
    }

    companion object {
        fun start(activity: Activity) {
            activity.startActivity(
                Intent(activity, ConversationActivity::class.java)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JustFaStarterTheme {
        ConversationPage(
            state = exampleUiState,
        )
    }
}