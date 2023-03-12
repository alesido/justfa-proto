package com.fusion.shared.presenters.user.session


import com.fusion.shared.domain.repositories.AccountService
import com.fusion.shared.presenters.user.session.UserSessionStage.ESTABLISHED
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

/**
 * Presenter encompassing sign in and automatic session continuation.
 *
 * Presenter here plays role of Interactor, implementing a Use Case,
 * and also this is View Model providing State Management.
 *
 * It is included into the shared part of Kotlin Multiplatform Mobile
 *
 * It is a practice, established in a pair of own projects, that View Model
 * yields state flow where each state is a data structure ready (has complete data set
 * on the state) to be presented in a Jetpack Composable.
 */
class UserSessionPresenter(val accountService: AccountService): KoinComponent {

    //@NativeCoroutineScope
    private val viewModelScope = CoroutineScope(Dispatchers.Default)

    private val _sessionStateFlow = MutableStateFlow(UserSessionState.initial())
    val sessionStateFlow get() = _sessionStateFlow.asStateFlow()

    fun login(loginName: String, password: String) {
        viewModelScope.launch {
            accountService.login(loginName, password).collect {
                _sessionStateFlow.value = UserSessionState(ESTABLISHED, it)
            }
        }
    }
}