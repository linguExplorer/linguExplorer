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
    fun saveUserId(userId: Int) {
        configFile.writeText("userid=$userId")
    }

    // Benutzer-ID lesen
    fun readUserId(): Int {
        return if (configFile.exists()) {
            val content = configFile.readText()
            val userIdString = content.substringAfter("userid=").trim()
            userIdString.toIntOrNull() ?: 1
        } else {
            1
        }
    }
}
