plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.kapt")
    id("maven-publish")
}

group = "com.sagrishin.extended.navigation.generator"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(project(":annotations"))
    implementation("com.squareup:kotlinpoet:1.12.0")
    implementation("com.google.auto.service:auto-service:1.1.1")
    kapt("com.google.auto.service:auto-service:1.1.1")
}


publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.sagrishin.extended"
            artifactId = "nav-generator"
            version = "1.0.2"

            from(components["java"])
        }
    }
}
