import me.daemon.gradle.PublishInfo
import me.daemon.gradle.PublishInfo.Pom
import me.daemon.gradle.PublishInfo.Pom.Scm

plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    id("me.daemon.gradle")
    id("maven-publish")
    id("signing")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

val publishInfo = PublishInfo(
    artifactId = "annotation",
    artifactVersion = "1.0.0",
    pom = Pom(
        name = "annotation",
        description = "Annotation Library",
        url = "https://github.com/daemon369/annotation",
        scm = Scm(
            connection = "scm:git:git://github.com/daemon369/annotation.git",
            developerConnection = "scm:git:ssh://github.com/daemon369/annotation.git",
            url = "https://github.com/daemon369/annotation/tree/main",
        )
    )
)

val artifactGroupId:String by project

afterEvaluate {
    publishing {

        publications {
            repositories {
                maven {
                    url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")

                    credentials {
                        username = project.extra["ossrhUsername"]!!.toString()
                        password = project.extra["ossrhPassword"]!!.toString()
                    }
                }
            }

            create<MavenPublication>("release") {
                groupId = artifactGroupId
                artifactId = publishInfo.artifactId
                version = publishInfo.artifactVersion
                if (plugins.findPlugin("com.android.library") != null) {
                    from(components["release"])
                } else {
                    from(components["java"])
                }

                pom {
                    name.set(publishInfo.pom.name)
                    description.set(publishInfo.pom.description)
                    url.set(publishInfo.pom.url)
                    licenses {
                        license {
                            name.set("The Apache Software License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("daemon")
                            name.set("Daemon")
                            email.set("daemon336699@gmail.com")
                        }
                    }
                    scm {
                        connection.set(publishInfo.pom.scm.connection)
                        developerConnection.set(publishInfo.pom.scm.developerConnection)
                        url.set(publishInfo.pom.scm.url)
                    }
                }
            }
        }
    }
}

signing {
    sign(publishing.publications)
}