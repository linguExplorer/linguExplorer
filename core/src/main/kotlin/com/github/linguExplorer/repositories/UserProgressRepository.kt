package com.github.linguExplorer.repositories

import com.github.linguExplorer.models.Topic
import com.github.linguExplorer.models.TopicEntity
import org.jetbrains.exposed.sql.transactions.transaction
import com.github.linguExplorer.models.UserProgressEntity
import com.github.linguExplorer.models.User_Progress
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserProgressRepository {
    fun getUserProgress(userId: Int, topic: TopicEntity?): UserProgressEntity? =
        transaction {
            User_Progress
                .select { (User_Progress.userId eq userId) and (User_Progress.topicId eq topic!!.id) }
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


    companion object {
        private fun ResultRow.toUserProgress() = UserProgressEntity(
            this[User_Progress.userId],
            this[User_Progress.topicId],
            this[User_Progress.isMastered],
        )
    }
}
