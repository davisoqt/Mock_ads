plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.firebase.crashlytics")
    id("com.google.devtools.ksp")
    id("io.realm.kotlin")
//    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.mockads"
    compileSdk = 34

    signingConfigs {
        create("release") {
            storeFile = file("debug.jks")
            storePassword = "hGE#^0v18Huy"
            keyAlias = "topewow"
            keyPassword = "hGE#^0v18Huy"
        }
    }

    defaultConfig {
        applicationId = "com.example.mockads"
        minSdk = 24
        targetSdk = 34
        versionCode = if(project.hasProperty("BUILD_NUMBER")) (project.properties["BUILD_NUMBER"] as String)!!.toInt() else 1
        versionName = "1.${defaultConfig.versionCode}"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.compose.boom))
    implementation(libs.compose.ui)
    implementation(libs.compose.graphics)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.material3)
    implementation(libs.androidx.window)
    debugImplementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.test.manifest)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(project(":ads_module"))

    implementation("io.github.raamcosta.compose-destinations:animations-core:1.11.1-alpha")
    ksp("io.github.raamcosta.compose-destinations:ksp:1.11.1-alpha")

    implementation(libs.workmanager)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.coil.compose)

    implementation(libs.realm)
    implementation(libs.jsoup)
}




