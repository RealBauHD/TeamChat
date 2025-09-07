plugins {
    alias(libs.plugins.shadow)
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    implementation(project(":teamchat-common"))

    compileOnly(libs.miniplaceholders)
    compileOnly(libs.placeholderapi)
    compileOnly("io.papermc.paper:paper-api:1.20.6-R0.1-SNAPSHOT")
}