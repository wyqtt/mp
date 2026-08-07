package app.template.patches.shared

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    
    val KWGT_COMPATIBILITY = Compatibility(
        name = "KWGT (Kustom Widget Maker)",
        packageName = "org.kustom.widget",
        appIconColor = 0x4CAF50,
        targets = listOf(AppTarget(version = "3.82b619510aosp", versionCode = 382619510))
    )

}
