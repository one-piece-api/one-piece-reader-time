# one-piece-reader-time

The reader-axis rules of `one-piece-api` as a pure Java library: what a reader at chapter N or at episode N knows. Shared by `one-piece-content-service` and `one-piece-public-api`. Design: `docs/adr/0001-reader-time-library.md`.

## Model

| Type | Meaning |
|---|---|
| `Point` | A chapter and/or an episode, each part optional and independent. A missing part means "not seen on that axis". |
| `ReaderPosition` | The reader: an axis and a number. `hasReached(point)` compares on that axis only. |
| `RevealWindow` | `discovered` and an optional `ended`. State at a position: `HIDDEN`, `CURRENT`, `FORMER`. |
| `RevealedField<V>` | Current value, initial knowledge and reveals (`FieldReveal<V>`: a point and either a learned value or the current value). Fields revealed together are one record `V`. |
| `Windowed`, `GalleryImage` | Implemented by the services' list entries, paragraphs and images. |

## Resolution

- `Visibility`: an item from its first appearance; an entry when its window and its target are visible.
- `RevealedField.valueAt(position)`: the latest reveal reached on the axis, else the initial value, else unknown.
- `Paragraphs.visibleAt(...)`: discovered and not ended. `Paragraphs.current(...)`: without an end.
- `Gallery.mainPortraitAt(...)` and `Gallery.mainPortrait(...)`: the latest current `ASPETTO`.

## Checks on save

Pure functions returning violations. The service maps them to its error codes and field paths.

- `PointRules`: a window or a reveal against the first appearance (workflow §3.7.4).
- `PeriodRules`: the periods of a membership, ordered and not overlapping (§3.4.7).
- `RevealRules`: the reveals of one field (§3.7.1).
- `RevealWindow.overlapsOn(other)`: fruit users (§3.4.8).

## Using it

```kotlin
repositories {
    maven {
        url = uri("https://maven.pkg.github.com/one-piece-api/one-piece-reader-time")
        credentials {
            username = providers.gradleProperty("gpr.user").orElse(System.getenv("GITHUB_ACTOR")).get()
            password = providers.gradleProperty("gpr.token").orElse(System.getenv("GITHUB_TOKEN")).get()
        }
    }
}

dependencies {
    implementation("dev.onepieceapi:one-piece-reader-time:<version>")
}
```

GitHub Packages needs a token with `read:packages` even for public packages (see `one-piece-exception`'s README).

## Building and releasing

```bash
./gradlew check
```

Release: bump `version` in `build.gradle.kts`, commit, push tag `v<version>`. CI publishes on the tag only.
