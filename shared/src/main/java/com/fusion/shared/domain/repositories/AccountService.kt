package com.fusion.shared.domain.repositories

import com.fusion.shared.domain.models.Account
import kotlinx.coroutines.flow.Flow

/**
 * Account data service covering data on user identity and authorization.
 *
 * This is Service, not Repository, because deals with actions
 * in addition to CRUD operations.
 *
 * Supposed there will be separate repository for User Profile which is
 * quite complicated in the project.
 */
interface AccountService {
    /**
     * * User log in (sign in)
     *
     * Legacy (temporary) method to start user session. To be replaced with a real OAuth flow.
     */
    suspend fun login(username: String, password: String): Account

    /**
     * * User authorization
     *
     * Desired (future) method to start user session where user credentials received
     * by an external application via its authorization UI.
     */
    suspend fun authorise(): Account


    suspend fun currentAccount(): Account?
}