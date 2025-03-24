package com.github.linguExplorer.models

import org.jetbrains.exposed.sql.Table

object PhraseProgressHistory : Table("phrase_progress_history") {
    val progressId = integer("pk_progress_id").autoIncrement()
    val userId = integer("fk_user_id")
    val saveNumber = integer("fk_save_number")  // Ergänzt für die Referenz zu User
    val phraseId = integer("fk_phrase_id")
    val correct = bool("correct")

    override val primaryKey = PrimaryKey(progressId)
}

data class PhraseProgressHistoryEntity(
    val progressId: Int,
    val userId: Int,
    val saveNumber: Int,  // Ergänzt!
    val phraseId: Int,
    val correct: Int
)
