package com.fusion.shared.presenters.user.session

import com.fusion.shared.domain.models.Account

enum class ConnectionStage {
    INITIAL, CONNECTING, CONNECTED, DISCONNECTED, FAILED
}

enum class UserSessionStage {
    INITIAL, AUTHORIZATION, ESTABLISHED, FAILED
}

data class UserSessionState(
    val stage: UserSessionStage = UserSessionStage.INITIAL,
    val account: Account? = null
) {
    companion object {
        fun initial() = UserSessionState()
    }
}
