# KSP Circular Dependency Issue

This project demonstrates the core of an issue we're having in our own real project.

* We have a KSP processor that generates a resource file (here, `":ksp:processor"`).
* We have a Gradle task which depends on the generated resource file in order to
  create some Kotlin source (here, `":app"`'s `build.gradle.kts` file which defines `generateCodeTask`)

## Reproducing the issue

To see the error for yourself, just run `./gradlew :app:run` in the root directory.

The error will be something like this:
```
* Exception is:
org.gradle.api.CircularReferenceException: Circular dependency between the following tasks:
:app:generateCodeTask
\--- :app:kspKotlin
     \--- :app:generateCodeTask
```

## Points of interest

### `GreetingResourceSymbolProcessor.kt`

Creates a `Greeting.txt` file in
`/app/build/generated/ksp/main/resources/org/example/ksp/`

---

### `/app/build.gradle.kts`

Defines a task `generateCodeTask` which depends on the generated resource file.

Note that we have to do a strange invocation to pull the resource out of the KSP task:

```kotlin
val inputFile = tasks.named("kspKotlin").map { kspTask ->
    RegularFile {
        kspTask.outputs.files.asFileTree.matching { include("**/Greeting.txt") }.singleFile
    }
}
```

Maybe there's a better way, but this was the best we could find after hours of experimentation.

## Current Fix

To fix the issue, search the `build.gradle.kts` file for the string "To fix". Basically you need to comment out one way
and uncomment another. This solution came from our Slack discussion, but it will be going away in K2:

```kotlin
// Before
kotlin {
    sourceSets.main {
        kotlin.srcDir(generateCodeTask)
    }
}

// After
tasks.named<KotlinJvmCompile>("compileKotlin") {
    source(generateCodeTask)
}
```
