package com.github.linguExplorer.repositories

import com.github.linguExplorer.models.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserProgressRepository {
    fun getUserProgress(userId: Int, topicId: Int): UserProgressEntity? =
        transaction {
            UserProgress
                .select { (UserProgress.userId eq userId) and (UserProgress.topicId eq topicId) }
                .map { it.toUserProgress() }
                .singleOrNull()
        }


    fun addProgess(userId: Int, topic: TopicEntity?): UserProgressEntity? =
        transaction {
            if (topic != null) {
                val insertStatement = UserProgress.insert {
                    it[UserProgress.userId] = userId
                    it[UserProgress.topicId] = topic.id
                    it[UserProgress.isMastered] = false
                }
                insertStatement.resultedValues?.first()?.toUserProgress()
            } else {
                return@transaction null
            }
        }

    fun removeUserProgresses(userId: Int): Boolean =
        transaction {
            val deletedRows = Topic.deleteWhere { UserProgress.userId eq id }
            deletedRows > 0
        }

    fun changeMasteredState(userProgressEntity: UserProgressEntity?) =
        transaction {
            if (userProgressEntity != null) {
                UserProgress.update({
                    UserProgress.userId eq userProgressEntity.userId and
                        (UserProgress.topicId eq userProgressEntity.topicId)
                }) {
                    it[isMastered] = true
                }
            } else {
                return@transaction null
            }
        }

    fun checkIfMastered(topic: String, userId: Int): Boolean {
            val phraseList = PhraseRepository().getPhrasesByTopicName(topic)
            val userProgressList = PhraseProgressRepository().getAllPhraseProgressForUser(userId)

            val masteredProgressList = userProgressList.filter { progress ->
                progress.isMastered && progress.phraseId in phraseList.map { it.id }
            }

            return phraseList.size == masteredProgressList.size
        }



    companion object {
        private fun ResultRow.toUserProgress() = UserProgressEntity(
            this[UserProgress.userId],
            this[UserProgress.topicId],
            this[UserProgress.isMastered],
        )
    }
}
