package com.fusion.shared.presenters.user.session

import com.fusion.shared.domain.models.Account

enum class UserSessionStage {
    INITIAL, AUTHORIZATION, ESTABLISHED, FAILURE
}

data class UserSessionState(
    val stage: UserSessionStage = UserSessionStage.INITIAL,
    val account: Account? = null,
    val error: String? = null
) {
    companion object {
        fun initial() = UserSessionState()
    }
}
