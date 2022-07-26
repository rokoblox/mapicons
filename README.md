# pinlib
![Modrinth](https://img.shields.io/modrinth/dt/pinlib?label=modrinth&color=green&style=for-the-badge)
![GitHub release (latest by SemVer including pre-releases)](https://img.shields.io/github/downloads/rokoblox/pinlib/latest/total?color=aaaaff&label=latest%20release&sort=semver&style=for-the-badge)
![GitHub release (latest by SemVer including pre-releases)](https://img.shields.io/github/downloads-pre/rokoblox/pinlib/latest/total?color=6666bb&label=latest%20pre-release&sort=semver&style=for-the-badge)

A lightweight minecraft library to add map icons and markers for fabric mod loader.

## How to add to your build.gradle

You can add the mod to your build.gradle file with 2 ways.

### [Modrinth](https://modrinth.com)

```gradle
repositories {
    // Your repositories...
    maven {
        name = "Modrinth"
        url = "https://api.modrinth.com/maven"
        content {
            includeGroup "maven.modrinth"
        }
    }
}

dependencies {
    // Your dependencies....
    modApi "maven.modrinth:pinlib:<VERSION>"
}
```
See [dependency scope](https://docs.modrinth.com/docs/tutorials/maven/#dependency-scope) to know the difference between `modImplementation`, `modApi` and `include`.

### [JitPack](https://jitpack.io/)

```gradle
repositories {
    // Your repositories...
    maven { url 'https://jitpack.io' }
}

dependencies {
    // Your dependencies....
    implementation 'com.github.rokoblox:pinlib:latestdev' // or specific version.
}
```

Happy coding!
