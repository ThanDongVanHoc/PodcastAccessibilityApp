package com.mobile.podcast.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.mobile.podcast.data.model.Podcast
import com.mobile.podcast.ui.components.CoverArt

/**
 * A podcast row on the Discover screen.
 *
 * ACCESSIBILITY (advanced semantics):
 *  - The whole card is ONE focus stop. `mergeDescendants = true` collapses cover + title + author
 *    + metadata into a single TalkBack utterance instead of 4-5 separate swipes.
 *  - The nested bookmark and overflow icon buttons would each steal focus and fail as tiny targets.
 *    We `clearAndSetSemantics {}` them so TalkBack skips them for touch users, and re-expose their
 *    actions as `customActions` on the card — reachable from the TalkBack actions menu.
 *  - `minimumInteractiveComponentSize()` still guarantees a 48dp touch target for sighted users.
 */
@Composable
fun PodcastCard(
    podcast: Podcast,
    onOpen: () -> Unit,
    onToggleBookmark: (Episode) -> Unit,
    modifier: Modifier = Modifier,
) {
    // "Bookmark the latest episode" is the card-level shortcut we surface to TalkBack.
    val latest = podcast.episodes.first()
    val bookmarkLabel = stringResource(
        if (latest.isBookmarked) R.string.action_remove_bookmark else R.string.action_bookmark
    )
    val openLabel = stringResource(R.string.action_play_episode)

    Card(
        onClick = onOpen,
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                customActions = listOf(
                    CustomAccessibilityAction(bookmarkLabel) { onToggleBookmark(latest); true },
                )
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CoverArt(title = podcast.title, color = podcast.coverColor)
            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(podcast.title, style = MaterialTheme.typography.titleMedium)
                Text(
                    podcast.author,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "${podcast.category} • ${podcast.episodes.size} episodes",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Nested control #1 — bookmark. Cleared from the semantics tree (handled by customAction).
            IconButton(
                onClick = { onToggleBookmark(latest) },
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .clearAndSetSemantics {}
            ) {
                Icon(
                    imageVector = if (latest.isBookmarked) Icons.Filled.Bookmark else Icons.Filled.BookmarkBorder,
                    contentDescription = null
                )
            }

            // Nested control #2 — overflow. Also cleared; a real menu would live in customActions too.
            IconButton(
                onClick = onOpen,
                modifier = Modifier
                    .minimumInteractiveComponentSize()
                    .clearAndSetSemantics {}
            ) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
            }
        }
    }
}
