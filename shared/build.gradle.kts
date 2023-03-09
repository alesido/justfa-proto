plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    //kotlin("jvm") version "1.8.10" // or kotlin("multiplatform") or any other kotlin plugin
    kotlin("plugin.serialization") version "1.8.10"
}

android {
    namespace = "com.fusion.shared"
    compileSdk = 33

    defaultConfig {
        minSdk = 24
        //targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        //consumerProguardFiles = "consumer-rules.pro"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
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

            api(MultiplatformDependencies.koinCore)

            api(MultiplatformDependencies.napier)
        }

        sourceSets["test"].dependencies {
            implementation(kotlin("test"))
            implementation(MultiplatformDependencies.koinTest)
            implementation("org.slf4j:slf4j-simple:1.7.26")
            //implementation(MultiplatformDependencies.kotest)
            //implementation(MultiplatformDependencies.kotestKoin)
            implementation(MultiplatformDependencies.ktorMock)
            implementation(MultiplatformDependencies.kotlinxTestResources)
            implementation(MultiplatformDependencies.kotlinxCoroutinesTest)
            implementation(MultiplatformDependencies.multiplatformSettingsTest)
        }
    }
}