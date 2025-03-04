package com.github.linguExplorer.repositories

import com.github.linguExplorer.models.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class UserProgressRepository {

    fun getUserProgress(userId: Int, saveNumber: Int, topicId: Int): UserProgressEntity? =
        transaction {
            UserProgress
                .select {
                    (UserProgress.userId eq userId) and
                        (UserProgress.saveNumber eq saveNumber) and
                        (UserProgress.topicId eq topicId)
                }
                .map { it.toUserProgress() }
                .singleOrNull()
        }

    fun addProgress(userId: Int, saveNumber: Int, topicId: Int): UserProgressEntity? =
        transaction {
            val insertStatement = UserProgress.insert {
                it[UserProgress.userId] = userId
                it[UserProgress.saveNumber] = saveNumber
                it[UserProgress.topicId] = topicId
                it[UserProgress.isMastered] = false
            }
            insertStatement.resultedValues?.first()?.toUserProgress()
        }

    fun removeUserProgresses(userId: Int, saveNumber: Int): Boolean =
        transaction {
            val deletedRows = UserProgress.deleteWhere {
                (UserProgress.userId eq userId) and
                    (UserProgress.saveNumber eq saveNumber)
            }
            deletedRows > 0
        }

    fun changeMasteredState(userId: Int, saveNumber: Int, topicId: Int, isMastered: Boolean) =
        transaction {
            UserProgress.update({
                (UserProgress.userId eq userId) and
                    (UserProgress.saveNumber eq saveNumber) and
                    (UserProgress.topicId eq topicId)
            }) {
                it[UserProgress.isMastered] = isMastered
            }
        }

    /*fun checkIfMastered(topic: String, userId: Int, saveNumber: Int): Boolean {
        val phraseList = PhraseRepository().getPhrasesByTopicName(topic)
        val userProgressList = PhraseProgressRepository().getAllPhraseProgressForUser(userId, saveNumber)

        val masteredProgressList = userProgressList.filter { progress ->
            progress.isMastered && progress.phraseId in phraseList.map { it.id }
        }

        return phraseList.size == masteredProgressList.size
    }*/

    fun getLatestUserProgress(userId: Int, saveNumber: Int): UserProgressEntity? =
        transaction {
            UserProgress
                .select {
                    (UserProgress.userId eq userId) and
                        (UserProgress.saveNumber eq saveNumber)
                }
                .orderBy(UserProgress.topicId, SortOrder.DESC)
                .map { it.toUserProgress() }
                .firstOrNull()
        }

    companion object {
        private fun ResultRow.toUserProgress() = UserProgressEntity(
            this[UserProgress.userId],
            this[UserProgress.saveNumber],
            this[UserProgress.topicId],
            this[UserProgress.isMastered]
        )
    }
}
