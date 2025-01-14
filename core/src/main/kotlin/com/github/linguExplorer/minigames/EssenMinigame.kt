package com.github.linguExplorer.minigames

import com.github.linguExplorer.database.allPhraseAssets
import com.github.linguExplorer.database.allPhrasesList
import com.github.linguExplorer.database.phraseIndex
import com.github.linguExplorer.models.PhraseEntity
import com.github.linguExplorer.repositories.*
import com.github.linguExplorer.userId
import kotlin.random.Random

class EssenMinigame {
    internal lateinit var phraseList: List<PhraseEntity>
    private lateinit var allPhrases: List<PhraseEntity>
    internal val capturedPhrases = mutableMapOf<PhraseEntity, Boolean>()
    private var topicId = TopicRepository().getTopicIdByName("Essen")

    /**
     * Die Funktion greift auf allPhrasesList (aus LoadDatabaseLists) und filtert sie nach der Topic Id des Minigames
     * ich muss das so machen, weil ich sonst zu viele selects hätte und dadurch die Wartezeit zu lang ist :(
     */
    /*
    fun loadAllPhrases() {
        println(allPhrasesList[0])
        allPhrases = allPhrasesList.filter { phrase ->
            phrase.topicId == this.topicId
        }.shuffled().take(16)
    }*/
    fun loadAllPhrases() {
        val filteredPhrases = allPhrasesList.filter { phrase ->
            phrase.topicId == this.topicId
        }

        val mandatoryPhrases = phraseList.filter { it in filteredPhrases }
        val remainingPhrases = filteredPhrases.filter { it !in mandatoryPhrases }.shuffled()

        allPhrases = (mandatoryPhrases + remainingPhrases).take(16).shuffled()
    }

    /**
     * Verwendet die getLimitedPhrasesByTopicForUser method um so die Liste der Minigames,
     * die richtig sein müssen, in einer seperaten Liste zu haben
     *
     * Wird gebraucht, damit man checken kann ob es sich bei einer ausgewählten Phrase um eine richtige handelt
     * (also quasi ist eine Phrase aus allPhrases in phraseList)
     *
     */
    fun loadMinigamePhrases() {
        phraseList = PhraseRepository().getLimitedPhrasesByTopicNameForUser(
            topicId, userId, Random.nextInt(4, 8)
        )
    }

    /**
     * Mapped die Phrase mit ihrem Assetpfad
     */
    fun loadPhrasesWithAssets(): List<Pair<PhraseEntity, String>> {
        //nur bestimmte Bilder laden
        //val allowedAssets = listOf("Minigames/Essen/foods/apple.png", "Minigames/Essen/foods/bacon.png")
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

    /**
     * speichert die Phrasen im Map captured Phrases (relevant um diese Phrasen später einzutragen in der db)
     *
     * @param PhraseEntity: Ist die Phrase
     * @param correct: Das ist der Value, ob sich die Phrase in der phraseList befindet
     */
    fun phraseCheck(phrase: PhraseEntity, correct: Boolean) {
        capturedPhrases[phrase] = correct
    }

    /**
     * ruft die checkCompletion Methode auf
     * IntelliJ meint sie soll private sein
     */
    fun isGameComplete(): Boolean {
        return checkCompletion()
    }

    /**
     * die Methode überprüft ob alle Phrasen der phraseList in capturedPhrases vorkommen
     */
    private fun checkCompletion(): Boolean {
        val allCorrectCaptured = phraseList.all { capturedPhrases[it] == true }
        return allCorrectCaptured
    }


    /**
     * die Methode führt alle benötigten Statements aus, um die Infos in die Datenbank zu packen
     */
    fun storePhraseData() {
        val progressRepo = PhraseProgressRepository()
        val historyRepo = PhraseProgressHistoryRepository()

        val userProgress = progressRepo.getAllPhraseProgressForUser(userId)
        val userHistory = historyRepo.getAllEntriesForUser(userId)
        val phrasesGameHistory = mutableListOf<Pair<Int, Boolean>>()
        val phrasesProgress = mutableListOf<Triple<Int, Int, Boolean>>()
        val phraseStateUpdates = mutableListOf<Int>()

        capturedPhrases.forEach { (phrase, correct) ->
            //sucht nach dem Progress mit der id, den die Phrase hat
            val relevantProgress = userProgress.find { it.phraseId == phrase.id }
            //Berechnung des knowlegdeIndex der Phrase (anhand der Infos der userHistory)
            val correctIndex = historyRepo.calculateCorrectIndex(phrase.id, userHistory)

            if (relevantProgress == null) {
                //Phrasen werden einzeln in die Liste geaddet, um so weniger Statements auszuführen
                phrasesProgress.add(Triple(phrase.id, userId, false))
            }

            //same here
            phrasesGameHistory.add(Pair(phrase.id, correct))

            if (correct) {
                if (correctIndex > phraseIndex && relevantProgress != null && !relevantProgress.isMastered) {
                    phraseStateUpdates.add(phrase.id) //wird ebenfalls in eine Liste gepackt
                    println("Du hast die Phrase gemeistert!")
                }
            }
        }

        //insert Statements aus den Repositories
        progressRepo.addMultiplePhraseProgress(phrasesProgress)
        historyRepo.addPhraseProgressHistories(userId, phrasesGameHistory)
        if (phraseStateUpdates.isNotEmpty()) {
            progressRepo.changeMultipleMasteredStates(userId, phraseStateUpdates)
        }
    }

}
