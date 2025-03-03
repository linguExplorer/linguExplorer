package com.github.linguExplorer.repositories

import com.github.linguExplorer.models.PhraseProgressHistory
import com.github.linguExplorer.models.PhraseProgressHistoryEntity
import com.github.linguExplorer.saveNumber
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.math.exp

class PhraseProgressHistoryRepository {

    fun addPhraseProgress(userId: Int, phraseId: Int, saveNumber: Int, correct: Boolean): PhraseProgressHistoryEntity? =
        transaction {
            val insertStatement = PhraseProgressHistory.insert {
                it[PhraseProgressHistory.userId] = userId
                it[PhraseProgressHistory.phraseId] = phraseId
                it[PhraseProgressHistory.correct] = correct
                it[PhraseProgressHistory.saveNumber] = saveNumber
            }
            insertStatement.resultedValues?.first()?.toPhraseProgressHistoryEntity()
        }

    fun addPhraseProgressHistories(userId: Int, saveNumber: Int, phrasesWithCorrectness: List<Pair<Int, Boolean>>) {
        transaction {
            phrasesWithCorrectness.forEach { (phraseId, correct) ->
                PhraseProgressHistory.insert {
                    it[PhraseProgressHistory.userId] = userId
                    it[PhraseProgressHistory.phraseId] = phraseId
                    it[PhraseProgressHistory.correct] = correct
                    it[PhraseProgressHistory.saveNumber] = saveNumber
                }
            }
        }
    }

    fun getCorrectValuesForUserAndPhrase(userId: Int, phraseId: Int, saveNumber: Int): List<Boolean> =
        transaction {
            PhraseProgressHistory
                .select {
                    (PhraseProgressHistory.userId eq userId) and
                        (PhraseProgressHistory.phraseId eq phraseId) and
                        (PhraseProgressHistory.saveNumber eq saveNumber)
                }
                .map { it[PhraseProgressHistory.correct] }
        }

    fun getAllEntriesForUser(userId: Int, saveNumber: Int): List<PhraseProgressHistoryEntity> =
        transaction {
            PhraseProgressHistory
                .select { (PhraseProgressHistory.userId eq userId) and (PhraseProgressHistory.saveNumber eq saveNumber)}
                .map { it.toPhraseProgressHistoryEntity() }
        }

    fun calculateCorrectIndex(phraseId: Int, entryValues: List<PhraseProgressHistoryEntity>): Double {
        var weightedSum = 0.0
        var factorSum = 0.0
        var size = 0

        entryValues.filter { entry ->
            entry.phraseId == phraseId
        }.forEachIndexed { index, entry ->
            size++
            val factor = index + 1
            weightedSum += (if (entry.correct == 1) 1 else 0) * factor
            factorSum += factor
        }

        return if (factorSum > 0) 1 - exp(-0.5 * (weightedSum / factorSum) * size) else -1.0
    }

    private fun ResultRow.toPhraseProgressHistoryEntity() = PhraseProgressHistoryEntity(
        progressId = this[PhraseProgressHistory.progressId],
        userId = this[PhraseProgressHistory.userId],
        phraseId = this[PhraseProgressHistory.phraseId],
        correct = if (this[PhraseProgressHistory.correct]) 1 else 0,
        saveNumber = this[PhraseProgressHistory.saveNumber]
    )
}
