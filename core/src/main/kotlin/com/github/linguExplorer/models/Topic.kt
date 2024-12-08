package com.github.linguExplorer.models

import org.jetbrains.exposed.sql.Table

object Topics : Table() {
    val topicId = integer("pk_topic_id").autoIncrement()
    val name = varchar("name", 50)
    val description = varchar("description", 200)
    val next = integer("next").nullable()

    override val primaryKey = PrimaryKey(topicId)
}


data class Topic(
    val topicId: Int,
    val name: String,
    val description: String,
    val int: String?
)
