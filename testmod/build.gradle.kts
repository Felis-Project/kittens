plugins {
    alias(libs.plugins.felis.dam)
    `maven-publish`
    alias(libs.plugins.dokka)
}

group = "felis"
version = "1.2.0-alpha"

loaderMake {
    version = "1.21"
}

repositories {
    mavenLocal()
}

dependencies {
    implementation(libs.felis)
    implementation(libs.micromixin)
    implementation(project(":"))
}

tasks.processResources {
    filesMatching("felis.mod.toml") {
        expand("version" to version)
    }
}
