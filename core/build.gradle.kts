plugins {
    buildsrc.convention.`kotlin-jvm`
}

sourceSets {
    main {
        // assets folder contains the resources of the game
        resources.srcDir(rootProject.files("assets"))
    }
}

dependencies {
    api(libs.gdx) // requires API to correctly expose Disposable for launcher classes (TeaVM)
    api(libs.bundles.ktxBaseBundle) // requires API to correctly expose ApplicationListener for launcher classes
    implementation(libs.bundles.freetypeBundle)
    implementation(libs.fleks)

    testImplementation(kotlin("test"))
}
