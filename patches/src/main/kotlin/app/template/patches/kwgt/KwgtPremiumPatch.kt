package app.template.patches.kwgt

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch

/**
 * KWGT Premium Patch
 *
 * Unlocks KWGT Pro features by patching 5 layers of the licensing system:
 *  A) LicenseState.isLicensed() → return true
 *  B) LicenseClient.j() → return LICENSED enum constant
 *  C) BuildEnv.g1() → return true (has-pro-key flag)
 *  D) BuildEnv.o1() → return true (secondary premium flag)
 *  E) Config.v() → return false (should-show-ads flag)
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

        // Layer E: Config.v() → return false (disables ads)
        // This method is called by AdsActivity.onResume() and LicenseActivity.J()
        // to decide whether to load/show ads. Returning false = no ads.
        ShouldShowAdsFingerprint.method.addInstructions(
            0,
            """
            const/4 v0, 0x0
            return v0
            """.trimIndent(),
        )
    }
}