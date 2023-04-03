package com.fusion.shared.data.remote.justfa.repositories

import com.fusion.shared.data.remote.justfa.wsapi.JfaWebSocketChannel
import com.fusion.shared.domain.repositories.CommunicationCenterService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class JfaCommunicationCenterService: CommunicationCenterService, KoinComponent {

    private val webSocketChannel: JfaWebSocketChannel by inject()

    override suspend fun openSession(): Result<Boolean> {
        return webSocketChannel.openSession()
    }

    override suspend fun closeSession() {
        webSocketChannel.closeSession()
    }

    override suspend fun shutdown() {
        TODO("Not yet implemented")
    }
}