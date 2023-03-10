object AndroidSdk {
    const val appName = "JustFaProto"

    const val applicationId = "com.justfa.android.proto"

    const val buildToolVersion = "30.0.3"

    const val minSdkVersion = 26
    const val compileSdkVersion = 33
    const val targetSdkVersion = compileSdkVersion

    const val versionCode = 1
    const val versionName = "1.0"

    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
}

object AndroidDependencies {
    // kotlin
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    const val kotlinxCotoutines =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.kotlinxCotoutines}"

    // koin DI
    const val koinAndroid = "io.insert-koin:koin-android:${Versions.koin}"
    const val koinCompose = "io.insert-koin:koin-androidx-compose:${Versions.koin}"

    // androidx
    const val androidxCore = "androidx.core:core-ktx:${Versions.androidxCore}"
    const val androidxLifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.androidxLifecycle}"
    const val viewModelLifecycle = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.androidxLifecycle}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"

    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"

    // jetpack compose, core
    const val composeActivity =
        "androidx.activity:activity-compose:${Versions.composeActivity}"
    const val composeUi = "androidx.compose.ui:ui:${Versions.compose}"
    const val composeTooling = "androidx.compose.ui:ui-tooling-preview:${Versions.compose}"

    // jetpack compose, material theming
    const val material = "com.google.android.material:material:1.9.0-alpha02"
    const val material3 = "androidx.compose.material3:material3:${Versions.material3}"
    const val material3WindowSize =
        "androidx.compose.material3:material3-window-size-class:${Versions.material3}"
    const val materialIcons = "androidx.compose.material:material-icons-core:${Versions.materialIcons}"
    const val materialIconsExt = "androidx.compose.material:material-icons-extended:${Versions.materialIcons}"
    const val googleFonts = "androidx.compose.ui:ui-text-google-fonts:${Versions.googleFonts}"

    // jetpack compose, navigation
    const val navigation = "androidx.navigation:navigation-compose:${Versions.navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui-ktx:${Versions.navigationUi}"
    const val navigationFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navigationUi}"

    // accompanist, jetpack compose additions
    const val accompanistSystemUIController =
        "com.google.accompanist:accompanist-systemuicontroller:${Versions.accompanist}"

    // ad hoc storage
    const val hawk = "com.orhanobut:hawk:${Versions.hawk}"

    // http & web sockets
    const val okHttp = "com.squareup.okhttp3:okhttp:${Versions.okHttp}"

    // time
    const val jodaTime = "joda-time:joda-time:${Versions.jodaTime}"

    // coil-image loader
    const val coil = "io.coil-kt:coil-compose:${Versions.coil}"

    // leak canary
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"

    // Testing
    const val jUnit = "junit:junit:${Versions.jUnit}"

    const val coroutinesTest =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesTest}"

    const val composeUiTest = "androidx.compose.ui:ui-test-junit4:${Versions.compose}"
    const val composeUiTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
    const val composeUiManifest = "androidx.compose.ui:ui-test-manifest:${Versions.compose}"

    const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
    const val robolectric = "org.robolectric:robolectric:${Versions.robolectric}"

    const val androidxJUnit= "androidx.test.ext:junit:1.1.5:${Versions.androidxJUnit}"
    const val androidxJUnitKtx = "androidx.test.ext:junit-ktx:${Versions.androidxJUnit}"
    const val testCore = "androidx.test:core-ktx:${Versions.testCore}"
    const val archTestCore = "androidx.arch.core:core-testing:${Versions.archTestCore}"
    const val testRules = "androidx.test:rules:${Versions.testRules}"
    const val testRunner = "androidx.test:runner:${Versions.testRunner}"


}
