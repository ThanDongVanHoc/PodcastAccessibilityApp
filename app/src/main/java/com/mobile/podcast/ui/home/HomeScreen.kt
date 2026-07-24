package com.mobile.podcast.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mobile.podcast.R
import com.mobile.podcast.data.repository.MockPodcastRepository

/**
 * Discover screen — the entry point of the user flow.
 *
 * ACCESSIBILITY highlights:
 *  - The app-bar title carries `heading()` so TalkBack users can jump between headings.
 *  - `Continue listening` is visually below the bar but should be announced FIRST. We wrap the whole
 *    screen in `isTraversalGroup = true` and give that banner the lowest `traversalIndex`, so it wins
 *    the reading order without moving visually. (See the report section on traversal order.)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenPodcast: (String) -> Unit,
    onPlayEpisode: (String) -> Unit,
) {
    val repo = MockPodcastRepository.instance
    val podcasts by repo.podcasts.collectAsStateWithLifecycle()
    val resumeEpisode = podcasts.getOrNull(1)?.episodes?.getOrNull(1) // "Compose semantics deep dive"

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.title_home),
                        modifier = Modifier.semantics { heading() }
                    )
                },
                actions = {
                    IconButton(onClick = { /* search flow omitted in demo */ }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = stringResource(R.string.cd_search)
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
                .semantics { isTraversalGroup = true },
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (resumeEpisode != null) {
                item {
                    ContinueListeningBanner(
                        episodeTitle = resumeEpisode.title,
                        remaining = resumeEpisode.remainingLabel,
                        onResume = { onPlayEpisode(resumeEpisode.id) },
                        // Lowest index in the traversal group => read before everything else.
                        modifier = Modifier.semantics { traversalIndex = -1f }
                    )
                }
            }

            item {
                Text(
                    text = "Fresh for you",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 4.dp)
                        .semantics { heading() }
                )
            }

            items(podcasts, key = { it.id }) { podcast ->
                PodcastCard(
                    podcast = podcast,
                    onOpen = { onOpenPodcast(podcast.id) },
                    onToggleBookmark = { repo.toggleBookmark(it.id) }
                )
            }
        }
    }
}
