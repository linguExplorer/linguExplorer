package com.github.linguExplorer.repositories

import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import com.github.linguExplorer.models.UserEntity
import com.github.linguExplorer.models.User
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select

class UserRepository {
    fun getUser(id: Int): UserEntity? =
        transaction {
            User
                .select { User.id eq id }
                .map { it.toUser() }
                .singleOrNull()
        }

    fun addUser(id: Int, name: String): UserEntity? =
        transaction {
            val insertStatement = User.insert {
                it[User.id] = id
                it[User.name] = name
            }
            insertStatement.resultedValues?.first()?.toUser()
        }

    fun removeUser(id: Int): Boolean =
        transaction {
            val deletedRows = User.deleteWhere { User.id eq id }
            deletedRows > 0
        }


    companion object {
        private fun ResultRow.toUser() = UserEntity(
            this[User.id],
            this[User.name]
        )
    }
}
