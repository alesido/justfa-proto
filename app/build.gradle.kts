plugins {
    kotlin(Plugins.android)
    id(Plugins.androidApplication)
}

android {
    namespace = "com.fusion.android"
    compileSdk = AndroidSdk.compileSdkVersion

    defaultConfig {
        applicationId = AndroidSdk.applicationId

        minSdk = AndroidSdk.minSdkVersion
        targetSdk = AndroidSdk.targetSdkVersion
        versionCode = AndroidSdk.versionCode
        versionName = AndroidSdk.versionName

        testInstrumentationRunner = AndroidSdk.testInstrumentationRunner

        vectorDrawables {
            @Suppress("UnstableApiUsage")
            useSupportLibrary = true
        }
    }
    buildTypes {
        getByName("release") {
            @Suppress("UnstableApiUsage")
            isMinifyEnabled = false
            proguardFiles(
                @Suppress("UnstableApiUsage")
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            @Suppress("UnstableApiUsage")
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
        freeCompilerArgs = listOf(
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
        )
    }
    @Suppress("UnstableApiUsage")
    buildFeatures {
        compose = true
    }
    @Suppress("UnstableApiUsage")
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    @Suppress("UnstableApiUsage")
    packagingOptions {
        resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")
    }
}

dependencies {

    constraints {
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.0") {
            because("kotlin-stdlib-jdk7 is now a part of kotlin-stdlib")
        }
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.0") {
            because("kotlin-stdlib-jdk8 is now a part of kotlin-stdlib")
        }
    }

    api(project(BuildModules.shared))

    // coroutines
    implementation(AndroidDependencies.kotlinxCotoutines)

    // androidx
    implementation(AndroidDependencies.androidxCore)
    implementation(AndroidDependencies.androidxLifecycle)
    implementation(AndroidDependencies.viewModelLifecycle)
    implementation(AndroidDependencies.appCompat)

    implementation(AndroidDependencies.koinAndroid)
    implementation(AndroidDependencies.koinCompose)

    implementation(AndroidDependencies.constraintLayout)

    // jetpack compose, core
    implementation(AndroidDependencies.composeActivity)
    implementation(AndroidDependencies.composeUi)
    implementation(AndroidDependencies.composeTooling)

    // jetpack compose, material theming
    implementation(AndroidDependencies.material)
    implementation(AndroidDependencies.material3)
    implementation(AndroidDependencies.material3WindowSize)
    implementation(AndroidDependencies.materialIcons)
    implementation(AndroidDependencies.materialIconsExt)
    implementation(AndroidDependencies.googleFonts)

    // jetpack compose, navigation
    implementation(AndroidDependencies.navigation)
    implementation(AndroidDependencies.navigationUi)
    implementation(AndroidDependencies.navigationFragment)

    // accompanist, jetpack compose additions
    implementation(AndroidDependencies.accompanistSystemUIController)

    // ad hoc storage
    implementation(AndroidDependencies.hawk)

    // http & web sockets
    implementation(AndroidDependencies.okHttp)


    // time
    implementation(AndroidDependencies.jodaTime)

    testImplementation(AndroidDependencies.jUnit)
    androidTestImplementation(AndroidDependencies.androidxJUnit)
    testImplementation(AndroidDependencies.espresso)

    // jetpack compose testing
    androidTestImplementation(AndroidDependencies.composeUiTest)
    androidTestImplementation(AndroidDependencies.composeUiTooling)
    androidTestImplementation(AndroidDependencies.composeUiManifest)
}