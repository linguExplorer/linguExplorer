package com.github.linguExplorer.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant


object Checkpoint : Table("checkpoint") {
    val id = integer("pk_fk_user_id")
    val balance = integer("current_balance")
    val positionX = float("current_position_x")
    val positionY = float("current_position_y")
    val currentTime = timestamp("currenttime")


    override val primaryKey = PrimaryKey(id)
}

/**
 * Datenklasse
 */
data class CheckpointEntity(
    val id: Int,
    val balance: Int,
    val positionX: Float,
    val positionY: Float,
    val currentTime: Instant
)

