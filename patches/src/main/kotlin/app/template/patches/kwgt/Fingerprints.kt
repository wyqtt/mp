package app.template.patches.kwgt

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall
import com.android.tools.smali.dexlib2.AccessFlags

object IsLicensedFingerprint : Fingerprint(
    returnType = "Z",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    parameters = listOf(),
    definingClass = "Lorg/kustom/billing/LicenseState;",
    name = "isLicensed",
)

object GetLicenseStateFingerprint : Fingerprint(
    returnType = "Lorg/kustom/billing/LicenseState;",
    accessFlags = listOf(AccessFlags.PRIVATE, AccessFlags.FINAL),
    parameters = listOf(),
    definingClass = "Lorg/kustom/billing/d;",
    name = "j",
    filters = listOf(
        methodCall(
            definingClass = "Lorg/kustom/billing/validators/a;",
            name = "f",
        ),
    ),
)

object BuildEnvHasProKeyFingerprint : Fingerprint(
    returnType = "Z",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC, AccessFlags.FINAL),
    parameters = listOf(),
    definingClass = "Lorg/kustom/config/BuildEnv;",
    name = "g1",
)

object BuildEnvIsProFingerprint : Fingerprint(
    returnType = "Z",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC, AccessFlags.FINAL),
    parameters = listOf(),
    definingClass = "Lorg/kustom/config/BuildEnv;",
    name = "o1",
)

/**
 * Targets the "should show ads" method: org/kustom/config/f.v()Z
 *
 * This method is called by AdsActivity.onResume() and LicenseActivity.J()
 * to determine whether ads should be displayed. Returns true = show ads.
 * Patching to return false disables all ad loading and display.
 *
 * The method calls y() (checks if not-pro) and BuildEnv.A0() (ads capability flag).
 *
 * Access flags: PUBLIC FINAL
 * Return type: Z
 * Defining class: Lorg/kustom/config/f; (obfuscated Settings/Config class)
 */
object ShouldShowAdsFingerprint : Fingerprint(
    returnType = "Z",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    parameters = listOf(),
    definingClass = "Lorg/kustom/config/f;",
    name = "v",
    filters = listOf(
        methodCall(
            definingClass = "Lorg/kustom/config/f;",
            name = "y",
        ),
        methodCall(
            definingClass = "Lorg/kustom/config/BuildEnv;",
            name = "A0",
        ),
    ),
)