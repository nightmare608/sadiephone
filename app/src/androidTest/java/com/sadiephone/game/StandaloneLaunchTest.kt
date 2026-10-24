package com.sadiephone.game

import android.content.pm.PackageManager
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StandaloneLaunchTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()
    @Test fun standaloneAppStartsWithoutAccountAndHasNoInternetPermission() {
        compose.onNodeWithText("sadiephone").assertIsDisplayed()
        compose.waitUntil(10000) { compose.onAllNodesWithText("Şirketimi kur").fetchSemanticsNodes().isNotEmpty() }
        compose.onNodeWithText("Şirketimi kur").assertIsDisplayed()
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        @Suppress("DEPRECATION")
        val info = context.packageManager.getPackageInfo(context.packageName,PackageManager.GET_PERMISSIONS or PackageManager.GET_PROVIDERS)
        assertTrue(context.packageName.startsWith("com.sadiephone.game"))
        assertFalse(info.requestedPermissions.orEmpty().contains("android.permission.INTERNET"))
        assertFalse(info.providers.orEmpty().any { it.name.contains("firebase",true) })
    }
}
