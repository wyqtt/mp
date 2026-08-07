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