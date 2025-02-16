package com.github.linguExplorer.models

import org.jetbrains.exposed.sql.Table

object Conversation : Table("conversation") {
    val id = integer("pk_conversation_id").autoIncrement()
    val name = varchar("name", 20)
    val npcId = integer("fk_npc_id")


    override val primaryKey = PrimaryKey(id)
}


data class ConversationEntity (
    val id: Int,
    val name: String,
    val npcId: Int
)
