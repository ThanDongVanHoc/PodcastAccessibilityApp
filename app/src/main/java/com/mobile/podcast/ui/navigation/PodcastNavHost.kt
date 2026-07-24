package com.mobile.podcast.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mobile.podcast.ui.detail.PodcastDetailScreen
import com.mobile.podcast.ui.home.HomeScreen
import com.mobile.podcast.ui.player.PlayerScreen

/** Route constants for the three-screen user flow: Discover -> Detail -> Player. */
object Routes {
    const val HOME = "home"
    const val DETAIL = "detail/{podcastId}"
    const val PLAYER = "player/{episodeId}"

    fun detail(podcastId: String) = "detail/$podcastId"
    fun player(episodeId: String) = "player/$episodeId"
}

@Composable
fun PodcastNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.HOME) {

        composable(Routes.HOME) {
            HomeScreen(
                onOpenPodcast = { podcastId -> navController.navigate(Routes.detail(podcastId)) },
                onPlayEpisode = { episodeId -> navController.navigate(Routes.player(episodeId)) }
            )
        }

        composable(Routes.DETAIL) { backStackEntry ->
            val podcastId = backStackEntry.arguments?.getString("podcastId").orEmpty()
            PodcastDetailScreen(
                podcastId = podcastId,
                onBack = { navController.popBackStack() },
                onPlayEpisode = { episodeId -> navController.navigate(Routes.player(episodeId)) }
            )
        }

        composable(Routes.PLAYER) { backStackEntry ->
            val episodeId = backStackEntry.arguments?.getString("episodeId").orEmpty()
            PlayerScreen(
                episodeId = episodeId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
