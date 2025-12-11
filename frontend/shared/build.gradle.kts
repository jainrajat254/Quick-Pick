import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinx.serialization)
    id("org.jetbrains.kotlin.native.cocoapods")
}

kotlin {

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Shared module for Quick Pick App"
        homepage = "https://quickpick.in"
        ios.deploymentTarget = "14.0"
        version = "1.0.0"
        podfile = project.file("../iosApp/Podfile")

        framework {
            baseName = "shared"
            isStatic = true
        }
    }
    sourceSets {
        all {
            languageSettings.optIn("kotlin.time.ExperimentalTime")
        }
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
            implementation(libs.androidx.work.runtime.ktx)
            implementation(libs.ktor.client.android)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(compose.materialIconsExtended)
            //Network Image
            implementation(libs.landscapist.coil3)
            implementation(libs.landscapist.animation)
            implementation(libs.landscapist.placeholder)
            //Date And Time
            implementation(libs.kotlinx.datetime)
            //Networking (Ktor)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)

            implementation(libs.kotlinx.coroutines.core)
            //DI (Koin)
            api(libs.koin.core)
            implementation(libs.koin.compose)

            implementation(libs.koin.compose.viewmodel)
            implementation(libs.lifecycle.viewmodel)

            //Logging
            implementation(libs.kermit)
            //Navigation
            implementation(libs.jetbrain.navigation)

            //Local Storage
            api(libs.datastore.preferences)
            api(libs.datastore)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.ios)
            implementation(libs.ktor.client.darwin)

            implementation(libs.kotlinx.coroutines.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }

}
android {
    namespace = "org.rajat.quickpick"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
    }

    packaging.resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
    implementation(libs.stompprotocolandroid)
    implementation(libs.rxjava)
    implementation(libs.rxandroid)
    implementation(libs.gson)
    implementation(libs.okhttp)
}