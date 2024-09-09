@Suppress("DSL_SCOPE_VIOLATION")

tasks.register<Delete>("clean").configure {
    delete(rootProject.layout.buildDirectory)
}
