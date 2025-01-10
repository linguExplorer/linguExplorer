package com.github.linguExplorer.models

import org.jetbrains.exposed.sql.Table

/**
 * Dieser Kommentar trifft auf alle weiteren Klassen zu
 */
object UserProgress : Table("user_progress") {
    val userId = integer("pk_fk_user_id")
    val topicId = integer("pk_fk_topic_id")
    val isMastered = bool("is_mastered").default(false)

    override val primaryKey = PrimaryKey(userId, topicId)
}

/**
 * Datenklasse
 */
data class UserProgressEntity (
    val userId: Int,
    val topicId: Int,
    val isMastered: Boolean? = false
)

