import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    
    jvm()
    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            // Core Compose dependencies
            api(compose.runtime)              // Core of Compose, provides @Composable
            api(compose.foundation)           // Provides basic layout components like Column, Row
            api(compose.material3)            // Provides styled components like Card, Text, Icon
            api(compose.ui)                   // Core UI primitives
            api(compose.material)
            api(libs.compose.material.icons.extended)   // Icons
            // Ktor for Networking
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.contentnegotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            // SQLDelight for local Database
            implementation(libs.sqldelight.runtime)
            // Decompose for Navigation & Component Logic
            implementation(libs.decompose)
            implementation(libs.decompose.extensions.compose)
            // Koin for Dependency Injection
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            // Image Loading from URL
            implementation(libs.kamel.image)
            // Date and Time
            implementation(libs.kotlinx.datetime)
            // Supabase Auth and Database
            implementation(libs.supabase.gotrue)
            implementation(libs.supabase.postgres)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.cinemate.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()

    }
}

// generates a kotlin file with the API key
tasks.register("generateApiKey") {
    val properties = Properties()
    val localPropertiesFile = project.rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        properties.load(localPropertiesFile.inputStream())
    }

    // fetch keys and values
    val tmdbApiKey = properties.getProperty("tmdb.api.key", "")
    val supabaseUrl = properties.getProperty("SUPABASE_URL", "")
    val supabaseKey = properties.getProperty("SUPABASE_KEY", "")
    val generatedFile = file("$buildDir/generated/moko/common/com/cinemate/shared/ApiKey.kt")

    outputs.files(generatedFile)

    doLast {
        generatedFile.parentFile.mkdirs()
        generatedFile.writeText(
            """
                 package com.cinemate.shared
                 
                 object ApiKey {
                    const val TMDB_API_KEY = "$tmdbApiKey"
                    const val SUPABASE_URL = "$supabaseUrl"
                    const val SUPABASE_KEY = "$supabaseKey"
                 }
            """.trimIndent()
        )
    }
}

kotlin.sourceSets.commonMain.get().kotlin.srcDir("$buildDir/generated/moko/common")

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().all {
    dependsOn("generateApiKey")
}
