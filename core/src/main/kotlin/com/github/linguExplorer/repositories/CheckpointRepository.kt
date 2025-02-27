package com.github.linguExplorer.repositories

import com.github.linguExplorer.models.Checkpoint
import com.github.linguExplorer.models.CheckpointEntity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.Timestamp

class CheckpointRepository {

    fun getCheckpoint(userId: Int): CheckpointEntity? =
        transaction {
            Checkpoint
                .selectAll().where { Checkpoint.id eq userId }
                .map { it.toCheckpointEntity() }
                .singleOrNull()
        }

    fun upsertCheckpoint(userId: Int, balance: Int? = null, positionX: Float? = null, positionY: Float? = null, currentTime: Timestamp? = null) = transaction {
        val existing = getCheckpoint(userId)

        if (existing == null) {
            // Insert new checkpoint (nur wenn alle Werte vorhanden sind)
            if (balance != null && positionX != null && positionY != null && currentTime != null) {
                val insertStatement = Checkpoint.insert {
                    it[id] = userId
                    it[Checkpoint.balance] = balance
                    it[Checkpoint.positionX] = positionX
                    it[Checkpoint.positionY] = positionY
                    it[Checkpoint.currentTime] = currentTime.toInstant()
                }
                insertStatement.resultedValues?.first()?.toCheckpointEntity()
            } else {
                throw IllegalArgumentException("Beim Einfügen eines neuen Checkpoints müssen alle Werte angegeben werden.")
            }
        } else {
            Checkpoint.update({ Checkpoint.id eq userId }) {
                balance?.let { balance -> it[Checkpoint.balance] = balance }
                positionX?.let { positionX -> it[Checkpoint.positionX] = positionX }
                positionY?.let { positionY -> it[Checkpoint.positionY] = positionY }
                currentTime?.let { currentTime -> it[Checkpoint.currentTime] = currentTime.toInstant() }
            }
        }
    }

    companion object {
        private fun ResultRow.toCheckpointEntity() = CheckpointEntity(
            this[Checkpoint.id],
            this[Checkpoint.balance],
            this[Checkpoint.positionX],
            this[Checkpoint.positionY],
            this[Checkpoint.currentTime]
        )
    }
}
