package com.mobile.podcast.data.repository

import com.mobile.podcast.data.model.Episode
import com.mobile.podcast.data.model.Podcast
import kotlinx.coroutines.flow.StateFlow

/**
 * Front-end contract for podcast data.
 *
 * The UI depends ONLY on this interface, never on a concrete data source. A real app would swap in
 * a Room/Retrofit-backed implementation; this demo ships [MockPodcastRepository]. This is the
 * "interface / mock" boundary requested for the case study — there is no real backend.
 */
interface PodcastRepository {

    /** Observable catalogue of podcasts. Emits again whenever bookmark/download state changes. */
    val podcasts: StateFlow<List<Podcast>>

    fun getPodcast(id: String): Podcast?

    fun getEpisode(id: String): Episode?

    fun toggleBookmark(episodeId: String)

    fun toggleDownload(episodeId: String)
}
