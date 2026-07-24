package com.mobile.podcast.data.repository

import androidx.compose.ui.graphics.Color
import com.mobile.podcast.data.model.Episode
import com.mobile.podcast.data.model.Podcast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * In-memory mock of [PodcastRepository]. All data is hard-coded; bookmark/download toggles mutate
 * the [MutableStateFlow] so the UI recomposes exactly as it would against a real reactive backend.
 *
 * A single shared instance is exposed via [MockPodcastRepository.instance] to keep the demo free of
 * a DI framework.
 */
class MockPodcastRepository : PodcastRepository {

    private val _podcasts = MutableStateFlow(seedData())
    override val podcasts: StateFlow<List<Podcast>> = _podcasts.asStateFlow()

    override fun getPodcast(id: String): Podcast? =
        _podcasts.value.firstOrNull { it.id == id }

    override fun getEpisode(id: String): Episode? =
        _podcasts.value.flatMap { it.episodes }.firstOrNull { it.id == id }

    override fun toggleBookmark(episodeId: String) = mutateEpisode(episodeId) {
        it.copy(isBookmarked = !it.isBookmarked)
    }

    override fun toggleDownload(episodeId: String) = mutateEpisode(episodeId) {
        it.copy(isDownloaded = !it.isDownloaded)
    }

    private fun mutateEpisode(episodeId: String, transform: (Episode) -> Episode) {
        _podcasts.value = _podcasts.value.map { podcast ->
            podcast.copy(
                episodes = podcast.episodes.map { ep ->
                    if (ep.id == episodeId) transform(ep) else ep
                }
            )
        }
    }

    companion object {
        /** Process-wide singleton so every screen observes the same state. */
        val instance: MockPodcastRepository by lazy { MockPodcastRepository() }

        private fun seedData(): List<Podcast> = listOf(
            Podcast(
                id = "p1",
                title = "The Quiet Hour",
                author = "Mai Anh Nguyen",
                category = "Mindfulness",
                description = "Short guided reflections to help you slow down and breathe. " +
                    "A calm companion for the end of a busy day.",
                coverColor = Color(0xFF4C6EF5),
                episodes = listOf(
                    Episode("e1", "p1", "Letting go of the day", "Jul 22", "18 min", "18 min", isNew = true),
                    Episode("e2", "p1", "Breathing in fours", "Jul 15", "12 min", "5 min"),
                    Episode("e3", "p1", "A body scan for sleep", "Jul 08", "24 min", "24 min", isDownloaded = true),
                )
            ),
            Podcast(
                id = "p2",
                title = "Built Different",
                author = "Trung Kien Le",
                category = "Technology",
                description = "Conversations with Android engineers about the craft of building " +
                    "delightful, inclusive mobile apps.",
                coverColor = Color(0xFF12B886),
                episodes = listOf(
                    Episode("e4", "p2", "Designing for TalkBack", "Jul 20", "52 min", "52 min", isNew = true, isBookmarked = true),
                    Episode("e5", "p2", "Compose semantics deep dive", "Jul 13", "47 min", "20 min"),
                    Episode("e6", "p2", "Shipping accessible by default", "Jul 06", "38 min", "38 min"),
                )
            ),
            Podcast(
                id = "p3",
                title = "Night Stories",
                author = "Lan Pham",
                category = "Audiobook",
                description = "Classic short fiction, read aloud. Perfect for listeners who prefer " +
                    "their books by ear.",
                coverColor = Color(0xFFF76707),
                episodes = listOf(
                    Episode("e7", "p3", "The Gift of the Magi", "Jul 21", "26 min", "26 min"),
                    Episode("e8", "p3", "A Cup of Tea", "Jul 14", "31 min", "9 min", isDownloaded = true),
                )
            ),
            Podcast(
                id = "p4",
                title = "Market Signals",
                author = "Hoang Vu",
                category = "Business",
                description = "A five-minute daily briefing on the numbers that moved, explained " +
                    "in plain language.",
                coverColor = Color(0xFFAE3EC9),
                episodes = listOf(
                    Episode("e9", "p4", "Why rates held steady", "Jul 22", "6 min", "6 min", isNew = true),
                    Episode("e10", "p4", "The week in three charts", "Jul 19", "8 min", "8 min"),
                )
            ),
        )
    }
}
