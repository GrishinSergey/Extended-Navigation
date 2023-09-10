plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

group = "com.sagrishin.extended.navigation.library"

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

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.1"
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
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")
    implementation("androidx.compose.ui:ui:1.5.0-beta02")

    implementation("com.google.code.gson:gson:2.10.1")
}


publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.sagrishin.extended"
            artifactId = "nav-library"
            version = "1.0.1.3"

            afterEvaluate { artifact(tasks.getByName("bundleReleaseAar")) }
        }
    }
}
