package com.fusion.shared.presenters.user.session


import com.fusion.shared.domain.repositories.AccountService
import com.fusion.shared.framework.resultOf
import com.fusion.shared.presenters.user.session.UserSessionStage.*
import io.ktor.util.reflect.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import java.nio.channels.UnresolvedAddressException

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

    val sessionStateFlow get() = _sessionStateFlow.asStateFlow()
    private val _sessionStateFlow = MutableStateFlow(UserSessionState.initial())

    private var lastLoginName: String? = null
    private var lastPassword: String? = null

    fun login(loginName: String, password: String) {
        lastLoginName = loginName
        lastPassword = password
        viewModelScope.launch {
            resultOf {
                accountService.login(loginName, password)
            }.onSuccess {
                _sessionStateFlow.value = UserSessionState(ESTABLISHED, it)
            }.onFailure {
                e -> _sessionStateFlow.value = UserSessionState(FAILURE, error = errorMessage(e))
            }
        }
    }

    fun retryLogin() {
        if (lastLoginName != null && lastPassword != null) {
            login(lastLoginName!!, lastPassword!!)
        }
        else {
            _sessionStateFlow.value = UserSessionState(INITIAL)
        }
    }

    private fun errorMessage(e: Throwable) = if (e.message?.isNotEmpty() == true) e.message
    else {
        when (e) {
            is UnresolvedAddressException -> "Please, check your Internet connection and try again"
            else -> e.javaClass.simpleName
        }
    }
}