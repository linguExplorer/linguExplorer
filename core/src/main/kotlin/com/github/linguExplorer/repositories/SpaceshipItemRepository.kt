package com.github.linguExplorer.repositories

import com.github.linguExplorer.models.SpaceshipItem
import com.github.linguExplorer.models.SpaceshipItemEntity
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class SpaceshipItemRepository {
    fun addSpaceshipItem(name: String, description: String, price: Int, resource: String): SpaceshipItemEntity? =
        transaction {
            val insertStatement = SpaceshipItem.insert {
                it[SpaceshipItem.name] = name
                it[SpaceshipItem.description] = description
                it[SpaceshipItem.price] = price
                it[SpaceshipItem.resource] = resource
            }
            insertStatement.resultedValues?.first()?.toSpaceshipItem()
        }

    fun getSpaceshipItemById(id: Int): SpaceshipItemEntity? =
        transaction {
            SpaceshipItem.select { SpaceshipItem.id eq id }
                .map { it.toSpaceshipItem() }
                .singleOrNull()
        }

    fun removeSpaceshipItem(id: Int): Boolean =
        transaction {
            val deletedRows = SpaceshipItem.deleteWhere { SpaceshipItem.id eq id }
            deletedRows > 0
        }

    private fun ResultRow.toSpaceshipItem() = SpaceshipItemEntity(
        id = this[SpaceshipItem.id],
        name = this[SpaceshipItem.name],
        description = this[SpaceshipItem.description],
        price = this[SpaceshipItem.price],
        resource = this[SpaceshipItem.resource]
    )
}
