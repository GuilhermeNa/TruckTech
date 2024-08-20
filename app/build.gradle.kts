plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "br.com.apps.trucktech"
    compileSdk = 34

    defaultConfig {
        applicationId = "br.com.apps.trucktech"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    testBuildType = "debug"

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {

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
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/{LICENSE.md}"
            excludes += "META-INF/{LICENSE-notice.md}"
        }
    }
}

dependencies {

    // My Modules
    implementation(project(mapOf("path" to ":model")))
    implementation(project(mapOf("path" to ":useCase")))
    implementation(project(mapOf("path" to ":repository")))

    //Test && Debug
    val testCoroutinesVersion = "1.8.1"
    implementation("com.squareup.leakcanary:leakcanary-android:2.12")
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$testCoroutinesVersion")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("io.mockk:mockk-android:1.13.11")
    androidTestImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$testCoroutinesVersion")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.6.1") {
        exclude(module = "protobuf-lite")
    }
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //FireBase
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage-ktx")

    // Native
    val navVersion = "2.7.7"
    val lifecycleVersion = "2.7.0"
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.annotation:annotation:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    // View utilities
    val coilVersion = "2.4.0"
    implementation("io.coil-kt:coil:$coilVersion")
    implementation("io.coil-kt:coil-gif:$coilVersion")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("me.relex:circleindicator:2.1.6")
    implementation("com.github.MikeOrtiz:TouchImageView:3.6")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.facebook.shimmer:shimmer:0.5.0")
    implementation("io.github.vicmikhailau:MaskedEditText:5.0.1")

    // DI && Preferences
    implementation("io.insert-koin:koin-android:3.4.0")
    implementation("androidx.datastore:datastore-preferences:1.0.0")

   /* // CameraX core library using the camera2 implementation
    val cameraxVersion = "1.4.0-alpha05"
    implementation("androidx.camera:camera-camera2:$cameraxVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraxVersion")
    implementation("androidx.camera:camera-view:$cameraxVersion")
    implementation("androidx.camera:camera-extensions:$cameraxVersion")*/

}