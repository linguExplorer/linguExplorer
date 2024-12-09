package com.github.linguExplorer.models

import org.jetbrains.exposed.sql.Table

/**
 * Dieser Kommentar trifft auf alle weiteren Klassen zu
 */
object User : Table() {
    val id = integer("pk_user_id")
    val name = varchar("name", 20).default("Blobi")

    override val primaryKey = PrimaryKey(id)
}

/**
 * Datenklasse
 */
data class UserEntity (
    val id: Int,
    val name: String
)

