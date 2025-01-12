package com.github.linguExplorer.minigames

import com.github.linguExplorer.database.allPhraseAssets
import com.github.linguExplorer.database.allPhrasesList
import com.github.linguExplorer.models.PhraseAssetEntity
import com.github.linguExplorer.models.PhraseEntity
import com.github.linguExplorer.repositories.PhraseAssetRepository
import com.github.linguExplorer.repositories.PhraseRepository
import com.github.linguExplorer.repositories.TopicRepository
import com.github.linguExplorer.userId
import kotlin.random.Random

class EssenMinigame {
    internal lateinit var phraseList: List<PhraseEntity>
    private lateinit var allPhrases: List<PhraseEntity>
    private var topicId = TopicRepository().getTopicIdByName("Essen")

    fun loadAllPhrases() {
        println(allPhrasesList[0])
        allPhrases = allPhrasesList.filter { phrase ->
            phrase.topicId == this.topicId
        }
    }

    fun loadMinigamePhrases() {
        phraseList = PhraseRepository().getLimitedPhrasesByTopicNameForUser(
            TopicRepository().getTopicById(topicId)!!.name, userId, Random.nextInt(10, 16)
        )
    }

    fun loadPhrasesWithAssets(): List<Pair<PhraseEntity, String>> {
        //nur bestimmte Bilder laden
        //val allowedAssets = listOf("Minigames/Essen/foods/apple.png", "Minigames/Essen/foods/bacon.png")
        return allPhrases.flatMap { phrase ->
            allPhraseAssets.filter { asset ->
                asset.phraseId == phrase.id //&& allowedAssets.contains(asset.resource) // Filtere die passenden Assets zu dieser Phrase
            }.map { asset ->
                Pair(phrase, asset.resource)  // Erstelle ein Paar aus Phrase und Asset-Resource
            }
        }
    }

}
