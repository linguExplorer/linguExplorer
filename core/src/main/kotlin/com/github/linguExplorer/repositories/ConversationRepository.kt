package com.github.linguExplorer.repositories

import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import com.github.linguExplorer.models.ConversationEntity
import com.github.linguExplorer.models.Conversation
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select

class ConversationRepository {
    fun getConversation(id: Int): ConversationEntity? =
        transaction {
            Conversation
                .select { Conversation.id eq id }
                .map { it.toConversation() }
                .singleOrNull()
        }

    fun addConversation(name: String, npcId: Int): ConversationEntity? =
        transaction {
            val insertStatement = Conversation.insert {
                it[Conversation.name] = name
                it[Conversation.npcId] = npcId
            }
            insertStatement.resultedValues?.first()?.toConversation()
        }

    fun removeConversation(id: Int): Boolean =
        transaction {
            val deletedRows = Conversation.deleteWhere { Conversation.id eq id }
            deletedRows > 0
        }

    fun getConversationById(id: Int): ConversationEntity? =
        transaction {
            Conversation
                .select { Conversation.id eq id }
                .map { it.toConversation() }
                .singleOrNull()
        }

    fun getConversationByName(name: String): ConversationEntity? =
        transaction {
            Conversation
                .select { Conversation.name eq name }
                .map { it.toConversation() }
                .singleOrNull()
        }



    companion object {
        private fun ResultRow.toConversation() = ConversationEntity(
            this[Conversation.id],
            this[Conversation.name],
            this[Conversation.npcId]
        )
    }
}
