package com.github.linguExplorer.models

import org.jetbrains.exposed.sql.Table

/**
 * Dieser Kommentar trifft auf alle weiteren Klassen zu
 */
object Users : Table() {
    val id = integer("id")
    val name = varchar("name", 20).default("Blobi")

    override val primaryKey = PrimaryKey(id)
}

/**
 * Datenklasse
 */
data class User(
    val id: Int,
    val name: String
)

