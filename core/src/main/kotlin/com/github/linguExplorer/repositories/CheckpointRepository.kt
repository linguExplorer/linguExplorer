package com.github.linguExplorer.repositories

import com.github.linguExplorer.models.Checkpoint
import com.github.linguExplorer.models.CheckpointEntity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Timestamp

class CheckpointRepository {

    fun getCheckpoint(userId: Int, saveNumber: Int): CheckpointEntity? =
        transaction {
            Checkpoint
                .select { (Checkpoint.userId eq userId) and (Checkpoint.saveNumber eq saveNumber) }
                .map { it.toCheckpointEntity() }
                .singleOrNull()
        }

    fun getAllCheckpointsForUser(userId: Int): List<CheckpointEntity> =
        transaction {
            Checkpoint
                .select { Checkpoint.userId eq userId }
                .map { it.toCheckpointEntity() }
        }

    fun addCheckpoint(userId: Int, saveNumber: Int, balance: Int, positionX: Float, positionY: Float, currentTime: Timestamp) =
        transaction {
            val existing = getCheckpoint(userId, saveNumber)

            if (existing != null) {
                throw IllegalArgumentException("Ein Checkpoint mit dieser Benutzer-ID und Speichernummer existiert bereits.")
            }

            val insertStatement = Checkpoint.insert {
                it[Checkpoint.userId] = userId
                it[Checkpoint.saveNumber] = saveNumber
                it[Checkpoint.balance] = balance
                it[Checkpoint.positionX] = positionX
                it[Checkpoint.positionY] = positionY
                it[Checkpoint.currentTime] = currentTime.toInstant()
            }

            insertStatement.resultedValues?.first()?.toCheckpointEntity()
        }

    fun updateCheckpoint(userId: Int, saveNumber: Int, balance: Int? = null, positionX: Float? = null, positionY: Float? = null, currentTime: Timestamp? = null) =
        transaction {
            val existing = getCheckpoint(userId, saveNumber)
                ?: throw IllegalArgumentException("Es wurde kein Checkpoint mit dieser Benutzer-ID und Speichernummer gefunden.")

            Checkpoint.update({ (Checkpoint.userId eq userId) and (Checkpoint.saveNumber eq saveNumber) }) {
                balance?.let { balance -> it[Checkpoint.balance] = balance }
                positionX?.let { positionX -> it[Checkpoint.positionX] = positionX }
                positionY?.let { positionY -> it[Checkpoint.positionY] = positionY }
                currentTime?.let { currentTime -> it[Checkpoint.currentTime] = currentTime.toInstant() }
            }
        }

    companion object {
        private fun ResultRow.toCheckpointEntity() = CheckpointEntity(
            this[Checkpoint.userId],
            this[Checkpoint.saveNumber],
            this[Checkpoint.balance],
            this[Checkpoint.positionX],
            this[Checkpoint.positionY],
            this[Checkpoint.currentTime]
        )
    }
}
