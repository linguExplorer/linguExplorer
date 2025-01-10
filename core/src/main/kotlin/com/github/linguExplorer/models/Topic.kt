package com.github.linguExplorer.models

import org.jetbrains.exposed.sql.Table

object Topic : Table("topic") {
    val id = integer("pk_topic_id").autoIncrement()
    val name = varchar("name", 50)
    val description = varchar("description", 200).nullable()
    val next = integer("next").nullable()

    override val primaryKey = PrimaryKey(id)
}


data class TopicEntity(
    val id: Int,
    val name: String,
    val description: String? = null,
    val next: Int? = null
)
