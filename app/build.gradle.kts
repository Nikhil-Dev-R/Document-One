plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.1.10-1.0.29"// KSP plugin
    alias(libs.plugins.kotlinx.serialization)// Kotlinx serialization plugin
}

android {
    namespace = "com.rudraksha.documentone"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.rudraksha.documentone"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true // Enable ProGuard/R8
            isShrinkResources = true // Enable resource shrinking
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

    // Room database dependencies
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.5.1")
    implementation(libs.androidx.navigation.compose)
    // Use KSP for Room's annotation processing
    ksp("androidx.room:room-compiler:2.6.1")
    implementation(libs.kotlinx.serialization.json)

    // Icons
    implementation(libs.material.icons.extended)

    // CameraX for camera capture
    implementation("androidx.camera:camera-camera2:1.4.1")
    implementation("androidx.camera:camera-lifecycle:1.4.1")
    implementation("androidx.camera:camera-view:1.4.1")

// ML Kit for Text Recognition (Latin scripts: English, Spanish, etc.)
    implementation("com.google.mlkit:text-recognition:16.0.0")

// ML Kit for Devanagari Text Recognition (Hindi, Sanskrit, Marathi, etc.)
    implementation("com.google.mlkit:text-recognition-devanagari:16.0.0")

// ML Kit for Chinese Text Recognition
//    implementation("com.google.mlkit:text-recognition-chinese:16.0.0")

// ML Kit for Japanese Text Recognition
//    implementation("com.google.mlkit:text-recognition-japanese:16.0.0")

// ML Kit for Korean Text Recognition
//    implementation("com.google.mlkit:text-recognition-korean:16.0.0")

    // Pdf Viewer
    implementation("androidx.pdf:pdf-viewer:1.0.0-alpha06")

    // Apache POI for Office documents
    implementation("org.apache.poi:poi-ooxml:5.2.3")

    // Accompanist for permissions and system UI controller
    implementation("com.google.accompanist:accompanist-permissions:0.30.1")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.30.1")

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}