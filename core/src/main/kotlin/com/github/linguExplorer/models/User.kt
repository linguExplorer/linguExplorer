package com.github.linguExplorer.models

import org.jetbrains.exposed.sql.Table

object User : Table("user") {
    val id = integer("pk_user_id")
    val saveNumber = integer("pk_save_number")
    val name = varchar("name", 20).default("Blob")

    override val primaryKey = PrimaryKey(id, saveNumber)
}

data class UserEntity(
    val id: Int,
    val saveNumber: Int,
    val name: String = "Blob"
)
