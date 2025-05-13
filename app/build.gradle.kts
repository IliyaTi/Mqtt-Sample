plugins {
    id("com.google.devtools.ksp")
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("com.google.dagger.hilt.android")

    kotlin("plugin.serialization")
}

android {
    namespace = "com.example.mqttsample"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.mqttsample"
        minSdk = 26
        targetSdk = 35
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.1")

    // ROOM DB
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
//    ksp("android.arch.persistence.room:compiler:1.1.1")
    ksp("androidx.room:room-compiler:2.5.0")

    // PAHO - MQTT
    implementation(libs.org.eclipse.paho.client.mqttv3)
    implementation(libs.org.eclipse.paho.android.service)
//    implementation("com.github.hannesa2:paho.mqtt.android:4.4")

    implementation("androidx.localbroadcastmanager:localbroadcastmanager:1.1.0")

    // COMPOSE_NAVIGATION
    implementation(libs.androidx.navigation.compose)

    // DAGGER-HILT
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    ksp("androidx.lifecycle:lifecycle-compiler:2.9.0")
    implementation("com.google.dagger:hilt-android:2.56.2")
    ksp("com.google.dagger:hilt-android-compiler:2.56.2")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")


//
//    implementation("com.google.dagger:hilt-android:2.56.2")
//    ksp("com.google.dagger:hilt-android-compiler:2.56.2")



}


