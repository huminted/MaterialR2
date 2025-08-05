plugins {
    kotlin("jvm")
}

group = "cn.iwakeup.r2client.api"
version = "1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    api("software.amazon.awssdk:s3:2.29.52")

}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}