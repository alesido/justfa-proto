/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fusion.android.data

import com.fusion.android.R
import com.fusion.android.conversation.DestinationScreen
import com.fusion.android.conversation.WsConnectionState
import com.fusion.android.conversation.WsConversationState
import com.fusion.android.conversation.WsMessage

private val initialMessages = listOf(
    WsMessage(
        "me",
        "Check it out!",
        1671482854000,
         "20:48"
    ),
    WsMessage(
        "me",
        "Thank you!",
        1671482854000,
        "20:48"
    ),
    WsMessage(
        "Taylor Brooks",
        "You can use all the same stuff",
        1671482854000,
        "20:48"
    ),
    WsMessage(
        "Taylor Brooks",
        "@aliconors Take a look at the `Flow.collectAsStateWithLifecycle()` APIs",
        1671482854000,
        "20:48"
    ),
    WsMessage(
        "John Glenn",
        "Compose newbie as well, have you looked at the JetNews sample? Most blog posts end up " +
            "out of date pretty fast but this sample is always up to date and deals with async " +
            "data loading (it's faked but the same idea applies) \uD83D\uDC49" +
            "https://github.com/android/compose-samples/tree/master/JetNews",
        1671482834000,
        "20:43"
    ),
    WsMessage(
        "me",
        "Compose newbie: I’ve scourged the internet for tutorials about async data loading " +
            "but haven’t found any good ones. What’s the recommended way to load async " +
            "data and emit composable widgets?",
        1671482824000,
        "20:41"
    )
)

val exampleUiState = WsConversationState(
    connection = WsConnectionState.initial(),
    channelName = "#composers",
    channelMembers = 42,
    initialMessages = initialMessages,
    screen = DestinationScreen.CONVERSATION_SETUP_SCREEN
)

