allprojects {
    group = "felis"
    version = "1.5.1-alpha"
}

plugins {
    alias(libs.plugins.felis.dam)
    `maven-publish`
    alias(libs.plugins.dokka)
}

repositories {
    mavenLocal()
}

loaderMake {
    version = "1.21"
    accessWidener(file("src/main/resources/kittens.accesswidener"))
}

dependencies {
    implementation(libs.felis)
    implementation(libs.micromixin)
    implementation(libs.aw)

    include(libs.micromixin)
    api(libs.micromixin)
    include(libs.aw)
}

tasks.processResources {
    filesMatching("felis.mod.toml") {
        expand("version" to version)
    }
}

java {
    targetCompatibility = JavaVersion.VERSION_21
    sourceCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
    withSourcesJar()
}

kotlin {
    jvmToolchain {
        languageVersion = JavaLanguageVersion.of(21)
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
