plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.nothingsecure"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.nothingsecure"
        minSdk = 26
        targetSdk = 36
        versionCode = 31
        versionName = "0.3.21-Pokemon_X"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    // Personal build: the keystore comes from CI secrets, decoded to the path in
    // KEYSTORE_FILE. Without it the release build is simply left unsigned.
    val keystorePath = System.getenv("KEYSTORE_FILE")
    if (keystorePath != null && file(keystorePath).exists()) {
        signingConfigs {
            create("release") {
                storeFile = file(keystorePath)
                storePassword = System.getenv("KEYSTORE_PASSWORD")
                keyAlias = System.getenv("KEY_ALIAS")
                keyPassword = System.getenv("KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfigs.findByName("release")?.let { signingConfig = it }
        }
    }

    // Personal build for one phone, so only the arm64 slice is produced.
    splits {
        abi {
            isEnable = true
            reset()
            include("arm64-v8a")
            isUniversalApk = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.security.crypto)
    implementation (libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.biometric)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}