package com.github.linguExplorer.models

import org.jetbrains.exposed.sql.Table

object Powerup : Table("powerup") {
    val id = integer("pk_powerup_id").autoIncrement()
    val name = varchar("name", 50)
    val description = varchar("description", 500).nullable()
    val price = integer("price")
    val baseNumberOfUsages = integer("base_number_of_usages").default(1)
    val resource = varchar("resource", 500)

    override val primaryKey = PrimaryKey(id)
}

data class PowerupEntity(
    val id: Int,
    val name: String,
    val description: String,
    val price: Int,
    val baseNumberOfUsages: Int,
    val resource: String
)
