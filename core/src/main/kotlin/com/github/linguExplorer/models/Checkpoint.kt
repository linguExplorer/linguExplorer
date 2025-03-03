package com.github.linguExplorer.models

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

object Checkpoint : Table("checkpoint") {
    val userId = integer("pk_fk_user_id")
    val saveNumber = integer("pk_fk_save_number")
    val balance = integer("current_balance")
    val positionX = float("current_position_x")
    val positionY = float("current_position_y")
    val currentTime = timestamp("currenttime").default(Instant.now())

    override val primaryKey = PrimaryKey(userId, saveNumber)
}

/**
 * Datenklasse für Checkpoint
 */
data class CheckpointEntity(
    val userId: Int,
    val saveNumber: Int,
    val balance: Int,
    val positionX: Float,
    val positionY: Float,
    val currentTime: Instant
)
