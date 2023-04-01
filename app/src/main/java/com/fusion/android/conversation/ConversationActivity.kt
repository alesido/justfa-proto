package com.fusion.android.conversation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.fusion.android.theme.JustFaStarterTheme
import com.fusion.shared.presenters.conversation.text.TextConversationPresenter
import com.fusion.shared.presenters.conversation.text.TextConversationStage
import org.koin.androidx.compose.get

class ConversationActivity : ComponentActivity() {

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

                        val viewModel : TextConversationPresenter = get()

                        val conversationState by viewModel.conversationFlow.collectAsState()
                        when(conversationState.stage) {

                            TextConversationStage.INITIAL, TextConversationStage.STARTING -> {
                                ProgressScreen("Starting up ...")
                            }

                            TextConversationStage.READY -> {
                                ConversationPage(
                                    state = conversationState,
                                    onMessageSubmitted = viewModel::onMessageSubmitted,
                                    modifier = Modifier.windowInsetsPadding(
                                        WindowInsets.navigationBars
                                            .only(WindowInsetsSides.Horizontal
                                                    + WindowInsetsSides.Top)
                                    )
                                )
                            }

                            TextConversationStage.FAILURE -> {
                                FailureScreen(
                                    conversationState.error, // TODO Add failure screen text
                                    configure = { },
                                    retry = { })
                            }

                            TextConversationStage.CLOSED -> {
                                ConversationClosedScreen()
                            }
                        }
                    }
                }
            }
        }
    }

/**
 * TODO Close conversation session on activity stop shutdown on destroy.
 *
    override fun onStop() {
        super.onStop()
        viewModel.closeConversation()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.shutdown()
    }
*/
    companion object {
        fun start(activity: Activity) {
            activity.startActivity(
                Intent(activity, ConversationActivity::class.java)
            )
        }
    }
}
/** TODO Add conversation screen preview
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JustFaStarterTheme {
        ConversationPage(
            state = exampleUiState,
        )
    }
}
*/