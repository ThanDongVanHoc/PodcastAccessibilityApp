package com.mobile.podcast.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.mobile.podcast.R
import com.mobile.podcast.data.model.Episode

/**
 * A single episode row.
 *
 * ACCESSIBILITY: the row merges into one focus stop announcing "title, published, duration". The
 * three trailing actions (play / bookmark / download) are consolidated into `customActions`; the
 * physical icon buttons are cleared from the tree but keep 48dp touch targets. A "New" badge is
 * decorative-looking but IS meaningful, so it stays in the merged announcement as text.
 */
@Composable
fun EpisodeRow(
    episode: Episode,
    onPlay: () -> Unit,
    onToggleBookmark: () -> Unit,
    onToggleDownload: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bookmarkLabel = stringResource(
        if (episode.isBookmarked) R.string.action_remove_bookmark else R.string.action_bookmark
    )
    val downloadLabel = stringResource(R.string.action_download)
    val playLabel = stringResource(R.string.action_play_episode)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .semantics(mergeDescendants = true) {
                customActions = listOf(
                    CustomAccessibilityAction(playLabel) { onPlay(); true },
                    CustomAccessibilityAction(bookmarkLabel) { onToggleBookmark(); true },
                    CustomAccessibilityAction(downloadLabel) { onToggleDownload(); true },
                )
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Primary action: play. Large, high-contrast, its own touch target.
        FilledIconButton(
            onClick = onPlay,
            modifier = Modifier
                .size(48.dp)
                .clearAndSetSemantics {} // action already exposed on the row
        ) {
            Icon(Icons.Filled.PlayArrow, contentDescription = null)
        }
        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (episode.isNew) {
                    NewBadge()
                    Spacer(Modifier.width(6.dp))
                }
                Text(episode.title, style = MaterialTheme.typography.titleMedium)
            }
            Text(
                stringResource(
                    R.string.episode_meta,
                    episode.publishedLabel,
                    episode.durationLabel,
                    episode.remainingLabel
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        IconButton(
            onClick = onToggleBookmark,
            modifier = Modifier.minimumInteractiveComponentSize().clearAndSetSemantics {}
        ) {
            Icon(
                imageVector = if (episode.isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                contentDescription = null
            )
        }
        IconButton(
            onClick = onToggleDownload,
            modifier = Modifier.minimumInteractiveComponentSize().clearAndSetSemantics {}
        ) {
            Icon(
                imageVector = if (episode.isDownloaded) Icons.Filled.Check else Icons.Filled.Download,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun NewBadge() {
    // Decorative-styled but semantically meaningful: the word "New" is read as part of the row.
    Surface(
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        shape = CircleShape
    ) {
        Text(
            text = stringResource(R.string.label_new),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}
