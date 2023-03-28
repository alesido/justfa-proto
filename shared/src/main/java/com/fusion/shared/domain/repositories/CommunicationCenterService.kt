package com.fusion.shared.domain.repositories

/**
 *  Communication service creates conditions required to run multiple services
 *  like text chat, voice and video communication, separately or concurrently.
 *
 *  I.e. text chat and voice communication are both require, e.g. web socket
 *  connection. Text chat uses it to send and receive messages, while voice
 *  or video communication, pear-to-pear or in conference, may use it to
 *  control the voice stream.
 *
 *  Communication service
 */
interface CommunicationCenterService {

    suspend fun openSession(): Result<Boolean>
    suspend fun closeSession()
    suspend fun shutdown()
}