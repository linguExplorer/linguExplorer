package com.github.linguExplorer.repositories

import org.jetbrains.exposed.sql.transactions.transaction
import com.github.linguExplorer.models.Phrase
import com.github.linguExplorer.models.PhraseEntity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

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


    fun getAllPhrases(): List<PhraseEntity> =
        transaction {
            Phrase
                .selectAll()
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

    /*fun getLimitedPhrasesByTopicName(topic: String, number: Int): List<PhraseEntity> =
    transaction {
        val topicId = TopicRepository().getTopicIdByName(topic)
        if (topicId == null) return@transaction emptyList<PhraseEntity>()

        val allPhrases = Phrase
            .select { Phrase.topicId eq topicId }
            .map { it.toPhrase() }

        allPhrases.shuffled().take(number)
    }*/

    /*fun getPhrasesByTopicNameForUser(topic: String, userId: Int): List<PhraseEntity> =
        transaction {
            val topicId = TopicRepository().getTopicIdByName(topic)
            val phaseProgressRepository = PhraseProgressRepository()
            val phaseProgressHistoryRepository = PhraseProgressHistoryRepository()

            if (topicId == null) return@transaction emptyList<PhraseEntity>()

            val allPhrases = getPhrasesByTopicId(topicId)


            val unMasteredPhrases = allPhrases.filter { phrase ->
                val progress = phaseProgressRepository.getPhraseProgress(phrase.id, userId)
                progress?.isMastered != true
            }.sortedBy {
                Math.random()
            }.sortedBy { phrase ->
                val correctIndex = phaseProgressHistoryRepository.calculateCorrectIndex(userId, phrase.id)
                correctIndex
            }.sortedBy { phrase ->
                val correctIndex = phaseProgressHistoryRepository.calculateCorrectIndex(userId, phrase.id)
                if (correctIndex == -1.0) 1 else 0
            }

            val masteredPhrases = allPhrases.filter { phrase ->
                val progress = phaseProgressRepository.getPhraseProgress(phrase.id, userId)
                progress?.isMastered == true
            }.sortedBy { phrase ->
                val correctIndex = phaseProgressHistoryRepository.calculateCorrectIndex(userId, phrase.id)
                correctIndex
            }.sortedBy {
                Math.random()
            }.sortedBy { phrase ->
                val correctIndex = phaseProgressHistoryRepository.calculateCorrectIndex(userId, phrase.id)
                correctIndex
            }

            val result = (unMasteredPhrases + masteredPhrases)

            return@transaction result
        }*/

    fun getLimitedPhrasesByTopicNameForUser(topicId: Int?, userId: Int, size: Int): List<PhraseEntity> =
        transaction {
            val phaseProgressRepository = PhraseProgressRepository()
            val phaseProgressHistoryRepository = PhraseProgressHistoryRepository()

            if (topicId == null) return@transaction emptyList<PhraseEntity>()

            // Alle Phrasen für das angegebene Thema laden
            val allPhrases = getPhrasesByTopicId(topicId)

            // Alle Fortschritte für den Benutzer in einem Rutsch abfragen
            val progressMap = phaseProgressRepository.getAllPhraseProgressForUser(userId)
                .associateBy { it.phraseId }

            val userHistory = phaseProgressHistoryRepository.getAllEntriesForUser(userId)

            val unMasteredPhrases = allPhrases.filter { phrase ->
                val progress = progressMap[phrase.id]
                progress?.isMastered != true
            }.sortedBy { phrase ->
                val correctIndex = phaseProgressHistoryRepository.calculateCorrectIndex(phrase.id, userHistory)
                correctIndex
            }.sortedBy { Math.random() }  // Zufällige Reihenfolge

            val masteredPhrases = allPhrases.filter { phrase ->
                val progress = progressMap[phrase.id]
                progress?.isMastered == true  // Nur bearbeitete Phrasen
            }.sortedBy { phrase ->
                val correctIndex = phaseProgressHistoryRepository.calculateCorrectIndex(phrase.id, userHistory)
                correctIndex
            }.sortedBy { Math.random() }  // Zufällige Reihenfolge

            // Die beiden Listen zusammenfügen und auf die gewünschte Größe beschränken
            val result = (unMasteredPhrases + masteredPhrases).take(size)

            return@transaction result
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
