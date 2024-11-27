import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
    id("org.jetbrains.kotlin.android")
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localPropertiesFile.inputStream().use { localProperties.load(it) }
}

val ip: String = localProperties.getProperty("ip") ?: ""

android {
    namespace = "com.sigit.mechaban"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sigit.mechaban"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "ip", "\"$ip\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    //noinspection GradlePath
    implementation(files("D:/Library Java/osmbonuspack_6.9.0.aar"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.glide)
    implementation(libs.gson)
    implementation(libs.dotsindicator)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.javax.annotation)
    implementation(libs.shuhart.stepview)
    implementation(libs.ucrop)
    implementation(libs.osmdroid.android)
    implementation(libs.play.services.location)
    implementation(libs.motiontoast)
    implementation(libs.volley)
}