package com.github.linguExplorer.minigames

import com.github.linguExplorer.database.allPhraseAssets
import com.github.linguExplorer.models.PhraseEntity
import com.github.linguExplorer.repositories.PhraseRepository
import com.github.linguExplorer.repositories.TopicRepository
import com.github.linguExplorer.userId
import kotlin.random.Random

class FamilieMinigame : MinigameSystem() {
    override val topicName: String = "Essen"
    override var topicId: Int = TopicRepository().getTopicIdByName(topicName)!!

    override fun loadMinigamePhrases() {
        phraseList = PhraseRepository()
            .getLimitedPhrasesByTopicNameForUser(topicId, userId, Random.nextInt(4, 5))
            .shuffled()

    }

    override fun loadPhrasesWithAssets(): List<Pair<PhraseEntity, String>> {
        return phraseList.flatMap { phrase ->
            allPhraseAssets.filter { it.phraseId == phrase.id }
                .map { asset -> Pair(phrase, asset.resource) }
        }
    }

    /*
    fun loadPhrasesWithTranslations(): List<Pair<PhraseEntity, String>> {
        return phraseList.map { phrase ->
            Pair(phrase, phrase.translation)
        }
    }*/
}

