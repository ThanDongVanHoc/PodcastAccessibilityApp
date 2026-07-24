package com.mobile.podcast.ui.player

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.mobile.podcast.R

/**
 * Custom circular play/pause control (like Spotify's).
 *
 * ACCESSIBILITY (core — Phong's chapter): a bare clickable [Box] tells assistive tech nothing.
 * We explicitly declare:
 *  - `role = Role.Button`      so TalkBack announces "Button".
 *  - `contentDescription`      the fixed name of the control ("Play" / "Pause").
 *  - `stateDescription`        the dynamic state ("Playing" / "Paused").
 * We `clearAndSetSemantics`-style override the inner Icon by passing null so the icon is not a
 * second focus stop. The 72dp size is far above the 48dp touch-target minimum.
 */
@Composable
fun PlayToggle(
    isPlaying: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val name = stringResource(if (isPlaying) R.string.cd_pause else R.string.cd_play)
    val state = stringResource(if (isPlaying) R.string.state_playing else R.string.state_paused)

    Box(
        modifier = modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
            .clickable(onClick = onToggle, role = Role.Button)
            .semantics {
                contentDescription = name
                stateDescription = state
                role = Role.Button
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
            contentDescription = null, // handled by the Box's semantics
            tint = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.size(36.dp)
        )
    }
}
