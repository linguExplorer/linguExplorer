package com.github.linguExplorer.models

import org.jetbrains.exposed.sql.Table

object PhraseAsset : Table("phrase_asset") {
    val id = integer("pk_asset_id").autoIncrement()
    val resource = varchar("resource", 500)
    val phraseId = integer("fk_phrase_id")

    override val primaryKey = PrimaryKey(id)
}


data class PhraseAssetEntity(
    val id: Int,
    val resource: String,
    val phraseId: Int
)
