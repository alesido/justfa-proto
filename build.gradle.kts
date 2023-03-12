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
// generated with Android Studio:
plugins {
    id(Plugins.androidApplication) version "7.4.1" apply false
    id(Plugins.androidLibrary) version "7.4.1" apply false
    id(Plugins.androidKotlin) version "1.7.0" apply false
}