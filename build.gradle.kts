plugins {
    java
    application
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // BoofCV основная библиотека
    implementation("org.boofcv:boofcv-core:1.1.2")
    // Для загрузки и сохранения изображений (javax.imageio)
    implementation("org.boofcv:boofcv-io:1.1.2")
}

application {
    mainClass.set("com.example.convolution.Main")
}