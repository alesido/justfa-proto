package com.fusion.shared.data.remote.justfa.api

import io.ktor.http.*

object JfaApiConfiguration {

    const val AUTH_URL = ""
    const val AUTH_TOKEN_REQUEST_URL = "http://dev.lno.fam-intra:8080/oauth/token"
    const val API_CLIENT_ID = "fusion-ui" //"fusion-android-app"

    const val API_PROTOCOL_NAME = "http"
    const val API_PROTOCOL_PORT = 8080
    val API_PROTOCOL = URLProtocol(API_PROTOCOL_NAME, API_PROTOCOL_PORT)
    const val API_HOST = "dev.lno.fam-intra"
    const val API_PATH = "/api/v1/"
    const val API_BASE_URL = "$API_PROTOCOL_NAME://$API_HOST:$API_PROTOCOL_PORT$API_PATH"
}