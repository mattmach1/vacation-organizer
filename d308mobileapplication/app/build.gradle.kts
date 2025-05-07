plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.d308mobileapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.d308mobileapplication"
        minSdk = 26
        targetSdk = 35
        versionCode = 2
        versionName = "1.0.1"

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

    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.security.crypto)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.robolectric:robolectric:4.10.3")
    testImplementation("androidx.test:core:1.5.0")

    androidTestImplementation("androidx.test:core:1.5.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    // Room components
    implementation("androidx.room:room-runtime:${rootProject.extra.get("roomVersion")}")
    annotationProcessor("androidx.room:room-compiler:${rootProject.extra.get("roomVersion")}")
    androidTestImplementation("androidx.room:room-testing:${rootProject.extra.get("roomVersion")}")
    implementation("androidx.security:security-crypto:1.1.0-alpha03")
    implementation("net.zetetic:android-database-sqlcipher:4.5.0")
}