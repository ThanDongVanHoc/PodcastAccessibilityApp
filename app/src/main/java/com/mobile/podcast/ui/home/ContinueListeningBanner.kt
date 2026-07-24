package com.mobile.podcast.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mobile.podcast.R

/**
 * "Continue listening" hero card. Demonstrates a merged text block sitting next to a genuinely
 * independent action (Resume) — the opposite trade-off to [PodcastCard]. Here the Resume button
 * KEEPS its own focus and label because it is the primary action, so we do NOT merge it away.
 */
@Composable
fun ContinueListeningBanner(
    episodeTitle: String,
    remaining: String,
    onResume: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Continue listening", style = MaterialTheme.typography.labelMedium)
                Text(episodeTitle, style = MaterialTheme.typography.titleMedium)
                Text("$remaining left", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(Modifier.width(12.dp))
            FilledIconButton(
                onClick = onResume,
                modifier = Modifier.size(56.dp) // comfortably above the 48dp minimum
            ) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = stringResource(R.string.cd_play)
                )
            }
        }
    }
}
