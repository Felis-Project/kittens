allprojects {
    group = "io.github.joemama"
    version = "1.1-ALPHA"
}

plugins {
    alias(libs.plugins.felis.dam)
    `maven-publish`
    alias(libs.plugins.dokka)
}

loaderMake {
    version = "1.20.4"
}

dependencies {
    implementation(libs.felis)
    implementation(libs.micromixin)
}

tasks.processResources {
    filesMatching("mods.toml") {
        expand("version" to version)
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            pom {
                artifactId = "kittens"
            }
            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "Repsy"
            url = uri("https://repo.repsy.io/mvn/0xjoemama/public")
            credentials {
                username = System.getenv("REPSY_USERNAME")
                password = System.getenv("REPSY_PASSWORD")
            }
        }
    }
}
