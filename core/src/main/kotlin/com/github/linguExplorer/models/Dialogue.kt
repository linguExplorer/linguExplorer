package com.github.linguExplorer.models

import org.jetbrains.exposed.sql.Table
//import org.jetbrains.exposed.sql.ReferenceOption

object Dialogue : Table("dialogue") {
    val dialogueId = integer("pk_dialogue_id").autoIncrement()
    val text = varchar("text", 500)
    val mood = varchar("mood", 20).default("Normal")
    val conversationId = integer("fk_conversation_id")

    override val primaryKey = PrimaryKey(dialogueId)
}


data class DialogueEntity(
    val dialogueId: Int,
    val text: String,
    val mood: String,
    val conversationId: Int

)
