import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.logging.LogFactory.release

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("maven-publish")
}

android {
    namespace = "qtjambiii.ads"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")

    api("com.onesignal:OneSignal:[5.0.0, 5.99.99]")
    api("com.google.android.gms:play-services-ads:22.5.0")
    api("androidx.datastore:datastore-preferences:1.0.0")
    api("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")

    implementation(platform("com.google.firebase:firebase-bom:32.4.1"))
    api("com.google.firebase:firebase-crashlytics-ktx")
    api("com.google.firebase:firebase-analytics-ktx")
    api("com.google.firebase:firebase-config-ktx")
    api("net.danlew:android.joda:2.9.9.3")
    api("com.blankj:utilcodex:1.31.0")

    implementation("com.google.android.play:app-update:2.1.0")
    implementation("com.google.android.play:app-update-ktx:2.1.0")
    implementation("com.google.android.play:review:2.0.1")
    implementation("com.google.android.play:review-ktx:2.0.1")

}

afterEvaluate {
    publishing {
        publications {
            register("release", MavenPublication::class) {
                from(components["release"])
                artifactId = "qtjambiii-ads"
                groupId = "qtjambiii.ads"
                version = "1.0"
            }
        }
    }
}