
plugins {
    id("com.android.application")
    kotlin("android")
    alias(libs.plugins.compose.compiler)
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
        kotlinCompilerExtensionVersion = "1.5.3" // ✅ Use latest Compose Compiler version
    }

    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs += listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:stabilityConfigurationPath=${project.layout.projectDirectory.file("C:/Users/berka/AndroidStudioProjects/notepad/app/src/main/java/stability_config.conf")}"
        )
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

composeCompiler {
    reportsDestination = layout.buildDirectory.dir("compose_compiler")
    stabilityConfigurationFile = rootProject.layout.projectDirectory.file("stability_config.conf")
}

dependencies {
    implementation(libs.androidx.ui.graphics)
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
}
