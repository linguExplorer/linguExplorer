package com.github.linguExplorer.models

import org.jetbrains.exposed.sql.Table

object PhraseProgress : Table("phrase_progress") {
    val phraseId = integer("pk_fk_phrase_id")
    val userId = integer("pk_fk_user_id")
    val saveNumber = integer("pk_fk_save_number")
    val isMastered = bool("is_mastered").default(false)

    override val primaryKey = PrimaryKey(phraseId, userId, saveNumber)
}

data class PhraseProgressEntity(
    val phraseId: Int,
    val userId: Int,
    val saveNumber: Int,
    val isMastered: Boolean = false
)
