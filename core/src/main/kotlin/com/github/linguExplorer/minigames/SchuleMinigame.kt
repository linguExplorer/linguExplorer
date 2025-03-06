package com.github.linguExplorer.minigames

import com.github.linguExplorer.database.allPhraseAssets
import com.github.linguExplorer.models.PhraseEntity
import com.github.linguExplorer.repositories.*
import com.github.linguExplorer.userId
import kotlin.random.Random

class SchuleMinigame : MinigameSystem() {
    override val topicName: String = "Schule"
    override var topicId: Int = TopicRepository().getTopicIdByName(topicName)!!

    // Listen für englische und deutsche Assets
    internal lateinit var englishAssets: List<Pair<PhraseEntity, String>>
    internal lateinit var germanAssets: List<Pair<PhraseEntity, String>>

    /**
     * Lädt eine zufällige Anzahl von Phrasen für das Minigame
     */
    override fun loadMinigamePhrases() {
        phraseList = PhraseRepository().getLimitedPhrasesByTopicNameForUser(topicId, userId, Random.nextInt(7, 11)).shuffled()
    }

    override fun loadPhrasesWithAssets(): List<Pair<PhraseEntity, String>> {
        return allPhrases.flatMap { phrase ->
            allPhraseAssets.filter { it.phraseId == phrase.id }
                .map { asset -> Pair(phrase, asset.resource) }
        }
    }

    /**
     * Lädt alle englischen Assets für die Phrasen
     * Filtert nach Assets, die "subjects_E" im Pfad haben
     */
    fun loadEnglishAssets(): List<Pair<PhraseEntity, String>> {
        englishAssets = allPhrases.flatMap { phrase ->
            allPhraseAssets.filter {
                it.phraseId == phrase.id && it.resource.contains("subjects_E")
            }.map { asset ->
                Pair(phrase, asset.resource)
            }
        }.shuffled()

        return englishAssets
    }

    /**
     * Lädt alle deutschen Assets für die Phrasen
     * Filtert nach Assets, die "subjects_D" im Pfad haben
     */
    fun loadGermanAssets(): List<Pair<PhraseEntity, String>> {
        germanAssets = phraseList.flatMap { phrase ->
            allPhraseAssets.filter {
                it.phraseId == phrase.id && it.resource.contains("subjects_D")
            }.map { asset ->
                Pair(phrase, asset.resource)
            }
        }.shuffled()

        return germanAssets
    }
}
