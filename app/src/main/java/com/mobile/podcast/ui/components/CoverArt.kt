package com.mobile.podcast.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Placeholder cover art: a coloured tile with the title's initial. Stands in for a real image in
 * this front-end-only demo.
 *
 * ACCESSIBILITY: the tile is purely decorative when it sits next to a text label that already names
 * the podcast. We therefore hide it from assistive services with [hideFromAccessibility] (Compose
 * 1.8+) so TalkBack does not announce a meaningless coloured box, while the node is still present
 * for UI tests. The initial letter is likewise not exposed.
 */
@Composable
fun CoverArt(
    title: String,
    color: Color,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
    decorative: Boolean = true,
) {
    val base = modifier
        .size(size)
        .clip(RoundedCornerShape(12.dp))
        .background(color)
    Box(
        modifier = if (decorative) base.semantics { hideFromAccessibility() } else base,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title.take(1).uppercase(),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = (size.value / 2.4f).sp,
            style = MaterialTheme.typography.titleLarge
        )
    }
}
