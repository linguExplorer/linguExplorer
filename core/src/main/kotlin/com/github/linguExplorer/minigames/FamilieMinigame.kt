package com.github.linguExplorer.minigames

import com.github.linguExplorer.database.allPhraseAssets
import com.github.linguExplorer.models.PhraseEntity
import com.github.linguExplorer.repositories.PhraseRepository
import com.github.linguExplorer.repositories.TopicRepository
import com.github.linguExplorer.saveNumber
import com.github.linguExplorer.userId
import kotlin.random.Random

class FamilieMinigame : MinigameSystem() {
    override val topicName: String = "Familie"
    override var topicId: Int = TopicRepository().getTopicIdByName(topicName)!!

    override fun loadMinigamePhrases() {
        phraseList = PhraseRepository()
            .getLimitedPhrasesByTopicNameForUser(topicId, userId, saveNumber, Random.nextInt(11, 16))

    }

    override fun loadPhrasesWithAssets(): List<Pair<PhraseEntity, String>> {
        phraseList.forEach { println(it.phrase) }
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

