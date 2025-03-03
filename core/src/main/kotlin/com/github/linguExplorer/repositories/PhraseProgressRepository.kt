package com.github.linguExplorer.repositories

import com.github.linguExplorer.models.PhraseEntity
import com.github.linguExplorer.models.PhraseProgress
import com.github.linguExplorer.models.PhraseProgressEntity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class PhraseProgressRepository {

    fun addPhraseProgress(phraseId: Int, userId: Int, saveNumber: Int, isMastered: Boolean = false) =
        transaction {
            PhraseProgress.insert {
                it[this.phraseId] = phraseId
                it[this.userId] = userId
                it[this.saveNumber] = saveNumber
                it[this.isMastered] = isMastered
            }
        }

    fun addMultiplePhraseProgress(progresses: List<PhraseProgressData>) =
        transaction {
            progresses.forEach { progress ->
                PhraseProgress.insert {
                    it[this.phraseId] = progress.phraseId
                    it[this.userId] = progress.userId
                    it[this.saveNumber] = progress.saveNumber
                    it[this.isMastered] = progress.isMastered
                }
            }
        }


    fun getPhraseProgress(phraseId: Int, userId: Int, saveNumber: Int): PhraseProgressEntity? =
        transaction {
            PhraseProgress
                .select { (PhraseProgress.phraseId eq phraseId) and (PhraseProgress.userId eq userId) and (PhraseProgress.saveNumber eq saveNumber) }
                .map { it.toPhraseProgress() }
                .singleOrNull()
        }

    fun deletePhraseProgresses(phraseId: Int, userId: Int, saveNumber: Int): Boolean =
        transaction {
            val deletedRows = PhraseProgress.deleteWhere {
                (PhraseProgress.phraseId eq phraseId) and (PhraseProgress.userId eq userId) and (PhraseProgress.saveNumber eq saveNumber)
            }
            deletedRows > 0
        }

    fun getAllPhraseProgressForUser(userId: Int, saveNumber: Int): List<PhraseProgressEntity> =
        transaction {
            PhraseProgress
                .select { (PhraseProgress.userId eq userId) and (PhraseProgress.saveNumber eq saveNumber) }
                .map { it.toPhraseProgress() }
        }

    fun getAllPhrasesOfUserProgress(userId: Int, saveNumber: Int): List<PhraseEntity> =
        transaction {
            PhraseProgress
                .select { (PhraseProgress.userId eq userId) and (PhraseProgress.saveNumber eq saveNumber) }
                .mapNotNull { progress ->
                    PhraseRepository().getPhrase(progress[PhraseProgress.phraseId])
                }
        }

    fun changeMasteredState(userId: Int, phraseId: Int, saveNumber: Int) =
        transaction {
            PhraseProgress.update({
                (PhraseProgress.userId eq userId) and
                    (PhraseProgress.phraseId eq phraseId) and
                    (PhraseProgress.saveNumber eq saveNumber)
            }) {
                it[isMastered] = true
            }
        }

    fun changeMultipleMasteredStates(userId: Int, saveNumber: Int, phraseIds: List<Int>) =
        transaction {
            phraseIds.forEach { phraseId ->
                PhraseProgress.update({
                    (PhraseProgress.userId eq userId) and
                        (PhraseProgress.phraseId eq phraseId) and
                        (PhraseProgress.saveNumber eq saveNumber)
                }) {
                    it[isMastered] = true
                }
            }
        }
}

private fun ResultRow.toPhraseProgress() = PhraseProgressEntity(
    this[PhraseProgress.phraseId],
    this[PhraseProgress.userId],
    this[PhraseProgress.saveNumber],
    this[PhraseProgress.isMastered]
)

data class PhraseProgressData(
    val phraseId: Int,
    val userId: Int,
    val saveNumber: Int,
    val isMastered: Boolean
)

