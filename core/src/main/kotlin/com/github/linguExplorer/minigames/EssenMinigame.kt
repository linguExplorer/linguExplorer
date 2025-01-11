package com.github.linguExplorer.minigames

import com.github.linguExplorer.database.allPhraseAssets
import com.github.linguExplorer.database.allPhrasesList
import com.github.linguExplorer.database.phraseIndex
import com.github.linguExplorer.models.PhraseAssetEntity
import com.github.linguExplorer.models.PhraseEntity
import com.github.linguExplorer.repositories.*
import com.github.linguExplorer.userId
import kotlin.random.Random

class EssenMinigame {
    internal lateinit var phraseList: List<PhraseEntity>
    private lateinit var allPhrases: List<PhraseEntity>
    private val capturedPhrases = mutableMapOf<PhraseEntity, Boolean>()
    private var topicId = TopicRepository().getTopicIdByName("Essen")

    /**
     * Die Funktion greift auf allPhrasesList (aus LoadDatabaseLists) und filtert sie nach der Topic Id des Minigames
     * ich muss das so machen, weil ich sonst zu viele selects hätte und dadurch die Wartezeit zu lang ist :(
     */
    fun loadAllPhrases() {
        println(allPhrasesList[0])
        allPhrases = allPhrasesList.filter { phrase ->
            phrase.topicId == this.topicId
        }
    }

    /**
     * Verwendet die getLimitedPhrasesByTopicForUser method um so die Liste der Minigames,
     * die richtig sein müssen, in einer seperaten Liste zu haben
     *
     * Wird gebraucht, damit man checken kann ob es sich bei einer ausgewählten Phrase um eine richtige handelt
     * (also quasi ist eine Phrase aus allPhrases in phraseList)
     *
     * TODO: ZU VIELE SELECTS
     */
    fun loadMinigamePhrases() {
        phraseList = PhraseRepository().getLimitedPhrasesByTopicNameForUser(
            TopicRepository().getTopicById(topicId)!!.name, userId, Random.nextInt(10, 16)
        )
    }

    /**
     * Mapped die Phrase mit ihrem Assetpfad
     */
    fun loadPhrasesWithAssets(): List<Pair<PhraseEntity, String>> {
        return allPhrases.flatMap { phrase ->
            allPhraseAssets.filter { asset ->
                asset.phraseId == phrase.id
            }.map { asset ->
                Pair(phrase, asset.resource)
            }
        }
    }

    /*
    fun phraseCheck(phrase: PhraseEntity, correct: Boolean) {
        val progressRepo = PhraseProgressRepository()
        val historyRepo = PhraseProgressHistoryRepository()

        // Lade Fortschritt und berechne Correct-Index in einem Schritt
        val progress = progressRepo.getPhraseProgress(phrase.id, userId)
        val correctIndex = progress.let { historyRepo.calculateCorrectIndex(userId, phrase.id) }

        if (progress == null) {
            progressRepo.addPhraseProgress(phrase.id, userId)
        }

        historyRepo.addPhraseProgress(userId, phrase.id, correct)

        if (correct) {
            if (correctIndex > phraseIndex && (progress == null || !progress.isMastered)) {
                progressRepo.changeMasteredState(userId, phrase.id)
                println("Du hast die Phrase gemeistert!")
            }
        } else {
            println("Falsch!")
        }
    }*/

    fun phraseCheck(phrase: PhraseEntity, correct: Boolean) {
        capturedPhrases[phrase] = correct
        checkCompletion() // Check if the game is complete
    }

    fun isGameComplete(): Boolean {
        return checkCompletion()
    }

    private fun checkCompletion(): Boolean {
        val allCorrectCaptured = phraseList.all { capturedPhrases[it] == true }
        return allCorrectCaptured
    }


    fun storePhraseData() {
        val progressRepo = PhraseProgressRepository()
        val historyRepo = PhraseProgressHistoryRepository()

        capturedPhrases.forEach { (phrase, correct) ->
            // Lade Fortschritt und berechne Correct-Index in einem Schritt
            val progress = progressRepo.getPhraseProgress(phrase.id, userId)
            val correctIndex = progress?.let { historyRepo.calculateCorrectIndex(userId, phrase.id) } ?: 0.0

            if (progress == null) {
                progressRepo.addPhraseProgress(phrase.id, userId)
            }

            historyRepo.addPhraseProgress(userId, phrase.id, correct)

            if (correct) {
                if (correctIndex > phraseIndex && (progress == null || !progress.isMastered)) {
                    progressRepo.changeMasteredState(userId, phrase.id)
                    println("Du hast die Phrase gemeistert!")
                }
            } else {
                println("Falsch!")
            }
        }
    }

}
