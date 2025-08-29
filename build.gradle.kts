plugins {
    java
    id("com.gradleup.shadow") version "8.3.0"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.11"
}

group = "endcrypt.equinox"
version = "1.0.5"

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.extendedclip.com/releases/")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://repo.opencollab.dev/main/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.fancyinnovations.com/releases")
}

dependencies {
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.6")

    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.3.0")
    compileOnly("com.arcaniax:HeadDatabase-API:1.3.2")
    compileOnly("org.geysermc.floodgate:api:2.2.4-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")

    compileOnly("com.intellectualsites.plotsquared:plotsquared-core")
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core")
    compileOnly("de.oliver:FancyHolograms:2.7.0")

    implementation(platform("com.intellectualsites.bom:bom-newest:1.52"))

    implementation("de.tr7zw:item-nbt-api:2.14.1")
    implementation("com.samjakob:SpiGUI:1.3.1")
    implementation("dev.jorel:commandapi-bukkit-shade:10.0.0")

}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks.reobfJar {
    outputJar.set(file("D:/Damn/Minecraft Servers/Minecraft Server - 1.21.4 (Equinox)/plugins/${rootProject.name}-${version}.jar"))
}

tasks {
    shadowJar {
        relocate("de.tr7zw.changeme.nbtapi", "endcrypt.shaded.nbtapi")
        relocate("dev.jorel.commandapi", "endcrypt.shaded.commandapi")
    }
    assemble {
        dependsOn(reobfJar)
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name()

        options.release.set(21)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }

}