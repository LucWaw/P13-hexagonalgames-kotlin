plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.crashlytics)
}

android {
    namespace = "com.openclassrooms.hexagonal.games"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.openclassrooms.hexagonal.games"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }

    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }




}
tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
dependencies {
    //kotlin
    implementation(platform(libs.kotlin.bom))

    //DI
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)

    //compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.material)
    implementation(libs.compose.material3)
    implementation(libs.lifecycle.runtime.compose)
    debugImplementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)

    implementation(libs.activity.compose)
    implementation(libs.navigation.compose)

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.coil.compose)
    implementation(libs.accompanist.permissions)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //JUnit5
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.console)
    testImplementation(libs.vintage.junit.vintage.engine)

    //Mockito
    testImplementation(libs.mockito.junit.jupiter)
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)

    //Firebase
    implementation(platform(libs.firebase.bom))

    //Analytics
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)

    //Authentication
    implementation(libs.firebase.ui.auth)

    //Messaging
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.messaging.directboot)

    //Storage
    implementation(libs.firebase.ui.storage)
    //Glide
    implementation(libs.glide)
    ksp(libs.glide.compiler)

    //Firestore
    implementation(libs.firebase.ui.firestore)

}
