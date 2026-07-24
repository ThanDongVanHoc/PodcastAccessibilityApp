package com.mobile.podcast.data.model

/**
 * A single episode inside a [Podcast].
 *
 * [durationLabel] and [remainingLabel] are pre-formatted, human-readable strings so the UI layer
 * never has to do locale-sensitive time formatting inline (which also keeps TalkBack output clean).
 */
data class Episode(
    val id: String,
    val podcastId: String,
    val title: String,
    val publishedLabel: String,   // e.g. "Jul 20"
    val durationLabel: String,    // e.g. "42 min"
    val remainingLabel: String,   // e.g. "18 min"
    val isNew: Boolean = false,
    val isBookmarked: Boolean = false,
    val isDownloaded: Boolean = false
)
