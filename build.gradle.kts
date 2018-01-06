plugins {
    kotlin("jvm") version "1.2.10"
    id("net.minecrell.licenser") version "0.3"
}

val gradleWrapperVersion: String by extra
val kotlinVersion: String by extra
val jtelegramVersion: String by extra
val configurateVersion: String by extra
val jrubyVersion: String by extra

configurations {
    create("shadow")

    getByName("compileOnly").extendsFrom(getByName("shadow"))
}

repositories {
    mavenLocal()
    mavenCentral()

    maven {
        name = "oss-sonatype"
        setUrl("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

dependencies {
    add("shadow", kotlin("stdlib-jdk8", kotlinVersion))
    add("shadow", "com.jtelegram:jtelegrambotapi-core:$jtelegramVersion")
    add("shadow", "ninja.leaping.configurate:configurate-hocon:$configurateVersion")
    add("shadow", "org.jruby:jruby-complete:$jrubyVersion")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
        languageVersion = "1.2"
    }
}

val jar by tasks.getting(Jar::class) {
    from(configurations["shadow"].map<File, Any> { if(it.isDirectory) it else zipTree(it) })
    manifest {
        attributes(mapOf(Pair("Main-Class", "eu.mikroskeem.neekerbot.NeekerBot")))
    }
}

license {
    header = rootProject.file("etc/HEADER")
    filter.include("**/*.kt")
}

val wrapper by tasks.creating(Wrapper::class) {
    gradleVersion = gradleWrapperVersion
    distributionUrl = "https://services.gradle.org/distributions/gradle-$gradleVersion-all.zip"
}

defaultTasks("licenseFormat", "build")