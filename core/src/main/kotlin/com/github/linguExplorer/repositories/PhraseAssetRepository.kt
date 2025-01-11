package com.github.linguExplorer.repositories

import com.github.linguExplorer.models.PhraseAssetEntity
import com.github.linguExplorer.models.Topic
import com.github.linguExplorer.models.PhraseAsset
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class PhraseAssetRepository {

    fun addPhraseAsset(resource: String, phraseId: Int): PhraseAssetEntity? =
        transaction {
            val insertStatement = PhraseAsset.insert {
                it[PhraseAsset.resource] = resource
                it[PhraseAsset.phraseId] = phraseId
            }
            insertStatement.resultedValues?.first()?.toPhraseAsset()
        }


    fun getPhraseAssetById(assetId: Int): PhraseAssetEntity? {
        return transaction {
            PhraseAsset.select { PhraseAsset.id eq assetId }
                .map { it.toPhraseAsset() }
                .singleOrNull()
        }
    }

    fun getPhraseAssetsByPhraseId(phraseId: Int): List<PhraseAssetEntity> {
        return transaction {
            PhraseAsset.select { PhraseAsset.phraseId eq phraseId }
                .map { it.toPhraseAsset() }
        }
    }

    fun getAllPhraseAssets(): List<PhraseAssetEntity> {
        return transaction {
            PhraseAsset.selectAll()
                .map { it.toPhraseAsset() }
        }
    }

    fun updatePhraseAsset(assetId: Int, newResource: String): Boolean {
        return transaction {
            PhraseAsset.update({ PhraseAsset.id eq assetId }) {
                it[resource] = newResource
            } > 0
        }
    }

    fun deletePhraseAsset(assetId: Int): Boolean {
        return transaction {
            PhraseAsset.deleteWhere { PhraseAsset.id eq assetId } > 0
        }
    }

    private fun ResultRow.toPhraseAsset() = PhraseAssetEntity(
        id = this[PhraseAsset.id],
        resource = this[PhraseAsset.resource],
        phraseId = this[PhraseAsset.phraseId]
    )
}
