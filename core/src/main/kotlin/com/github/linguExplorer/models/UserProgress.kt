package com.github.linguExplorer.models

import org.jetbrains.exposed.sql.Table

object UserProgress : Table("user_progress") {
    val userId = integer("pk_fk_user_id")
    val saveNumber = integer("pk_fk_save_number")
    val topicId = integer("pk_fk_topic_id")
    val isMastered = bool("is_mastered").default(false)

    override val primaryKey = PrimaryKey(userId, saveNumber, topicId)
}

data class UserProgressEntity(
    val userId: Int,
    val saveNumber: Int,  // WICHTIG: Ergänzt!
    val topicId: Int,
    val isMastered: Boolean = false
)
