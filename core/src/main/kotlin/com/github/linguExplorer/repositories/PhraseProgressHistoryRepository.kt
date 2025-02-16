package com.github.linguExplorer.repositories

import com.github.linguExplorer.models.PhraseProgressHistory
import com.github.linguExplorer.models.PhraseProgressHistoryEntity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.math.exp

class PhraseProgressHistoryRepository {
    fun addPhraseProgress(userId: Int, phraseId: Int, correct: Boolean): PhraseProgressHistoryEntity? =
        transaction {
            val insertStatement = PhraseProgressHistory.insert {
                it[PhraseProgressHistory.userId] = userId
                it[PhraseProgressHistory.phraseId] = phraseId
                it[PhraseProgressHistory.correct] = correct
            }
            insertStatement.resultedValues?.first()?.toPhraseProgressHistoryEntity()
        }


    fun getCorrectValuesForUserAndPhrase(userId: Int, phraseId: Int): List<Boolean> =
        transaction {
            PhraseProgressHistory
                .select { (PhraseProgressHistory.userId eq userId) and (PhraseProgressHistory.phraseId eq phraseId) }
                .map { it[PhraseProgressHistory.correct] }
        }

    fun calculateCorrectIndex(userId: Int, phraseId: Int): Double {
        val correctValues = getCorrectValuesForUserAndPhrase(userId, phraseId)

        var weightedSum = 0.0
        var factorSum = 0.0

        correctValues.forEachIndexed { index, correct ->
            val factor = index + 1
            weightedSum += (if (correct) 1 else 0) * factor
            factorSum += factor
        }

        return if (factorSum > 0) 1 - exp(-0.5 * (weightedSum / factorSum) * correctValues.size) else -1.0
    }

    private fun ResultRow.toPhraseProgressHistoryEntity() = PhraseProgressHistoryEntity(
        progressId = this[PhraseProgressHistory.progressId],
        userId = this[PhraseProgressHistory.userId],
        phraseId = this[PhraseProgressHistory.phraseId],
        correct = if (this[PhraseProgressHistory.correct]) 1 else 0
    )
}
