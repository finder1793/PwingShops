plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.pwing"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://repo.pwing.site/repository/maven-public/")
    maven("https://repo.skriptlang.org/releases")
    maven("https://repo.oraxen.com/releases/")
    maven("https://repo.nexomc.com/releases/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.6-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly(files("libs/PwingEco-1.1.1.jar"))
    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("com.github.SkriptLang:Skript:2.6.4")
    compileOnly("com.github.LoneDev6:API-ItemsAdder:3.5.0b")
    compileOnly("io.th0rgal:oraxen:1.185.0")
    compileOnly("com.nexomc:nexo:0.7.0")
    implementation("net.kyori:adventure-api:4.14.0")
    implementation("net.kyori:adventure-text-minimessage:4.14.0")
    implementation("net.kyori:adventure-text-serializer-legacy:4.14.0")
}
tasks {
    shadowJar {
        archiveClassifier.set("")
        minimize()
    }
    
    processResources {
        filesMatching("plugin.yml") {
            expand(
                "version" to project.version
            )
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(21)
    }
}

