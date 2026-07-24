package com.mobile.podcast.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobile.podcast.R
import com.mobile.podcast.data.repository.MockPodcastRepository
import com.mobile.podcast.ui.components.CoverArt

/**
 * Podcast detail: header (cover + title + author + description) followed by the episode list.
 *
 * ACCESSIBILITY:
 *  - The header is grouped with `isTraversalGroup = true` so TalkBack reads the whole show summary
 *    before diving into episodes, regardless of exact pixel geometry.
 *  - "Episodes" is a `heading()` so users can navigate by heading.
 *  - The back button has a succinct, localized contentDescription.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PodcastDetailScreen(
    podcastId: String,
    onBack: () -> Unit,
    onPlayEpisode: (String) -> Unit,
) {
    val repo = MockPodcastRepository.instance
    // Observe so bookmark/download toggles inside rows recompose the list.
    val podcasts by repo.podcasts.collectAsStateWithLifecycle()
    val podcast = podcasts.firstOrNull { it.id == podcastId } ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(podcast.title, modifier = Modifier.semantics { heading() }) },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            item {
                // Header summary read as one coherent block.
                Column(modifier = Modifier.semantics(mergeDescendants = true) { isTraversalGroup = true }) {
                    Spacer(Modifier.height(8.dp))
                    CoverArt(
                        title = podcast.title,
                        color = podcast.coverColor,
                        size = 120.dp,
                        decorative = false, // large hero image: give it a real description
                        modifier = Modifier.semantics {}
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(podcast.title, style = MaterialTheme.typography.headlineMedium)
                    Text(
                        "${podcast.author} • ${podcast.category}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(podcast.description, style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(16.dp))
                }
            }

            item {
                Text(
                    text = stringResource(R.string.label_episodes),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .semantics { heading() }
                )
            }

            items(podcast.episodes, key = { it.id }) { episode ->
                EpisodeRow(
                    episode = episode,
                    onPlay = { onPlayEpisode(episode.id) },
                    onToggleBookmark = { repo.toggleBookmark(episode.id) },
                    onToggleDownload = { repo.toggleDownload(episode.id) }
                )
                Divider(color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.12f))
            }
        }
    }
}
