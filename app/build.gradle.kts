plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
    //alias(libs.plugins.google.gms.google.services)
    id ("com.google.gms.google-services")
}

android {
    namespace = "com.mobile.medialibraryapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.mobile.medialibraryapp"
        minSdk = 23
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
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

configurations.all {
    resolutionStrategy {
        force ("io.grpc:grpc-okhttp:1.55.1")
        force ("io.grpc:grpc-core:1.55.1")
        force ("io.grpc:grpc-api:1.55.1")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.espresso.core)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.timber)

    implementation(libs.androidx.cardview)
    implementation(libs.material)
    testImplementation(libs.junit)

    //Image library
    implementation(libs.coil.compose)

    implementation(libs.androidx.activity.ktx)

    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    kapt(libs.hilt.compiler)

    //Hilt in ViewModels
    kapt(libs.androidx.hilt.compiler)

    //Compose Navigation
    implementation(libs.androidx.navigation.compose)

    //Compose Constraint Layout
    implementation(libs.androidx.constraintlayout.compose)

    //RoomDB
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.paging)
    implementation (libs.androidx.room.ktx)

    implementation (platform(libs.firebase.bom))
    implementation (libs.google.firebase.auth)
    implementation (libs.firebase.firestore)
    implementation (libs.play.services.auth)
    implementation ("com.google.firebase:firebase-appcheck-playintegrity:18.0.0")
    implementation ("io.grpc:grpc-okhttp:1.55.1")
    implementation ("io.grpc:grpc-core:1.55.1")
    implementation ("io.grpc:grpc-api:1.55.1")

    //Media3
    implementation (libs.androidx.media3.exoplayer)
    implementation (libs.androidx.media3.ui)
    implementation (libs.androidx.media3.common)
    implementation (libs.androidx.media3.session)

}