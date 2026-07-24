package com.mobile.podcast.ui.player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Forward30
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mobile.podcast.R
import com.mobile.podcast.data.repository.MockPodcastRepository
import com.mobile.podcast.data.repository.PlayerStateHolder
import com.mobile.podcast.ui.components.CoverArt

/**
 * Now-Playing screen — the end of the user flow.
 *
 * ACCESSIBILITY:
 *  - A `liveRegion` line announces "Now playing <title>" / "Paused" automatically when the state
 *    flips, so blind users get feedback without hunting for the button. (Phat's LiveRegion concept.)
 *  - The transport row (rewind / play / forward) uses generously sized, individually labelled
 *    targets.
 *  - The speed control announces its current value via contentDescription.
 *  - The seek Slider is natively accessible; we add a spoken position label.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    episodeId: String,
    onBack: () -> Unit,
) {
    val repo = MockPodcastRepository.instance
    val player = remember { PlayerStateHolder.instance }
    val episode = repo.getEpisode(episodeId) ?: return
    val podcast = repo.getPodcast(episode.podcastId)

    // Start playback the first time we land here (side effect, not during composition).
    LaunchedEffect(episodeId) { player.play(episode) }

    val liveText = if (player.isPlaying) {
        stringResource(R.string.live_now_playing, episode.title)
    } else {
        stringResource(R.string.state_paused)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.title_now_playing),
                        modifier = Modifier.semantics { heading() }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(Modifier.height(16.dp))
            CoverArt(
                title = podcast?.title ?: episode.title,
                color = podcast?.coverColor ?: MaterialTheme.colorScheme.primary,
                size = 220.dp,
                decorative = true // title is shown as text right below
            )
            Spacer(Modifier.height(24.dp))

            Text(
                episode.title,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
            Text(
                podcast?.author.orEmpty(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Assertive live region: announced as soon as play state changes.
            Text(
                text = liveText,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .semantics { liveRegion = LiveRegionMode.Assertive }
            )

            Spacer(Modifier.height(24.dp))
            SeekBar(
                progress = player.progress,
                remainingLabel = episode.remainingLabel,
                onSeek = player::seekTo
            )

            Spacer(Modifier.height(24.dp))
            TransportControls(
                isPlaying = player.isPlaying,
                onRewind = { player.seekTo(player.progress - 0.05f) },
                onToggle = player::togglePlayPause,
                onForward = { player.seekTo(player.progress + 0.05f) }
            )

            Spacer(Modifier.height(24.dp))
            SpeedButton(
                speedLabel = "${player.speed}x",
                onClick = player::cycleSpeed
            )
        }
    }
}

@Composable
private fun SeekBar(progress: Float, remainingLabel: String, onSeek: (Float) -> Unit) {
    val positionLabel = "$remainingLabel remaining"
    Column(modifier = Modifier.fillMaxWidth()) {
        Slider(
            value = progress,
            onValueChange = onSeek,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { contentDescription = positionLabel }
        )
        Text(
            text = positionLabel,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TransportControls(
    isPlaying: Boolean,
    onRewind: () -> Unit,
    onToggle: () -> Unit,
    onForward: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onRewind, modifier = Modifier.size(56.dp)) {
            Icon(
                Icons.Filled.Replay,
                contentDescription = stringResource(R.string.cd_skip_back),
                modifier = Modifier.size(32.dp)
            )
        }
        PlayToggle(isPlaying = isPlaying, onToggle = onToggle)
        IconButton(onClick = onForward, modifier = Modifier.size(56.dp)) {
            Icon(
                Icons.Filled.Forward30,
                contentDescription = stringResource(R.string.cd_skip_forward),
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun SpeedButton(speedLabel: String, onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.semantics {
            contentDescription = "Playback speed, currently $speedLabel"
        }
    ) {
        Icon(Icons.Filled.Speed, contentDescription = null)
        Spacer(Modifier.size(8.dp))
        Text(speedLabel)
    }
}
