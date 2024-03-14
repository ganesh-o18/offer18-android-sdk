plugins {
    id("com.android.library")
    id ("io.sentry.android.gradle") version "4.3.1"
    id("maven-publish")
}

android {
    namespace = "com.offer18.sdk"
    compileSdk = 34

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/OWNER/REPOSITORY")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("ReleaseAar") {
            groupId = "com.offer18"
            artifactId = "offer18-android-sdk"
            version = "0.0.1-SNAPSHOT"
            afterEvaluate {
                artifact(tasks.getByName("bundleReleaseAar"))
            }
        }
    }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
}

tasks.register<Zip>("generateRepo") {
    val publishTask = tasks.named(
            "publishReleasePublicationToMyrepoRepository",
            PublishToMavenRepository::class.java)
    from(publishTask.map { it.repository.url })
    into("mylibrary")
    archiveFileName.set("mylibrary.zip")
}