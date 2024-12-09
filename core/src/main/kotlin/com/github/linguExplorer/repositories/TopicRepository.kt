package com.github.linguExplorer.repositories

import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import com.github.linguExplorer.models.TopicEntity
import com.github.linguExplorer.models.Topic
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select

class TopicRepository {
    fun getTopicById(id: Int): TopicEntity? =
        transaction {
            Topic
                .select { Topic.id eq id }
                .map { it.toTopic() }
                .singleOrNull()
        }

    fun getTopicIdByName(topicName: String): Int? =
        transaction {
            Topic
                .select { Topic.name eq topicName }
                .map { it[Topic.id] }
                .singleOrNull()  // Gibt eine einzelne ID zurück oder null, wenn der Topic-Name nicht gefunden wurde
        }


    fun addTopic(name: String, description: String? = null, next: Int? = null): TopicEntity? =
        transaction {
            val insertStatement = Topic.insert {
                it[Topic.name] = name
                it[Topic.description] = description
                it[Topic.next] = next
            }
            insertStatement.resultedValues?.first()?.toTopic()
        }

    fun removeTopic(id: Int): Boolean =
        transaction {
            val deletedRows = Topic.deleteWhere { Topic.id eq id }
            deletedRows > 0
        }


    companion object {
        private fun ResultRow.toTopic() = TopicEntity(
            this[Topic.id],
            this[Topic.name],
            this[Topic.description],
            this[Topic.next]
        )
    }
}
