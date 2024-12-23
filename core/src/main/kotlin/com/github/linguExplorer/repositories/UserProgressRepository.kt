package com.github.linguExplorer.repositories

import com.github.linguExplorer.models.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserProgressRepository {
    fun getUserProgress(userId: Int, topicId: Int): UserProgressEntity? =
        transaction {
            User_Progress
                .select { (User_Progress.userId eq userId) and (User_Progress.topicId eq topicId) }
                .map { it.toUserProgress() }
                .singleOrNull()
        }


    fun addProgess(userId: Int, topic: TopicEntity?): UserProgressEntity? =
        transaction {
            if (topic != null) {
                val insertStatement = User_Progress.insert {
                    it[User_Progress.userId] = userId
                    it[User_Progress.topicId] = topic.id
                    it[User_Progress.isMastered] = false
                }
                insertStatement.resultedValues?.first()?.toUserProgress()
            } else {
                return@transaction null
            }
        }

    fun removeUserProgresses(userId: Int): Boolean =
        transaction {
            val deletedRows = Topic.deleteWhere { User_Progress.userId eq id }
            deletedRows > 0
        }

    fun changeMasteredState(userProgressEntity: UserProgressEntity?) =
        transaction {
            if (userProgressEntity != null) {
                User_Progress.update({
                    User_Progress.userId eq userProgressEntity.userId and
                        (User_Progress.topicId eq userProgressEntity.topicId)
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
            this[User_Progress.userId],
            this[User_Progress.topicId],
            this[User_Progress.isMastered],
        )
    }
}
