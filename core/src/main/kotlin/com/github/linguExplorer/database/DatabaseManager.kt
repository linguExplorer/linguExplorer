package com.github.linguExplorer.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.logging.Level
import java.util.logging.Logger

fun DatabaseManager() {
    val jdbcUrl = "jdbc:mysql://213.130.147.61:4000/linguexplorer"
    val username = "root"
    val password = "my-secret-pw"

    Database.connect(jdbcUrl, driver = "com.mysql.cj.jdbc.Driver", user = username, password = password)

    transaction {
        println("Verbindung zur MySQL-Datenbank erfolgreich!")
        Logger.getLogger("DEBUG Exposed").level = Level.OFF

    }
}
