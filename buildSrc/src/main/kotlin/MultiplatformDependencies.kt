object MultiplatformDependencies {

    // kotlinx
    const val kotlinxCoroutines =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinxCoroutines}"
    const val kotlinxSerialization =
        "org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.kotlinxSerialization}"
    const val kotlinxDateTime = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.kotlinxDateTime}"

    // koin
    const val koinCore = "io.insert-koin:koin-core:${Versions.koin}"
    const val koinTest = "io.insert-koin:koin-test:${Versions.koin}"
    const val koinTestJUnit4 = "io.insert-koin:koin-test-junit4:${Versions.koin}"
    const val koinTestJUnit5 = "io.insert-koin:koin-test-junit5:${Versions.koin}"

    // kotest
    const val kotest = "io.kotest:kotest-runner-junit5:${Versions.kotest}"
    const val kotestKoin = "io.kotest.extensions:kotest-extensions-koin:${Versions.kotestKoin}"
    const val slf4j = "org.slf4j:slf4j-simple:${Versions.slf4j}"

    // ktor
    const val ktorCore = "io.ktor:ktor-client-core:Plugins${Versions.ktor}"
    const val ktorEngineCio = "io.ktor:ktor-client-cio:${Versions.ktor}"
    const val ktorSerialization = "io.ktor:ktor-client-serialization:${Versions.ktor}"
    const val ktorJsonSerialization = "io.ktor:ktor-serialization-kotlinx-json:${Versions.ktor}"
    const val ktorContentNegotiation = "io.ktor:ktor-client-content-negotiation:${Versions.ktor}"
    const val ktorClientAuth = "io.ktor:ktor-client-auth:${Versions.ktor}"
    const val ktorLogging = "io.ktor:ktor-client-logging:${Versions.ktor}"
    const val ktorAndroid = "io.ktor:ktor-client-android:${Versions.ktor}"
    const val ktoriOS = "io.ktor:ktor-client-ios:${Versions.ktor}"
    const val ktorJvm = "io.ktor:ktor-client-java:${Versions.ktor}"
    const val ktorMock = "io.ktor:ktor-client-mock:${Versions.ktor}"

    // napier (ktor logging)
    const val napier = "io.github.aakira:napier:${Versions.napier}"

    // sql delight
    const val sqlDelight = "com.squareup.sqldelight:runtime:${Versions.sqlDelight}"
    const val sqlDelightCoroutine =
        "com.squareup.sqldelight:coroutines-extensions:${Versions.sqlDelight}"
    const val sqlDelightAndroid = "com.squareup.sqldelight:android-driver:${Versions.sqlDelight}"
    const val sqlDelightIos = "com.squareup.sqldelight:native-driver:${Versions.sqlDelight}"

    // multiplatform settings
    const val multiplatformSettings =
        "com.russhwolf:multiplatform-settings-no-arg:${Versions.multiplatformSettings}"
    const val multiplatformSettingsCoroutines =
        "com.russhwolf:multiplatform-settings-coroutines:${Versions.multiplatformSettings}"
    const val multiplatformSettingsTest =
        "com.russhwolf:multiplatform-settings-test:${Versions.multiplatformSettings}"

    const val mockk = "io.mockk:mockk:${Versions.mockk}"

    const val kotlinxTestResources = "com.goncalossilva:resources:${Versions.kotlinxTestResources}"

    const val kotlinxCoroutinesTest =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.kotlinxCoroutines}"
}
