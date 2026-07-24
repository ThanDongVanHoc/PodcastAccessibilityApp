package com.mobile.podcast

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import org.junit.Rule
import org.junit.Test

/**
 * Automated accessibility checks (see the Testing & Evaluation section of the report).
 *
 * These demonstrate the two Compose-testing ideas from the report:
 *  1. Asserting semantics contracts — a control has the accessible name a screen reader will speak.
 *  2. Dumping the (un)merged semantics tree with printToLog for diagnosis.
 *
 * With `espresso-accessibility` on the classpath, ATF rules (touch-target size, contrast,
 * speakable text) can additionally be enabled as a quality gate in CI. See report §7.
 */
class AccessibilityTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun home_showsDiscoverHeading() {
        composeRule.onNodeWithText("Discover").assertIsDisplayed()
    }

    @Test
    fun searchAction_hasAccessibleName() {
        // The icon-only search button must expose a spoken label, not just a glyph.
        composeRule.onNodeWithContentDescription("Search podcasts").assertExists()
    }

    @Test
    fun dumpSemanticsTree_forDiagnosis() {
        // Print the unmerged tree so we can verify merge/clear behaviour on the cards.
        composeRule.onRoot(useUnmergedTree = true).printToLog("A11Y_TREE")
    }
}
