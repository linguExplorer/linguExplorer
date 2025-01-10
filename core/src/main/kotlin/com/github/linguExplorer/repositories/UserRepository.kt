package com.github.linguExplorer.repositories

import com.github.linguExplorer.models.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
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

    fun addUser(id: Int, name: String = "Blobi"): UserEntity? =
        transaction {
            val insertStatement = User.insert {
                it[User.id] = id
                it[User.name] = name
            }
            insertStatement.resultedValues?.first()?.toUser()
        }

    fun deleteUserWithDependencies(userId: Int) {
        transaction {
            PhraseProgressHistory.deleteWhere { PhraseProgressHistory.userId eq userId }

            PhraseProgress.deleteWhere { PhraseProgress.userId eq userId }
            UserProgress.deleteWhere { UserProgress.userId eq userId }
            User.deleteWhere { User.id eq userId }
        }
    }

    companion object {
        private fun ResultRow.toUser() = UserEntity(
            this[User.id],
            this[User.name]
        )
    }
}
