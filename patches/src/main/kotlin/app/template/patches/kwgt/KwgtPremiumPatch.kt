package app.template.patches.kwgt

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch

/**
 * KWGT Premium Patch
 *
 * Unlocks KWGT Pro features and disables all ads.
 */
@Suppress("unused")
val kwgtPremiumPatch = bytecodePatch(
    name = "KWGT Premium",
    description = "Unlocks KWGT Pro features by bypassing Play Billing and build-time license checks.",
) {
    compatibleWith("org.kustom.widget")

    execute {
        // Layer A: LicenseState.isLicensed() → always true
        IsLicensedFingerprint.method.addInstructions(
            0,
            """
            const/4 v0, 0x1
            return v0
            """.trimIndent(),
        )

        // Layer B: LicenseClient.j() → return LICENSED enum constant immediately
        GetLicenseStateFingerprint.method.addInstructions(
            0,
            """
            sget-object v0, Lorg/kustom/billing/LicenseState;->LICENSED:Lorg/kustom/billing/LicenseState;
            return-object v0
            """.trimIndent(),
        )

        // Layer C: BuildEnv.g1() → return true
        BuildEnvHasProKeyFingerprint.method.addInstructions(
            0,
            """
            const/4 v0, 0x1
            return v0
            """.trimIndent(),
        )

        // Layer D: BuildEnv.o1() → return true
        BuildEnvIsProFingerprint.method.addInstructions(
            0,
            """
            const/4 v0, 0x1
            return v0
            """.trimIndent(),
        )

        // Layer E: AdsActivity.y1(Z)V → return-void (kill banner ad logic)
        AdsActivityToggleFingerprint.method.addInstructions(
            0,
            "return-void",
        )

        // Layer F: MarketActivity.l1() → return-void (kill interstitial ads)
        // Called from EditorActivity and DrawerActivity to show interstitial ads
        InterstitialAdFingerprint.method.addInstructions(
            0,
            "return-void",
        )

        // Layer G: Config.v() → return false (should-show-ads config flag)
        ShouldShowAdsFingerprint.method.addInstructions(
            0,
            """
            const/4 v0, 0x0
            return v0
            """.trimIndent(),
        )

        // Layer H: Ads initializer → return-void (prevent Appodeal SDK init)
        AdsInitFingerprint.method.addInstructions(
            0,
            "return-void",
        )

        // Layer I: Banner show → return-void (prevent banner display)
        AdsBannerShowFingerprint.method.addInstructions(
            0,
            "return-void",
        )
    }
}