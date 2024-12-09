package com.github.linguExplorer.models

import org.jetbrains.exposed.sql.Table

object NPC : Table() {
    val id = integer("pk_npc_id").autoIncrement()
    val name = varchar("name", 20)
    val role = varchar("role", 20).nullable()
    val resource = varchar("source", 500).nullable()

    override val primaryKey = PrimaryKey(id)
}


data class NPCEntity (
    val id: Int,
    val name: String,
    val role: String?,
    val resource: String?
)
