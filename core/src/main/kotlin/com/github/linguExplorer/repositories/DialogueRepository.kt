package com.github.linguExplorer.repositories

import com.github.linguExplorer.models.DialogueEntity
import com.github.linguExplorer.models.Dialogue
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class DialogueRepository {

    fun addDialogue(text: String, mood: String = "Normal", conversationId: Int): DialogueEntity? =
        transaction {
            val insertStatement = Dialogue.insert {
                it[Dialogue.text] = text
                it[Dialogue.mood] = mood
                it[Dialogue.conversationId] = conversationId
            }
            insertStatement.resultedValues?.first()?.toDialogue()
        }

    fun getDialogueById(id: Int): DialogueEntity? =
        transaction {
            Dialogue
                .select { Dialogue.dialogueId eq id }
                .map { it.toDialogue() }
                .singleOrNull()
        }

    fun removeDialogue(id: Int): Boolean =
        transaction {
            val deletedRows = Dialogue.deleteWhere { Dialogue.dialogueId eq id }
            deletedRows > 0
        }

    companion object {
        private fun ResultRow.toDialogue() = DialogueEntity(
            this[Dialogue.dialogueId],
            this[Dialogue.text],
            this[Dialogue.mood],
            this[Dialogue.conversationId]
        )
    }
}
