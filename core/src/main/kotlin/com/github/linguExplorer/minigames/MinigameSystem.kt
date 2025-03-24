package com.github.linguExplorer.minigames

import com.github.linguExplorer.*
import com.github.linguExplorer.models.PhraseEntity
import com.github.linguExplorer.models.PhraseProgressHistoryEntity
import com.github.linguExplorer.repositories.*

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


    fun storePhraseData() {
        val progressRepo = PhraseProgressRepository()
        val historyRepo = PhraseProgressHistoryRepository()

        val userProgress = progressRepo.getAllPhraseProgressForUser(userId, saveNumber)
        val userHistory = historyRepo.getAllEntriesForUser(userId, saveNumber)
        val phrasesGameHistory = mutableListOf<Pair<Int, Boolean>>()
        val phrasesProgress = mutableListOf<PhraseProgressData>() // Statt Triple nutzen wir jetzt ein Quadruple
        val phraseStateUpdates = mutableListOf<Int>()
        val phraseIndexList = mutableListOf<Double>()

        capturedPhrases.forEach { (phrase, correctSet) ->
            val relevantProgress = userProgress.find { it.phraseId == phrase.id }
            val correctIndex = historyRepo.calculateCorrectIndex(phrase.id, userHistory)
            phraseIndexList.add(correctIndex)

            if (relevantProgress == null) {
                phrasesProgress.add(PhraseProgressData(phrase.id, userId, saveNumber, false)) // saveNumber wird jetzt hinzugefügt
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
        historyRepo.addPhraseProgressHistories(userId, saveNumber, phrasesGameHistory) // saveNumber wird hier ebenfalls übergeben
        if (phraseStateUpdates.isNotEmpty()) {
            progressRepo.changeMultipleMasteredStates(userId, saveNumber, phraseStateUpdates)
        }

        if (UserProgressRepository().getUserProgress(userId, saveNumber, topicId) == null) {
            UserProgressRepository().addProgress(userId, saveNumber, topicId)
        }

        updateUserInformation(userHistory)
    }


    fun updateUserInformation(userHistory: List<PhraseProgressHistoryEntity>) {
        val progressRepo = PhraseProgressRepository()
        val historyRepo = PhraseProgressHistoryRepository()
        val phraseList = allPhrasesList.filter { it.topicId == this.topicId }

        var totalScore = 0.0
        var count = 0

        phraseList.forEach { phrase ->
            val correctIndex = historyRepo.calculateCorrectIndex(phrase.id, userHistory)
            var score = 0.0
            if (correctIndex >= phraseIndex) {
                score = 1.0
            } else if (correctIndex == -1.0) {
                score = 0.0
            } else {
                score = correctIndex / phraseIndex
            }

            totalScore += score
            count++
        }
        if(topicId == currentTopic.id) {
            topicProgress = if (count > 0) totalScore / count else 0.0
            println("HII")
            println(topicProgress)
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
