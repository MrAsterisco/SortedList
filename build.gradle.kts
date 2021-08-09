import org.jetbrains.kotlin.konan.target.HostManager
import java.util.*

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("com.moowork.gradle:gradle-node-plugin:+")
    }
}

plugins {
    kotlin("multiplatform") version ("1.5.21")
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
        val macosMain by getting { }
        val macosTest by getting { }
        val iosMain by getting { }
        val iosTest by getting { }
    }

    plugins.withId("maven-publish") {
        configure<PublishingExtension> {
            val vcs: String by project
            val githubUser: String by project
            val githubRepository: String by project

            repositories {
                val local = Properties()
                val localProperties: File = rootProject.file("local.properties")
                if (localProperties.exists()) {
                    localProperties.inputStream().use { local.load(it) }
                }

                maven {
                    name = "GitHubPackages"
                    url = uri("https://maven.pkg.github.com/$githubUser/$githubRepository")
                    credentials {
                        username = local["githubUser"] as String?
                        password = local["githubToken"] as String?
                    }
                }
            }

            publications.withType<MavenPublication> {
                pom {
                    name.set(project.name)
                    description.set(project.description)
                    url.set(vcs)
                    licenses {
                        license {
                            name.set("MIT")
                            url.set("$vcs/blob/master/LICENCE.md")
                            distribution.set("repo")
                        }
                    }
                    developers {
                        developer {
                            id.set(githubUser)
                            name.set("Alessio Moiso")
                        }
                    }
                    scm {
                        connection.set("$vcs.git")
                        developerConnection.set("$vcs.git")
                        url.set(vcs)
                    }
                }
            }

            val taskPrefixes = when {
                HostManager.hostIsLinux -> listOf(
                    "publishLinux",
                    "publishJs",
                    "publishJvm",
                    "publishMetadata",
                    "publishKotlinMultiplatform"
                )
                HostManager.hostIsMac -> listOf("publishMacos", "publishIos")
                HostManager.hostIsMingw -> listOf("publishMingw")
                else -> error("Unknown host, abort publishing.")
            }

            val publishTasks = tasks.withType<PublishToMavenRepository>().matching { task ->
                taskPrefixes.any { task.name.startsWith(it) }
            }

            tasks.register("publishAll") { dependsOn(publishTasks) }
        }
    }
}