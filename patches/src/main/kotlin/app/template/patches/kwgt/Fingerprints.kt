package app.template.patches.kwgt.premium

import app.morphe.patcher.Fingerprint
import app.morphe.patcher.methodCall
import com.android.tools.smali.dexlib2.AccessFlags

/**
 * Targets LicenseState.isLicensed()Z
 *
 * The primary license gate on the LicenseState enum. Every premium feature check
 * eventually calls isLicensed() on the current LicenseState. If this returns true,
 * the app treats the user as a paid/pro user.
 *
 * Smali (smali_classes6/org/kustom/billing/LicenseState.smali):
 *   .method public final isLicensed()Z
 *     .registers 2
 *     sget-object v0, Lorg/kustom/billing/LicenseState;->LICENSED:Lorg/kustom/billing/LicenseState;
 *     if-ne p0, v0, :cond_0
 *     const/4 v0, 0x1
 *     return v0
 *     :cond_0
 *     const/4 v0, 0x0
 *     return v0
 *   .end method
 *
 * Access flags: PUBLIC FINAL
 * Return type:  Z
 * Parameters:   none (instance method on enum)
 */
object IsLicensedFingerprint : Fingerprint(
    returnType = "Z",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.FINAL),
    parameters = listOf(),
    definingClass = "Lorg/kustom/billing/LicenseState;",
    name = "isLicensed",
)

/**
 * Targets LicenseClient.j()Lorg/kustom/billing/LicenseState;
 *
 * Private method that aggregates validator results and determines the overall
 * license state. It iterates over validators, collects their LicenseState via
 * validator.f(), and returns LICENSED if any validator reports LICENSED.
 * Otherwise falls back to NOT_CHECKED or NOT_LICENSED.
 *
 * By patching this to always return LICENSED, we bypass the entire validator chain.
 *
 * Smali (smali_classes6/org/kustom/billing/d.smali):
 *   .method private final j()Lorg/kustom/billing/LicenseState;
 *     ... validator iteration ...
 *     sget-object v0, Lorg/kustom/billing/LicenseState;->LICENSED:...
 *     invoke-interface {v1, v0}, Ljava/util/List;->contains(...)Z
 *     ...
 *   .end method
 *
 * Access flags: PRIVATE FINAL
 * Return type:  Lorg/kustom/billing/LicenseState;
 * Parameters:   none (instance method)
 * Defining class: Lorg/kustom/billing/d; (obfuscated LicenseClient)
 */
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

/**
 * Targets BuildEnv.g1()Z
 *
 * Static method that checks whether the build variant has a pro key.
 * Used by LicenseClient.k() to determine if the app supports in-app purchases.
 * Backed by a Kotlin lazy delegate (field Q).
 *
 * When this returns true, the billing system considers the app eligible for
 * premium features. The free version returns false here.
 *
 * Smali (smali_classes7/org/kustom/config/BuildEnv.smali):
 *   .method public static final g1()Z
 *     sget-object v0, Lorg/kustom/config/BuildEnv;->Q:Lkotlin/Lazy;
 *     invoke-interface {v0}, Lkotlin/Lazy;->getValue()Ljava/lang/Object;
 *     ... unbox Boolean ...
 *     return v0
 *   .end method
 *
 * Access flags: PUBLIC STATIC FINAL
 * Return type:  Z
 */
object BuildEnvHasProKeyFingerprint : Fingerprint(
    returnType = "Z",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC, AccessFlags.FINAL),
    parameters = listOf(),
    definingClass = "Lorg/kustom/config/BuildEnv;",
    name = "g1",
)

/**
 * Targets BuildEnv.o1()Z
 *
 * Static method that checks another premium-related flag.
 * Used by LicenseClient.l() as a secondary check.
 * Backed by a Kotlin lazy delegate (field R).
 *
 * Smali (smali_classes7/org/kustom/config/BuildEnv.smali):
 *   .method public static final o1()Z
 *     sget-object v0, Lorg/kustom/config/BuildEnv;->R:Lkotlin/Lazy;
 *     invoke-interface {v0}, Lkotlin/Lazy;->getValue()Ljava/lang/Object;
 *     ... unbox Boolean ...
 *     return v0
 *   .end method
 *
 * Access flags: PUBLIC STATIC FINAL
 * Return type:  Z
 */
object BuildEnvIsProFingerprint : Fingerprint(
    returnType = "Z",
    accessFlags = listOf(AccessFlags.PUBLIC, AccessFlags.STATIC, AccessFlags.FINAL),
    parameters = listOf(),
    definingClass = "Lorg/kustom/config/BuildEnv;",
    name = "o1",
)