plugins {
    kotlin("jvm") version "1.7.10"
}

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly("org.jetbrains:annotations:23.0.0")
    api("com.github.Minestom:Minestom:-SNAPSHOT")
    implementation("io.insert-koin:koin-core:3.2.0")
}