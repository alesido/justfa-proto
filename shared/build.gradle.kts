plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    //kotlin("jvm") version "1.8.10" // or kotlin("multiplatform") or any other kotlin plugin
    kotlin("plugin.serialization") version "1.8.10"
}

android {
    namespace = "com.fusion.shared"
    compileSdk = AndroidSdk.compileSdkVersion

    defaultConfig {
        minSdk = AndroidSdk.minSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        //consumerProguardFiles = "consumer-rules.pro"
    }

    buildTypes {
        release {
            @Suppress("UnstableApiUsage")
            isMinifyEnabled = false
            proguardFiles(
                @Suppress("UnstableApiUsage")
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

kotlin {
    sourceSets {

        sourceSets["main"].dependencies {
            implementation(MultiplatformDependencies.kotlinxCoroutines)
            implementation(MultiplatformDependencies.kotlinxDateTime)

            implementation(MultiplatformDependencies.ktorCore)
            implementation(MultiplatformDependencies.ktorEngineCio)
            implementation(MultiplatformDependencies.ktorJsonSerialization)
            implementation(MultiplatformDependencies.ktorContentNegotiation)
            implementation(MultiplatformDependencies.ktorLogging)
            implementation(MultiplatformDependencies.ktorClientAuth)
            api(MultiplatformDependencies.napier)

            api(MultiplatformDependencies.koinCore)
        }

        sourceSets["test"].dependencies {
            implementation(kotlin("test"))
            implementation(MultiplatformDependencies.koinTest)
            implementation(MultiplatformDependencies.slf4j)
            implementation(MultiplatformDependencies.ktorMock)
            implementation(MultiplatformDependencies.kotlinxTestResources)
            implementation(MultiplatformDependencies.kotlinxCoroutinesTest)
        }
    }
}