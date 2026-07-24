package com.mobile.podcast.data.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.mobile.podcast.data.model.Episode

/**
 * A tiny mock "player". No real audio engine — it only holds enough observable state for the UI to
 * demonstrate accessible playback controls (play/pause state descriptions, live-region
 * announcements, seek progress).
 */
class PlayerStateHolder {

    var currentEpisode by mutableStateOf<Episode?>(null)
        private set

    var isPlaying by mutableStateOf(false)
        private set

    /** 0f..1f progress through the current episode. */
    var progress by mutableStateOf(0.35f)
        private set

    /** Playback speed multiplier, e.g. 1.0f, 1.5f, 2.0f. */
    var speed by mutableStateOf(1.0f)
        private set

    fun play(episode: Episode) {
        currentEpisode = episode
        isPlaying = true
    }

    fun togglePlayPause() {
        if (currentEpisode != null) isPlaying = !isPlaying
    }

    fun seekTo(newProgress: Float) {
        progress = newProgress.coerceIn(0f, 1f)
    }

    fun cycleSpeed() {
        speed = when (speed) {
            1.0f -> 1.5f
            1.5f -> 2.0f
            else -> 1.0f
        }
    }

    companion object {
        val instance: PlayerStateHolder by lazy { PlayerStateHolder() }
    }
}
