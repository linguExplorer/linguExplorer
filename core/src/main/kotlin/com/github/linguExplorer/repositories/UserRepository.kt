package com.github.linguExplorer.repositories

import com.github.linguExplorer.models.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserRepository {

    fun getUser(id: Int, saveNumber: Int): UserEntity? =
        transaction {
            User
                .select { (User.id eq id) and (User.saveNumber eq saveNumber) }
                .map { it.toUser() }
                .singleOrNull()
        }

    fun getAllUsersById(id: Int): List<UserEntity> =
        transaction {
            User
                .select { User.id eq id }
                .map { it.toUser() }
        }

    fun addUser(id: Int, saveNumber: Int, name: String = "Blob"): UserEntity? =
        transaction {
            val insertStatement = User.insert {
                it[User.id] = id
                it[User.saveNumber] = saveNumber
                it[User.name] = name
            }
            insertStatement.resultedValues?.first()?.toUser()
        }

    /*fun deleteUserWithDependencies(userId: Int, saveNumber: Int) {
        transaction {
            PhraseProgressHistory.deleteWhere {
                (PhraseProgressHistory.userId eq userId) and (PhraseProgressHistory.saveNumber eq saveNumber)
            }

            PhraseProgress.deleteWhere {
                (PhraseProgress.userId eq userId) and (PhraseProgress.saveNumber eq saveNumber)
            }

            UserProgress.deleteWhere {
                (UserProgress.userId eq userId) and (UserProgress.saveNumber eq saveNumber)
            }

            User.deleteWhere {
                (User.id eq userId) and (User.saveNumber eq saveNumber)
            }
        }
    }*/

    companion object {
        private fun ResultRow.toUser() = UserEntity(
            this[User.id],
            this[User.saveNumber],  // WICHTIG: saveNumber hinzugefügt!
            this[User.name]
        )
    }
}
