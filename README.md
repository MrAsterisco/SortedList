# SortedList
SortedList is an implementation of a sorted mutable list based on an [AVL tree](https://en.wikipedia.org/wiki/AVL_tree) that supports adding, iterating and removing items.

SortedList is a Kotlin multiplatform library that provides a generic implementation compatible with all platforms.

## Usage
### Showcase

```kotlin
// Create a new sorted mutable list.
sortedMutableListOf(1, 2, 5, 6)

// Create a new sorted list.
sortedList("A", "C", "B")

// Create a new sorted mutable list using a custom comparator.
sortedMutableListOf(compareBy { it.length }, "B", "AAA")

// Convert a normal list into a sorted list.
listOf(5, 4, 6, 2).toSortedList()
```

## Installation
Add the GitHub registry to your `build.gradle`:

```kotlin
allprojects {
 repositories {
     maven {
         url = uri("https://maven.pkg.github.com/mrasterisco/sortedlist")
         credentials {
             username = <YOUR GITHUB USERNAME>
             password = <YOUR GITHUB PERSONAL ACCESS TOKEN>
         }
     }
 }
}
```

GitHub doesn't support accessing packages without authentication ([more info](https://github.community/t/download-from-github-package-registry-without-authentication/14407/96)). You can generate a personal access token for your account [here](https://github.com/settings/tokens).

Since version 1.3.0, SortedList can be imported as dependency by just referencing it in the `commonMain` module:

```kotlin
val commonMain by getting {
    dependencies {
        implementation("io.github.mrasterisco:sortedlist:<version>")
    }
}
```

### Compatibility

The library uses only Kotlin common code and does not provide explicit implementations for any platform, hence it should work out-of-the-box everywhere. See the table below for further details:

|                      |      iOS     |    macOS    |     JVM     |  nodeJS  | browserJS | Windows |  Linux  |
|:--------------------:|:------------:|:-----------:|:-----------:|:--------:|:---------:|:-------:|:-------:|
|  Built using Gradle  |      YES     |     YES     |     YES     |    Removed in 1.3.0   |    Removed in 1.3.0    |    NO   |    NO   |
|      Unit Tests      |  YES, passed | YES, passed | YES, passed |  Not run |  Not run  | Not run | Not run |
| Published to GitHub Packages | YES, -native | YES, -macos |  YES        | NO |  NO |    NO   |    NO   |
|  Used in Production  |      YES     |      NO     |     YES     |    NO    |     NO    |    NO   |    NO   |

## Contributing
All contributions to expand the library are welcome. Fork the repo, make the changes you want, and open a Pull Request.

If you make changes to the codebase, I am not enforcing a coding style, but I may ask you to make changes based on how the rest of the library is made.

## Status
This library is under **active development**, as some of the more specific features are not yet implemented. For all the implemented features, you can expect stability, and they can be used in a Production app.

Even if most of the APIs are pretty straightforward, they may change in the future; but you don't have to worry about that, because releases will follow [Semanting Versioning 2.0.0](https://semver.org/).

## Credits
The AVL tree implementation is an improved and expanded version of the original implementation provided by [Péter Gulyás](https://gitlab.com/gulyaspeter) [here](https://gitlab.com/gulyaspeter/the-advent-of-kotlin-2018-week-3). Most of the original code has been refactored, but it retains the original essence.

The basic batch of unit tests are those provided by [Kt. Academy](https://blog.kotlin-academy.com) in their [Advent of Kotlin, week 3](https://blog.kotlin-academy.com/advent-of-kotlin-week-3-sortedlist-2ff49c250aad) article.

## License
SortedList is distributed under the MIT license. [See LICENSE](https://github.com/MrAsterisco/SortedList/blob/master/LICENSE) for details.