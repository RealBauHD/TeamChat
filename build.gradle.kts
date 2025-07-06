plugins {
    id("java")
}

group = "dev.bauhd"
version = "2.1-SNAPSHOT"

allprojects {
    apply(plugin = "java")

    repositories {
        mavenCentral()
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }

    tasks.withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}
