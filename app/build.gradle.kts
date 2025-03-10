plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "tn.esprit.pfe_club"
    compileSdk = 35

    defaultConfig {
        applicationId = "tn.esprit.pfe_club"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    flavorDimensions +="appType"
    productFlavors {
        create ("club_privileges") {
            dimension = "appType"
            applicationIdSuffix = ".club"
            versionNameSuffix = "-club"
        }
        create ("gym") {
            dimension = "appType"
            applicationIdSuffix = ".gym"
            versionNameSuffix = "-gym"
        }
    }


    buildTypes {
        debug {
            isMinifyEnabled = false

            buildConfigField("String", "BASE_URL_1", "\"https://api.clubprivileges.app/api/\"")
            buildConfigField("String", "BASE_URL_2", "\"https://api.cd.clubprivileges.app/api/\"")
            buildConfigField("String", "BASE_URL_3", "\"https://api.ci.clubprivileges.app/api/\"")
        }
        release {
            isMinifyEnabled = true
            buildConfigField("String", "BASE_URL_1", "\"https://api.clubprivileges.app/api/\"")
            buildConfigField("String", "BASE_URL_2", "\"https://api.cd.clubprivileges.app/api/\"")
            buildConfigField("String", "BASE_URL_3", "\"https://api.ci.clubprivileges.app/api/\"")
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures{
        buildConfig = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation ("com.airbnb.android:lottie:6.0.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation ("com.google.android.gms:play-services-auth-api-phone:18.1.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")



    implementation (project(":module"));

}
