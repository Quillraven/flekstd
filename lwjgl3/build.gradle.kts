import io.github.fourlastor.construo.Target

plugins {
    buildsrc.convention.`kotlin-jvm`
    application
    alias(libs.plugins.construoGradlePlugin)
}

dependencies {
    implementation(libs.gdxBackendLwjgl3)
    implementation(project(":core"))
    listOf(
        libs.gdxPlatform,
        libs.gdxFreetypePlatform,
    ).forEach {
        runtimeOnly(it) {
            artifact { classifier = "natives-desktop" }
        }
    }
}

group = "io.github.quillraven.flekstd"
version = "1.0"

application {
    applicationName = "flekstd"
    mainClass = "io.github.quillraven.flekstd.Lwjgl3LauncherKt"
}

tasks {
    jar {
        archiveBaseName.set(application.applicationName)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE

        from(files(sourceSets.main.get().output.classesDirs))
        from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })

        manifest {
            attributes["Main-Class"] = application.mainClass
        }
    }
}

construo {
    name.set(application.applicationName)
    // human-readable name, used for example in the `.app` name for macOS
    humanName.set(application.applicationName)

    targets {
        create<Target.Linux>("linuxX64") {
            architecture.set(Target.Architecture.X86_64)
            jdkUrl.set("https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.19%2B10/OpenJDK17U-jdk_x64_linux_hotspot_17.0.19_10.tar.gz")
        }
        create<Target.Windows>("winX64") {
            architecture.set(Target.Architecture.X86_64)
            jdkUrl.set("https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.19%2B10/OpenJDK17U-jdk_x64_windows_hotspot_17.0.19_10.zip")
        }
    }
}
