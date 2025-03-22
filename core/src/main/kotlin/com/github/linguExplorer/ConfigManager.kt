package com.github.linguExplorer
import java.io.File

object ConfigManager {
    private const val CONFIG_FILE_NAME = "config.properties"
    private val configFile: File

    init {
        val userHome = System.getProperty("user.home")
        val appDir = File("$userHome/.linguExplorer")
        appDir.mkdirs()
        configFile = File(appDir, CONFIG_FILE_NAME)
    }

    // Benutzer-ID speichern
    fun saveUserId(userId: String) {
        configFile.writeText("userid=$userId")
    }

    // Benutzer-ID lesen
    fun readUserId(): String? {
        return if (configFile.exists()) {
            configFile.readText().split("=")[1] // Liest den Wert nach "userid="
        } else {
            null
        }
    }
}
