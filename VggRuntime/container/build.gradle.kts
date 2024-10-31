@file:Suppress("UnstableApiUsage")

@Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("maven-publish")
}

android {
    namespace = "com.verygoodgraphics.android.container"
    ndkVersion = "27.0.12077973"

    defaultConfig {
        minSdk = 26
        compileSdk = 34
        compileSdkVersion = "android-34"

        externalNativeBuild {
            cmake {
                // cmake flags
                arguments.addAll(
                    arrayOf(
                        "-DANDROID_SUPPORT_FLEXIBLE_PAGE_SIZES=ON",
                        "-DANDROID_STL=c++_shared",
                    )
                )
                // cxx flags
                val flags = arrayOf(
                    "-Qunused-arguments",
                    "-fvisibility=protected",
                    "-fvisibility-inlines-hidden",
                    "-fno-omit-frame-pointer",
                )
                cppFlags(*flags)
                targets += "vgg_android"
            }
        }

        ndk.debugSymbolLevel = "FULL"

        ndk.abiFilters.addAll(listOf("x86_64", "arm64-v8a"))
    }

    externalNativeBuild {
        cmake {
            path = File(projectDir, "src/main/cpp/CMakeLists.txt")
        }
    }

    sourceSets {
        val main by getting
        main.jniLibs.srcDirs("jniLibs")
    }

    // for jni debug
    buildTypes {
        getByName("debug") {
            isJniDebuggable = true
        }
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    compileOnly(libs.androidx.annotation)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

// task copy "../external/$abi/lib/*.so" to "jniLibs/$abi/"
tasks.register("copyLibs") {
    val abiList = arrayOf("x86_64", "arm64-v8a", "armeabi-v7a", "x86", "riscv64")
    // declare input and output
    inputs.files(abiList.map { file("../external/$it/lib") })
    outputs.dir(file("jniLibs"))

    doLast {
        abiList.forEach { abi ->
            val srcDir = file("../external/$abi/lib")
            val destDir = file("jniLibs/$abi")
            if (srcDir.exists()) {
                destDir.mkdirs()
                // filter *.so
                srcDir.listFiles { _, name -> name.endsWith(".so") }?.forEach {
                    it.copyTo(File(destDir, it.name), overwrite = true)
                }
            }
        }
    }
}

// run before build
tasks.getByName("preBuild").dependsOn("copyLibs")
