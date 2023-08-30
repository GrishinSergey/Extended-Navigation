plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.sagrishin.extended.navigation.library"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
    }

    buildTypes {
        release {}

        debug {}
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.navigation:navigation-compose:2.6.0")
    implementation("com.google.code.gson:gson:2.10.1")
}
