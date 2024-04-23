pluginManagement {
    repositories {
        maven {
            url = uri("https://repo.repsy.io/mvn/0xjoemama/public")
            name = "Loader Repo"
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "kittens"
include("testmod")
