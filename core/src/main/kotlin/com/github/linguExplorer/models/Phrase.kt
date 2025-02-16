package com.github.linguExplorer.models


import org.jetbrains.exposed.sql.Table

object Phrase : Table("phrase") {
    val id = integer("pk_phrase_id").autoIncrement()
    val phrase = varchar("phrase", 50)
    val translation = varchar("translation", 50)
    val definition = varchar("definition", 100).nullable()
    val topicId = integer("fk_topic_id")

    override val primaryKey = PrimaryKey(id)
}

// Datenklasse für Phrase
data class PhraseEntity(
    val id: Int,
    val phrase: String,
    val translation: String,
    val definition: String? = null,
    val topicId: Int?
)

