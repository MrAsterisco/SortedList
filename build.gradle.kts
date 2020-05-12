buildscript {
    repositories {
        mavenCentral()
        jcenter()
        maven { url = uri("https://dl.bintray.com/jetbrains/kotlin-native-dependencies") }
    }
    dependencies {
        classpath("com.moowork.gradle:gradle-node-plugin:+")
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4-jetbrains-5")
    }
}

plugins {
    kotlin("multiplatform") version("1.3.71")
    id("maven-publish")
}

repositories {
    mavenCentral()
}

group = project.property("group")!!
        version = project.property("version")!!

        kotlin {

            targets {

                jvm()
                js("js") {
                    browser {
                    }
                    nodejs {
                    }
                }

                ios()
                watchos()
                macosX64("macos")
            }

            sourceSets {
                val commonMain by getting {
                    dependencies {
                        implementation(kotlin("stdlib-common"))
                        implementation(kotlin("reflect"))
                    }
                }
                val commonTest by getting {
                    dependencies {
                        implementation(kotlin("test-common"))
                        implementation(kotlin("test-annotations-common"))
                    }
                }
                val jvmMain by getting {
                    dependencies {
                        implementation(kotlin("stdlib-jdk8"))
                    }
                }
                val jvmTest by getting {
                    dependencies {
                        implementation(kotlin("test"))
                        implementation(kotlin("test-junit"))
                    }
                }
                val jsMain by getting {
                    dependencies {
                        implementation(kotlin("stdlib-js"))
                    }
                }
                val jsTest by getting {
                    dependencies {
                        implementation(kotlin("test-js"))
                    }
                }
                val macosMain by getting { }
                val macosTest by getting { }
                val iosMain by getting { }
                val iosTest by getting { }
            }

        }

apply(from = rootProject.file("gradle/publish.gradle"))
apply(from = rootProject.file("gradle/node-js.gradle"))
apply(from = rootProject.file("gradle/test-mocha-js.gradle"))