plugins {
    id("java")
    alias(libs.plugins.shadow)
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(project(":teamchat-common"))

    compileOnly(libs.miniplaceholders)
    compileOnly("io.papermc.paper:paper-api:1.19.4-R0.1-SNAPSHOT")
}