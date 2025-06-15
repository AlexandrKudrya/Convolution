plugins {
    java
    application
    id("me.champeau.jmh") version "0.7.2"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.boofcv:boofcv-core:1.1.2")
    implementation("org.boofcv:boofcv-io:1.1.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("net.jqwik:jqwik:1.8.4")
}

application {
    mainClass.set("com.example.convolution.Main")
}

tasks.test {
    useJUnitPlatform()

    maxHeapSize = "1G"

    testLogging {
        events("passed")
    }
}

jmh {}
