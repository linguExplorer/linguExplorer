package com.github.linguExplorer.repositories

import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import com.github.linguExplorer.models.Phrase
import com.github.linguExplorer.models.PhraseEntity
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select

class PhraseRepository {
    fun getPhrase(id: Int): PhraseEntity? =
        transaction {
            Phrase
                .select { Phrase.id eq id }
                .map { it.toPhrase() }
                .singleOrNull()
        }

    fun getPhrasesByTopicId(topicId: Int): List<PhraseEntity> =
        transaction {
            Phrase
                .select { Phrase.topicId eq topicId }
                .map { it.toPhrase() }
        }

    fun getPhrasesByTopicName(topic: String): List<PhraseEntity> =
        transaction {
            val topicId = TopicRepository().getTopicIdByName(topic)
            if (topicId == null) return@transaction emptyList<PhraseEntity>()

            Phrase
                .select { Phrase.topicId eq topicId }
                .map { it.toPhrase() }
        }

    fun getLimitedPhrasesByTopicName(topic: String, number: Int): List<PhraseEntity> =
    transaction {
        val topicId = TopicRepository().getTopicIdByName(topic)
        if (topicId == null) return@transaction emptyList<PhraseEntity>()

        Phrase
            .select { Phrase.topicId eq topicId }
            .limit(number)
            .map { it.toPhrase() }
    }




    fun addPhrase(phrase: String, translation: String, definition: String? = null,  topicId: Int): PhraseEntity? =
        transaction {
            val insertStatement = Phrase.insert {
                it[Phrase.phrase] = phrase
                it[Phrase.translation] = translation
                it[Phrase.definition] = definition
                it[Phrase.topicId] = topicId
            }
            insertStatement.resultedValues?.first()?.toPhrase()
        }

    fun removePhrase(id: Int): Boolean =
        transaction {
            val deletedRows = Phrase.deleteWhere { Phrase.id eq id }
            deletedRows > 0
        }


    companion object {
        private fun ResultRow.toPhrase() = PhraseEntity(
            this[Phrase.id],
            this[Phrase.phrase],
            this[Phrase.translation],
            this[Phrase.definition],
            this[Phrase.topicId]
        )
    }
}
