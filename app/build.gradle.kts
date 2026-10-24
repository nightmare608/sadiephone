import java.security.KeyStore
import java.security.MessageDigest

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}
val signingFile = providers.environmentVariable("SADIEPHONE_KEYSTORE").orNull
val passwordFile = providers.environmentVariable("SADIEPHONE_PASSWORD_FILE").orNull
val hasDistributionKey = signingFile != null && passwordFile != null
android {
    namespace = "com.sadiephone.game"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.sadiephone.game"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    signingConfigs {
        if (hasDistributionKey) create("distribution") {
            storeFile = file(signingFile!!)
            storePassword = file(passwordFile!!).readText().trim()
            keyAlias = "sadiephone"
            keyPassword = storePassword
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            if (hasDistributionKey) signingConfig = signingConfigs.getByName("distribution")
        }
        debug { applicationIdSuffix = ".debug"; versionNameSuffix = "-debug" }
    }
    buildFeatures { compose = true }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
}
kotlin { compilerOptions { jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17) } }
tasks.register("verifyDistributionKey") {
    doLast {
        check(hasDistributionKey) { "Release requires SADIEPHONE_KEYSTORE and SADIEPHONE_PASSWORD_FILE. Keep the same private key for updates." }
        val password = file(passwordFile!!).readText().trim().toCharArray()
        val store = KeyStore.getInstance("PKCS12")
        file(signingFile!!).inputStream().use { store.load(it, password) }
        val certificate = checkNotNull(store.getCertificate("sadiephone")) { "Missing sadiephone alias" }
        val actual = MessageDigest.getInstance("SHA-256").digest(certificate.encoded).joinToString("") { "%02x".format(it) }
        val expected = rootProject.file("release-certificate.sha256").readText().replace(":", "").trim().lowercase()
        check(actual == expected) { "Distribution certificate differs from the published app. Release stopped to prevent an incompatible update." }
        password.fill('\u0000')
    }
}
tasks.matching { it.name == "assembleRelease" || it.name == "validateSigningRelease" }.configureEach { dependsOn("verifyDistributionKey") }
dependencies {
    val bom = platform("androidx.compose:compose-bom:2025.01.01")
    implementation(bom)
    androidTestImplementation(bom)
    implementation("androidx.activity:activity-compose:1.10.0")
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.json:json:20250517")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
