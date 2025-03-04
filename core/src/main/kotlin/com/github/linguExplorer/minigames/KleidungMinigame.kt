package com.github.linguExplorer.minigames

import com.github.linguExplorer.allPhraseAssets
import com.github.linguExplorer.models.PhraseEntity
import com.github.linguExplorer.repositories.*
import com.github.linguExplorer.saveNumber
import com.github.linguExplorer.userId
import kotlin.random.Random

class KleidungMinigame : MinigameSystem() {
    override val topicName: String = "Kleidung"
    override var topicId: Int = TopicRepository().getTopicIdByName(topicName)!!

    // Zwei separate Listen für die beiden Bags
    internal lateinit var bag1: List<PhraseEntity>
    internal lateinit var bag2: List<PhraseEntity>

    /**
     * Lädt eine zufällige Anzahl von Phrasen für das Minigame und teilt sie zufällig in zwei Bags auf.
     * Jeder Bag enthält mindestens 2 Phrasen.
     */
    override fun loadMinigamePhrases() {
        phraseList = PhraseRepository().getLimitedPhrasesByTopicNameForUser(topicId, userId, saveNumber, Random.nextInt(4, 8)).shuffled()


        val splitIndex = Random.nextInt(2, phraseList.size - 1)

        bag1 = phraseList.take(splitIndex)
        bag2 = phraseList.drop(splitIndex)
    }

    override fun loadPhrasesWithAssets(): List<Pair<PhraseEntity, String>> {
        return allPhrases.flatMap { phrase ->
            allPhraseAssets.filter { it.phraseId == phrase.id }
                .map { asset -> Pair(phrase, asset.resource) }
        }
    }
}
