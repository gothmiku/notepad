
plugins {
    id("com.android.application")
    kotlin("android")
    alias(libs.plugins.compose.compiler)
    id("com.google.devtools.ksp") version "2.0.20-1.0.25"
}

android {
    namespace = "com.example.yourapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.yourapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true // ✅ Enable Compose
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13" // ✅ Use latest Compose Compiler version
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:stabilityConfigurationPath=${project.layout.projectDirectory.file("C:/Users/berka/AndroidStudioProjects/notepad/app/src/main/java/stability_config.conf")}"
        )
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    stabilityConfigurationFile = rootProject.layout.projectDirectory.file("stability_config.conf")
}

dependencies {
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.runtime.livedata)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    val composeBom = platform("androidx.compose:compose-bom:2025.05.00") // ✅ Compose BOM
    implementation(composeBom)

    // ✅ Compose core libraries
    implementation(libs.ui)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)

    // Optional: for lifecycle + activity
    implementation(libs.androidx.activity.compose.v190)
    implementation(libs.androidx.lifecycle.runtime.ktx.v270)

    testImplementation(libs.junit)

    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    implementation(libs.androidx.room.runtime)
    implementation(libs.dagger.compiler)
    ksp(libs.dagger.compiler)
    // If this project uses any Kotlin source, use Kotlin Symbol Processing (KSP)
    // See Add the KSP plugin to your project
    ksp(libs.room.compiler)


        // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.androidx.room.ktx)

        // optional - RxJava2 support for Room
    implementation(libs.androidx.room.rxjava2)

        // optional - RxJava3 support for Room
    implementation(libs.androidx.room.rxjava3)

        // optional - Guava support for Room, including Optional and ListenableFuture
    implementation(libs.androidx.room.guava)

        // optional - Test helpers
    testImplementation(libs.androidx.room.testing)

        // optional - Paging 3 Integration
    implementation(libs.androidx.room.paging)

    implementation(libs.androidx.lifecycle.viewmodel.compose)

}

