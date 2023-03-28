package com.fusion.shared.data.remote.justfa.api

import io.ktor.http.*

object JfaApiConfiguration {

    // Authorisation
    const val AUTH_URL = ""
    const val AUTH_TOKEN_REQUEST_URL = "http://dev.lno.fam-intra:8080/oauth/token"
    const val API_CLIENT_ID = "fusion-ui" //"fusion-android-app"

    // API
    private const val API_PROTOCOL_NAME = "http"
    private const val API_PROTOCOL_PORT = 8080
    val API_PROTOCOL = URLProtocol(API_PROTOCOL_NAME, API_PROTOCOL_PORT)
    const val API_HOST = "dev.lno.fam-intra"
    const val API_PATH = "/api/v1/"
    const val API_BASE_URL = "$API_PROTOCOL_NAME://$API_HOST:$API_PROTOCOL_PORT$API_PATH"

    // Web Sockets, Staging
//    const val WS_PORT = 8080
//    const val WS_HOST = "dev.lno.fam-intra"
//    private const val WS_PATH = "/chat"

    // Web Sockets, Debugging
    const val WS_PORT = 8082
    const val WS_HOST = "localhost"
    private const val WS_PATH = ""


    fun wsPathPrefix(accessToken: String?) = accessToken?.let {
        "$WS_PATH?access_token=${it}"
    }?: WS_PATH
}