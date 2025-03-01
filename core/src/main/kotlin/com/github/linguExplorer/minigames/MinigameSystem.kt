package com.github.linguExplorer.minigames

import com.github.linguExplorer.database.allPhraseAssets
import com.github.linguExplorer.database.allPhrasesList
import com.github.linguExplorer.database.phraseIndex
import com.github.linguExplorer.models.PhraseEntity
import com.github.linguExplorer.repositories.*
import com.github.linguExplorer.userId
import kotlin.random.Random

// Elternklasse Minigame
abstract class MinigameSystem {
    internal lateinit var phraseList: List<PhraseEntity>
    internal lateinit var allPhrases: List<PhraseEntity>
    internal val capturedPhrases = mutableMapOf<PhraseEntity, MutableSet<Boolean>>()
    abstract val topicName: String
    abstract var topicId: Int

    /**
     * Lädt alle Phrasen des spezifischen Themas und mischt sie
     */
    fun loadAllPhrases() {
        val filteredPhrases = allPhrasesList.filter { it.topicId == this.topicId }

        val mandatoryPhrases = phraseList.filter { it in filteredPhrases }
        val remainingPhrases = filteredPhrases.filter { it !in mandatoryPhrases }.shuffled()

        allPhrases = (mandatoryPhrases + remainingPhrases).take(16).shuffled()
    }

    fun phraseCheck(phrase: PhraseEntity, correct: Boolean) {
        capturedPhrases.getOrPut(phrase) { mutableSetOf() }.add(correct)
    }

    /**
     * Prüft, ob das Spiel abgeschlossen ist
     */
    fun isGameComplete(): Boolean {
        return phraseList.all { capturedPhrases[it]?.contains(true) == true }
    }


    /**
     * Speichert die erfassten Daten in die Datenbank
     */
    fun storePhraseData() {
        val progressRepo = PhraseProgressRepository()
        val historyRepo = PhraseProgressHistoryRepository()

        val userProgress = progressRepo.getAllPhraseProgressForUser(userId)
        val userHistory = historyRepo.getAllEntriesForUser(userId)
        val phrasesGameHistory = mutableListOf<Pair<Int, Boolean>>()
        val phrasesProgress = mutableListOf<Triple<Int, Int, Boolean>>()
        val phraseStateUpdates = mutableListOf<Int>()

        capturedPhrases.forEach { (phrase, correctSet) ->
            val relevantProgress = userProgress.find { it.phraseId == phrase.id }
            val correctIndex = historyRepo.calculateCorrectIndex(phrase.id, userHistory)

            if (relevantProgress == null) {
                phrasesProgress.add(Triple(phrase.id, userId, false))
            }

            // Beide möglichen Werte aus dem Set verarbeiten (max. 1x true und 1x false pro Phrase)
            correctSet.forEach { correct ->
                phrasesGameHistory.add(Pair(phrase.id, correct))

                if (correct && correctIndex > phraseIndex && relevantProgress?.isMastered == false) {
                    phraseStateUpdates.add(phrase.id)
                    println("Du hast die Phrase gemeistert!")
                }
            }
        }

        progressRepo.addMultiplePhraseProgress(phrasesProgress)
        historyRepo.addPhraseProgressHistories(userId, phrasesGameHistory)
        if (phraseStateUpdates.isNotEmpty()) {
            progressRepo.changeMultipleMasteredStates(userId, phraseStateUpdates)
        }

        if (UserProgressRepository().getUserProgress(userId, topicId) == null) {
            UserProgressRepository().addProgess(userId, topicId)
        }
    }


    /**
     * Abstrakte Methode zur Phrasen-Ladung. Die Subklassen bestimmen die Details.
     */
    abstract fun loadMinigamePhrases()

    /**
     * Lädt die Phrasen mit ihren zugehörigen Assets
     */
    abstract fun loadPhrasesWithAssets(): List<Pair<PhraseEntity, String>>
}
