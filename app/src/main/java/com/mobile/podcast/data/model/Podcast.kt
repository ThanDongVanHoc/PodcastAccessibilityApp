package com.mobile.podcast.data.model

import androidx.compose.ui.graphics.Color

/**
 * A podcast / audiobook series. Pure UI model — no network or database types leak in here,
 * which keeps the front end decoupled from any future backend implementation.
 */
data class Podcast(
    val id: String,
    val title: String,
    val author: String,
    val category: String,
    val description: String,
    /** Placeholder cover colour used instead of a real image asset in this front-end-only demo. */
    val coverColor: Color,
    val episodes: List<Episode>
)
