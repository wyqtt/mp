package app.template.patches.kwgt

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall
import app.morphe.patcher.string
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
 * Targets AdsActivity.y1(Z)V — the core banner ad toggle method.
 *
 * Contains the string "Unable to start ads, banner container not available"
 * which uniquely identifies it across versions.
 */
object AdsActivityToggleFingerprint : Fingerprint(
    returnType = "V",
    accessFlags = listOf(AccessFlags.PRIVATE, AccessFlags.FINAL),
    parameters = listOf("Z"),
    definingClass = "Lorg/kustom/app/AdsActivity;",
    name = "y1",
    filters = listOf(
        string("Unable to start ads, banner container not available"),
    ),
)

/**
 * Targets MarketActivity.l1(String, Function0)V — the interstitial ad method.
 *
 * Called from EditorActivity and DrawerActivity to show Appodeal interstitial ads.
 * Shows interstitial (0x80) via Appodeal.show$default().
 */
object InterstitialAdFingerprint : Fingerprint(
    returnType = "V",
    accessFlags = listOf(AccessFlags.PROTECTED, AccessFlags.FINAL),
    parameters = listOf("Ljava/lang/String;", "Lkotlin/jvm/functions/Function0;"),
    definingClass = "Lorg/kustom/app/MarketActivity;",
    name = "l1",
    filters = listOf(
        methodCall(
            definingClass = "Lcom/appodeal/ads/Appodeal;",
            name = "show\$default",
        ),
    ),
)

/**
 * Targets the "should show ads" config flag: org/kustom/config/f.v()Z
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