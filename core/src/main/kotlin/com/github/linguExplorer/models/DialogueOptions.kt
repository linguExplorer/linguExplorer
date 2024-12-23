package com.github.linguExplorer.models

import org.jetbrains.exposed.sql.Table
//import org.jetbrains.exposed.sql.ReferenceOption

object DialogueOptions : Table("dialogue_options") {
    val responseId = integer("pk_response_id")
    val text = varchar("text", 500)
    val dialogueId = integer("fk_dialogue_id")
    val next = integer("next_dialogue_id")

    override val primaryKey = PrimaryKey(dialogueId)
}


data class DialogueOptionsEntity(
    val responseId: Int,
    val text: String,
    val dialogueId: Int,
    val next: Int

)
