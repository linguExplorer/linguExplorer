package com.github.linguExplorer.models

import org.jetbrains.exposed.sql.Table

object Backpacks : Table("backpack") {
    val backpackId = integer("pk_backpack_id").autoIncrement()
    val userId = integer("fk_user_id")

    override val primaryKey = PrimaryKey(backpackId)
}

data class Backpack(
    val backpackId: Int,
    val userId: Int
)
