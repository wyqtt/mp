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
 * Targets the "should show ads" config flag: org/kustom/config/f.v()Z
 *
 * Returns true when ads should show. Patching to return false prevents
 * the ad display path in AdsActivity.onResume() and LicenseActivity.J().
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

/**
 * Targets the Appodeal ad initializer: org/kustom/ads/c.a(Activity)V
 *
 * This is the entry point that initializes and caches Appodeal ads.
 * Called from AdsActivity.onResume() via the ads singleton.
 * Patching to return-void immediately prevents any ad SDK initialization.
 */
object AdsInitFingerprint : Fingerprint(
    returnType = "V",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    parameters = listOf("Landroid/app/Activity;"),
    definingClass = "Lorg/kustom/ads/c;",
    name = "a",
    filters = listOf(
        methodCall(
            definingClass = "Lcom/appodeal/ads/Appodeal;",
            name = "setTesting",
        ),
    ),
)

/**
 * Targets the ad banner show method: org/kustom/ads/a.b(FrameLayout, AdsViewHelperInterface$a)V
 *
 * This method creates the Appodeal banner view and adds it to the FrameLayout container.
 * Patching to return-void immediately prevents any banner from appearing.
 */
object AdsBannerShowFingerprint : Fingerprint(
    returnType = "V",
    accessFlags = listOf(AccessFlags.PUBLIC),
    parameters = listOf("Landroid/widget/FrameLayout;", "Lorg/kustom/ads/AdsViewHelperInterface\$a;"),
    definingClass = "Lorg/kustom/ads/a;",
    name = "b",
    filters = listOf(
        methodCall(
            definingClass = "Lcom/appodeal/ads/Appodeal;",
            name = "getBannerView",
        ),
    ),
)