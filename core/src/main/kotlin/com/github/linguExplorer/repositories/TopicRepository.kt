package com.github.linguExplorer.repositories

import org.jetbrains.exposed.sql.transactions.transaction
import com.github.linguExplorer.models.TopicEntity
import com.github.linguExplorer.models.Topic
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class TopicRepository {
    fun getTopicById(id: Int?): TopicEntity? =
        transaction {
            if (id != null) {
                Topic
                    .select { Topic.id eq id }
                    .map { it.toTopic() }
                    .singleOrNull()
            } else {
                return@transaction null
            }
        }

    fun getTopics(): List<TopicEntity> =
        transaction {
            Topic
                .selectAll()
                .map { it.toTopic() }
    }

    fun getFirstTopic(): TopicEntity =
        transaction {
            Topic
                .selectAll()
                .orderBy(Topic.id to SortOrder.ASC)
                .limit(1)
                .map { it.toTopic() }
                .singleOrNull()!!
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
