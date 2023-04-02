package com.fusion.shared.data.remote.justfa.repositories

import com.fusion.shared.data.remote.justfa.api.account.JfaAccountApi
import com.fusion.shared.data.remote.justfa.api.auth.simple.JfaAuthServiceSimple
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
    private val authService: JfaAuthServiceSimple by inject()

    private val accountApi = JfaAccountApi()

    private var currentAuthorizedAccount: Account? = null

    override suspend fun login(username: String, password: String): Account {
        authService.authorize(username, password)
        val account = accountApi.getAccount().toDomain()
        currentAuthorizedAccount = account
        return account
    }

    override suspend fun authorise(): Account {
        TODO("Not yet implemented")
    }

    override suspend fun currentAccount(): Account? = currentAuthorizedAccount
}