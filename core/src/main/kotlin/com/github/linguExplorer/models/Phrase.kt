package com.github.linguExplorer.models


import org.jetbrains.exposed.sql.Table

object Phrases : Table() {
    val phraseId = integer("pk_phrase_id").autoIncrement()
    val phrase = varchar("phrase", 50)
    val translation = varchar("translation", 50)
    val definition = varchar("definition", 100).nullable()
    val topicId = integer("topic_id").nullable()

    override val primaryKey = PrimaryKey(phraseId)
}

// Datenklasse für Phrase
data class Phrase(
    val phraseId: Int,
    val phrase: String,
    val translation: String,
    val definition: String?,
    val topicId: Int?
)

