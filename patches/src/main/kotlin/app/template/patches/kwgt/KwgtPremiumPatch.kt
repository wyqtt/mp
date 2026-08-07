package app.template.patches.kwgt.premium

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.bytecodePatch
import app.template.patches.shared.Constants.KWGT_COMPATIBILITY
import app.template.patches.shared.returnEarly

/**
 * KWGT Premium Patch
 *
 * KWGT (Kustom Widget Maker) uses a multi-layer licensing system built around
 * the org.kustom.billing package:
 *
 *  1. LicenseState enum — represents the current license as one of:
 *     NOT_CHECKED, CHECK_FAILED, LICENSED, NOT_LICENSED
 *     All premium gates call isLicensed() on the active LicenseState instance.
 *
 *  2. LicenseClient (obfuscated as `d`) — the central billing manager.
 *     Method j() iterates over two validators (Play Billing + key unlock),
 *     aggregates their individual LicenseState results, and returns the combined
 *     state. The result is cached in field `b` and broadcast to all registered
 *     LicenseListener (e interface) callbacks.
 *
 *  3. BuildEnv — build-time capability flags.
 *     - g1() returns whether the build variant has a pro key available for purchase.
 *     - o1() is a secondary premium-capability flag used by LicenseClient.l().
 *     Both are backed by Kotlin lazy delegates (fields Q and R respectively).
 *     The AOSP/free build ships with these returning false, disabling the billing
 *     pipeline before it even starts.
 *
 * Patch layers:
 *  A) LicenseState.isLicensed() → return true
 *     Cascades to every premium gate in the editor, widget renderer, feature flags,
 *     and UI visibility checks. The method compares `this` to the LICENSED enum
 *     constant — patching it means every LicenseState instance appears licensed.
 *
 *  B) LicenseClient.j() → return LICENSED enum constant
 *     Short-circuits the validator aggregation loop. Since j() feeds the cached
 *     license state (field `b`) that is broadcast to all listeners, this ensures
 *     the entire app receives a LICENSED event on startup without any network call.
 *
 *  C) BuildEnv.g1() → return true
 *     Fixes the has-pro-key flag so the billing client does not abort early. In the
 *     AOSP build this is false by default, which causes LicenseClient.k() to skip
 *     initiating any license check at all. Returning true enables the full pipeline.
 *
 *  D) BuildEnv.o1() → return true
 *     Fixes the secondary premium capability flag consumed by LicenseClient.l() and
 *     any other build-time feature gates tied to this lazy value.
 */
@Suppress("unused")
val kwgtPremiumPatch = bytecodePatch(
    name = "KWGT Premium",
    description = "Unlocks KWGT Pro features by bypassing Play Billing and build-time license checks.",
) {
    compatibleWith(KWGT_COMPATIBILITY)

    execute {
        // Layer A: LicenseState.isLicensed() → always true
        // This is the single most effective injection point — every premium gate in the
        // app eventually calls isLicensed() on whatever LicenseState it holds, so patching
        // this one method covers the entire feature lock surface.
        IsLicensedFingerprint.method.returnEarly(true)

        // Layer B: LicenseClient.j() → return LICENSED enum constant immediately
        // Skips the validator aggregation loop and prevents any Play Billing / key-unlock
        // network round-trip. The returned LICENSED object is cached in field `b` and
        // dispatched to all LicenseListener callbacks (e.J()), so the UI reflects
        // licensed status on the first callback cycle.
        GetLicenseStateFingerprint.method.addInstructions(
            0,
            """
            sget-object v0, Lorg/kustom/billing/LicenseState;->LICENSED:Lorg/kustom/billing/LicenseState;
            return-object v0
            """.trimIndent(),
        )

        // Layer C: BuildEnv.g1() → return true
        // Re-enables the billing/pro-key capability flag that the AOSP build ships with
        // disabled. Without this, LicenseClient.k() returns false and the license check
        // is never initiated, meaning the LICENSED callback is never fired.
        BuildEnvHasProKeyFingerprint.method.returnEarly(true)

        // Layer D: BuildEnv.o1() → return true
        // Covers the secondary pro-capability lazy flag so any code path guarded by
        // LicenseClient.l() or direct calls to BuildEnv.o1() also sees a true value.
        BuildEnvIsProFingerprint.method.returnEarly(true)
    }
}