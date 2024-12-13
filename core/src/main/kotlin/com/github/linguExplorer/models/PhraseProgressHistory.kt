package com.github.linguExplorer.models

import org.jetbrains.exposed.sql.Table

/**
 * Dieser Kommentar trifft auf alle weiteren Klassen zu
 */
object PhraseProgressHistory : Table("phrase_progress_history") {
    val progressId = integer("pk_progress_id").autoIncrement()
    val userId = integer("fk_user_id")
    val phraseId = integer("fk_phrase_id")
    val correct = bool("correct")

    override val primaryKey = PrimaryKey(progressId)
}

/**
 * Datenklasse
 */
data class PhraseProgressHistoryEntity(
    val progressId: Int,
    val userId: Int,
    val phraseId: Int,
    val correct: Int
)

