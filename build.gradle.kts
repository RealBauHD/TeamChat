plugins {
    id("java")
}

group = "dev.bauhd"
version = "2.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}
