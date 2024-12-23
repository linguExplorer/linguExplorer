package com.github.linguExplorer.repositories

import com.github.linguExplorer.models.PhraseProgress
import com.github.linguExplorer.models.PhraseProgressEntity
import com.github.linguExplorer.models.UserProgressEntity
import com.github.linguExplorer.models.User_Progress
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class PhraseProgressRepository {

    fun addPhraseProgress(phraseId: Int, userId: Int, isMastered: Boolean = false) =
        transaction {
            PhraseProgress.insert {
                it[this.phraseId] = phraseId
                it[this.userId] = userId
                it[this.isMastered] = isMastered
            }
        }

    fun getPhraseProgress(phraseId: Int, userId: Int): PhraseProgressEntity? =
        transaction {
            PhraseProgress
                .select { (PhraseProgress.phraseId eq phraseId) and (PhraseProgress.userId eq userId) }
                .map { it.toPhraseProgress() }
                .singleOrNull()
        }

    fun deletePhraseProgresses(phraseId: Int, userId: Int): Boolean =
        transaction {
            val deletedRows = PhraseProgress.deleteWhere {
                (PhraseProgress.phraseId eq phraseId) and (PhraseProgress.userId eq userId)
            }
            deletedRows > 0
        }


    fun getAllPhraseProgressForUser(userId: Int): List<PhraseProgressEntity> =
        transaction {
            PhraseProgress
                .select { PhraseProgress.userId eq userId }
                .map { it.toPhraseProgress() }
        }


    fun changeMasteredState(userId: Int, phraseId: Int) =
        transaction {
                PhraseProgress.update({
                    PhraseProgress.userId eq userId and
                        (PhraseProgress.phraseId eq phraseId)
                }) {
                    it[isMastered] = true
                }
            }
        }


    private fun ResultRow.toPhraseProgress() = PhraseProgressEntity(
        this[PhraseProgress.phraseId],
        this[PhraseProgress.userId],
        this[PhraseProgress.isMastered]
    )
