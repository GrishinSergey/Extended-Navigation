plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("maven-publish")
}

group = "com.sagrishin.extended.navigation.annotations"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}


publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.sagrishin.extended"
            artifactId = "nav-annotations"
            version = "1.0.1"

            from(components["java"])
        }
    }
}
