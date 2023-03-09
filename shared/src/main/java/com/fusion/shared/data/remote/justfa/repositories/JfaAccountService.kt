package com.fusion.shared.data.remote.justfa.repositories

import com.fusion.shared.data.remote.justfa.api.account.JfaAccountApi
import com.fusion.shared.data.remote.justfa.api.auth.simple.AuthServiceSimple
import com.fusion.shared.data.remote.justfa.repositories.mappers.toDomain
import com.fusion.shared.domain.models.Account
import com.fusion.shared.domain.repositories.AccountService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Prefix "Jfa" is to distinguish from other remote API, included in to the project.
 */
class JfaAccountService: AccountService, KoinComponent {

    /**
     * It is supposed that the Auth Service have to be accessible by other services
     * and repositories, so it is Single DI component.
     */
    private val authService: AuthServiceSimple by inject()

    private val accountApi = JfaAccountApi()

    override suspend fun login(username: String, password: String): Flow<Account?> {
        authService.authorize(username, password)
        return flowOf(accountApi.getAccount().toDomain())
    }

    override suspend fun authorise(): Flow<Account?> {
        TODO("Not yet implemented")
    }
}