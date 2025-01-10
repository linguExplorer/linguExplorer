package com.github.linguExplorer

import com.github.linguExplorer.database.DatabaseManager
import com.github.linguExplorer.repositories.PhraseAssetRepository
import com.github.linguExplorer.repositories.PhraseRepository


    fun main() {
        DatabaseManager()
        var test = PhraseAssetRepository().getPhraseAssetsByPhraseId(PhraseRepository().getPhrase(2)!!.id)
        println(test.get(0).resource)
    }
