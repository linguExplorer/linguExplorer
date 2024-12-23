package com.github.linguExplorer.repositories

import com.github.linguExplorer.models.DialogueOptions
import com.github.linguExplorer.models.DialogueOptionsEntity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class DialogueOptionsRepository {

    fun addResponse(responseId: Int, text: String, dialogueId: Int, next: Int): DialogueOptionsEntity? =
        transaction {
            val insertStatement = DialogueOptions.insert {
                it[DialogueOptions.responseId] = responseId
                it[DialogueOptions.text] = text
                it[DialogueOptions.dialogueId] = dialogueId
                it[DialogueOptions.next] = next
            }
            insertStatement.resultedValues?.first()?.toDialogueOptionsEntity()
        }

    fun getResponseById(responseId: Int): DialogueOptionsEntity? =
        transaction {
            DialogueOptions
                .select { DialogueOptions.responseId eq responseId }
                .map { it.toDialogueOptionsEntity() }
                .singleOrNull()
        }

    fun getResponsesByDialogueId(dialogueId: Int): List<DialogueOptionsEntity> =
        transaction {
            DialogueOptions
                .select { DialogueOptions.dialogueId eq dialogueId }
                .map { it.toDialogueOptionsEntity() }
        }

    fun removeResponse(responseId: Int): Boolean =
        transaction {
            val deletedRows = DialogueOptions.deleteWhere { DialogueOptions.responseId eq responseId }
            deletedRows > 0
        }

    companion object {
        private fun ResultRow.toDialogueOptionsEntity() = DialogueOptionsEntity(
            this[DialogueOptions.responseId],
            this[DialogueOptions.text],
            this[DialogueOptions.dialogueId],
            this[DialogueOptions.next]
        )
    }
}
