package com.github.linguExplorer.repositories

import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import com.github.linguExplorer.models.NPCEntity
import com.github.linguExplorer.models.NPC
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select

class NPCRepository {
    fun getNPC(id: Int): NPCEntity? =
        transaction {
            NPC
                .select { NPC.id eq id }
                .map { it.toUser() }
                .singleOrNull()
        }

    fun addNPC(id: Int, name: String, role: String, resource: String): NPCEntity? =
        transaction {
            val insertStatement = NPC.insert {
                it[NPC.id] = id
                it[NPC.name] = name
                it[NPC.role] = role
                it[NPC.resource] = resource
            }
            insertStatement.resultedValues?.first()?.toUser()
        }

    fun removeNPC(id: Int): Boolean =
        transaction {
            val deletedRows = NPC.deleteWhere { NPC.id eq id }
            deletedRows > 0
        }


    companion object {
        private fun ResultRow.toUser() = NPCEntity(
            this[NPC.id],
            this[NPC.name],
            this[NPC.role],
            this[NPC.resource]
        )
    }
}
