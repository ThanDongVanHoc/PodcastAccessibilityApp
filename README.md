# Accessible Podcast — Case Study App

A front-end-only Jetpack Compose demo for the report *"Building Accessible Android Applications"*.
It implements a three-screen podcast / audiobook user flow and showcases every accessibility
technique researched by the group. **There is no backend** — all data comes from an in-memory mock
behind a repository interface.

## User flow

```
Discover (Home)  ──tap card──▶  Podcast detail  ──tap episode──▶  Now Playing (Player)
       │                                                              ▲
       └──────────────── "Continue listening" ────────────────────────┘
```

## How to run

1. Open the `PodcastAccessibilityApp` folder in **Android Studio (Koala or newer)**.
2. Let it sync — Android Studio will generate the Gradle wrapper and `local.properties` (SDK path).
3. Run the `app` configuration on an emulator or device (min SDK 24, target SDK 34).
4. To experience the accessibility work, enable **TalkBack** (Settings ▸ Accessibility) and/or
   **Accessibility Scanner**, and set **Font size** to 200%.

> Gradle wrapper binaries (`gradlew`, `gradle-wrapper.jar`) are intentionally omitted; Android
> Studio recreates them. From the CLI you can run `gradle wrapper` once, then `./gradlew assembleDebug`.

## Where each technique lives (maps to the report)

| Report section | Technique | File |
|---|---|---|
| Core / Phong | `contentDescription`, decorative `null` | `ui/components/CoverArt.kt`, `strings.xml` |
| Core / Phong | `Role.Button` + `stateDescription` on a custom control | `ui/player/PlayToggle.kt` |
| Core / Phong | 48dp touch targets (`minimumInteractiveComponentSize`) | `ui/home/PodcastCard.kt`, `ui/detail/EpisodeRow.kt` |
| Core / Phong | `sp` typography + non-linear font scaling | `ui/theme/Type.kt` |
| Core / Phong | heading navigation (`heading()`) | `HomeScreen.kt`, `PodcastDetailScreen.kt` |
| Advanced / Khuong | `mergeDescendants` | `PodcastCard.kt`, `EpisodeRow.kt` |
| Advanced / Khuong | `clearAndSetSemantics` + `customActions` | `PodcastCard.kt`, `EpisodeRow.kt` |
| Advanced / Khuong | `hideFromAccessibility` (decorative cover) | `CoverArt.kt` |
| Advanced / Khuong | `isTraversalGroup` + `traversalIndex` | `HomeScreen.kt`, `PodcastDetailScreen.kt` |
| Foundation / Phat | `liveRegion` playback announcements | `ui/player/PlayerScreen.kt` |
| Testing / Tinh | Compose semantics assertions + `printToLog`, ATF | `androidTest/.../AccessibilityTest.kt` |

## Package layout

```
com.mobile.podcast
├── MainActivity.kt
├── data
│   ├── model            Podcast, Episode
│   └── repository       PodcastRepository (interface), MockPodcastRepository, PlayerStateHolder
└── ui
    ├── theme            Color, Type (sp), Theme (M3, contrast-checked)
    ├── navigation       PodcastNavHost (Discover → Detail → Player)
    ├── components       CoverArt
    ├── home             HomeScreen, PodcastCard, ContinueListeningBanner
    ├── detail           PodcastDetailScreen, EpisodeRow
    └── player           PlayerScreen, PlayToggle
```
