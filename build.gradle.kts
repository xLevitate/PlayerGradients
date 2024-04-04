plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "me.levitate"
version = "1.0.4"

repositories {
    mavenCentral()

    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://repo.mattstudios.me/artifactory/public/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.aikar.co/content/groups/aikar/")
    maven("https://repo.panda-lang.org/releases")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.3")

    compileOnly("org.projectlombok:lombok:1.18.28")
    annotationProcessor("org.projectlombok:lombok:1.18.28")

    implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT")
    implementation("dev.rollczi:litecommands-bukkit:3.2.0")

    implementation("fr.mrmicky:FastInv:3.0.4")
    implementation("dev.triumphteam:triumph-gui:3.1.2")
}

tasks.shadowJar {
    relocate("dev.triumphteam.gui", "me.levitate.gui")
}