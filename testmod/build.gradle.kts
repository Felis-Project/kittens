plugins {
    alias(libs.plugins.felis.dam)
    `maven-publish`
    alias(libs.plugins.dokka)
}

group = "felis"
version = "1.2.0-alpha"

loaderMake {
    version = "1.20.4"
}

repositories {
    mavenLocal()
}

dependencies {
    implementation(libs.felis)
    implementation(project(":"))
}

tasks.processResources {
    filesMatching("mods.toml") {
        expand("version" to version)
    }
}
