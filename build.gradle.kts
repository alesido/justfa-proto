// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra.apply{
        set("compose_version", "1.3.3")
    }
    dependencies {
        classpath(Plugins.kotlin)
        classpath(Plugins.gradle)
    }
}
plugins {
    id("com.android.application") version "7.4.1" apply false
    id("com.android.library") version "7.4.1" apply false
    id("org.jetbrains.kotlin.android") version "1.7.0" apply false
}