package com.github.linguExplorer.minigames

import com.github.linguExplorer.allPhraseAssets
import com.github.linguExplorer.models.PhraseEntity
import com.github.linguExplorer.repositories.*
import com.github.linguExplorer.saveNumber
import com.github.linguExplorer.userId
import kotlin.random.Random

class EssenMinigame : MinigameSystem() {
    override val topicName: String = "Essen"
    override var topicId: Int = TopicRepository().getTopicIdByName(topicName)!!

    /**
     * Lädt eine zufällige Anzahl von Phrasen für das Minigame
     */
    override fun loadMinigamePhrases() {
        phraseList = PhraseRepository().getLimitedPhrasesByTopicNameForUser(topicId, userId, saveNumber, Random.nextInt(4, 8)).shuffled()
    }

    override fun loadPhrasesWithAssets(): List<Pair<PhraseEntity, String>> {
        return allPhrases.flatMap { phrase ->
            allPhraseAssets.filter { it.phraseId == phrase.id }
                .map { asset -> Pair(phrase, asset.resource) }
        }
    }
}
