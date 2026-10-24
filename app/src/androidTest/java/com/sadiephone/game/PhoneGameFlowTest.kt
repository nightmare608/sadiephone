package com.sadiephone.game

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.sadiephone.game.PhoneCompanyScreen
import androidx.compose.material3.MaterialTheme

@RunWith(AndroidJUnit4::class)
class PhoneGameFlowTest {
    @get:Rule val compose = createAndroidComposeRule<ComponentActivity>()
    @Test fun foundDesignLaunchSellReviewAndReturn() {
        val scope = "phone-ui-test-${System.nanoTime()}"
        var returned = false
        compose.setContent { MaterialTheme { PhoneCompanyScreen(scope) { returned = true } } }
        compose.waitUntil(10000) { compose.onAllNodesWithText("Şirketimi kur").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithText("Şirketimi kur").performClick()
        compose.waitUntil(10000) { compose.onAllNodesWithText("Yeni telefon tasarla").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithText("Yeni telefon tasarla").performClick()
        snapshot("sadiephone-designer.png")
        compose.onNodeWithTag("phone-designer-list").performScrollToNode(hasText("Lansmanı hazırla"))
        compose.onNodeWithText("Lansmanı hazırla").performClick()
        compose.onNodeWithText("Üret ve satışa çıkar").performClick()
        compose.waitUntil(10000) { compose.onAllNodesWithText("Üret ve satışa çıkar").fetchSemanticsNodes().isEmpty() }
        compose.onNodeWithText("Şirket", useUnmergedTree = true).performClick()
        compose.waitUntil(10000) { compose.onAllNodesWithText("Haftayı ilerlet →").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithText("Haftayı ilerlet →").performScrollTo().performClick()
        compose.waitUntil(10000) { compose.onAllNodesWithText("HAFTA 2 · İTİBAR",substring=true).fetchSemanticsNodes().isNotEmpty() }
        snapshot("sadiephone-company.png")
        compose.onNodeWithText("Yorumlar",useUnmergedTree=true).performScrollTo().performClick()
        compose.onNodeWithText("Beğenilme").assertIsDisplayed()
        snapshot("sadiephone-reviews.png")
        compose.onNodeWithContentDescription("Oyundan çık").performClick()
        compose.runOnIdle { org.junit.Assert.assertTrue(returned) }
    }
    private fun snapshot(name: String) {
        compose.waitForIdle()
        val instrument = InstrumentationRegistry.getInstrumentation()
        android.os.ParcelFileDescriptor.AutoCloseInputStream(
            instrument.uiAutomation.executeShellCommand("screencap -p /sdcard/Download/$name")
        ).use { it.readBytes() }
    }
}
