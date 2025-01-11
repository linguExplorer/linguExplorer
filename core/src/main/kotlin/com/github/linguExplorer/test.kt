package com.github.linguExplorer

import com.github.linguExplorer.database.DatabaseManager
import com.github.linguExplorer.models.PhraseProgress
import com.github.linguExplorer.repositories.PhraseAssetRepository
import com.github.linguExplorer.repositories.PhraseProgressHistoryRepository
import com.github.linguExplorer.repositories.PhraseProgressRepository
import com.github.linguExplorer.repositories.PhraseRepository


    fun main() {
        DatabaseManager()
        var test = PhraseProgressRepository().getAllPhraseProgressForUser(userId)
        test.forEach { phrase ->
            println("${PhraseRepository().getPhrase(phrase.phraseId)} und das ist ${phrase.isMastered}")
            println("${PhraseProgressHistoryRepository().calculateCorrectIndex(userId, phrase.phraseId)}")
        }
    }
