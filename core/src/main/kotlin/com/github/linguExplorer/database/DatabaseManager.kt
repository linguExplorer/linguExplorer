package com.github.linguExplorer.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.logging.Level
import java.util.logging.Logger

fun DatabaseManager() {
    val jdbcUrl = "jdbc:mysql://localhost:3306/linguExplorer"
    val username = "root"
    val password = ""

    Database.connect(jdbcUrl, driver = "com.mysql.cj.jdbc.Driver", user = username, password = password)

    transaction {
        println("Verbindung zur MySQL-Datenbank erfolgreich!")
        Logger.getLogger("DEBUG Exposed").level = Level.OFF

    }
}
