package com.github.linguExplorer.models

import org.jetbrains.exposed.sql.Table

object SpaceshipItem : Table("spaceship_item") {
    val id = integer("pk_item_id").autoIncrement()
    val name = varchar("name", 50)
    val description = varchar("description", 100)
    val price = integer("price")
    val resource = varchar("resource", 500)

    override val primaryKey = PrimaryKey(id)
}

data class SpaceshipItemEntity(
    val id: Int,
    val name: String,
    val description: String,
    val price: Int,
    val resource: String
)
