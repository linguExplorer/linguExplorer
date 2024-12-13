package com.github.linguExplorer

import com.github.linguExplorer.database.DatabaseManager
import com.github.linguExplorer.repositories.PhraseProgressHistoryRepository
import com.github.linguExplorer.repositories.PhraseRepository
import com.github.linguExplorer.repositories.TopicRepository
import com.github.linguExplorer.repositories.UserProgressRepository
import kotlin.random.Random

fun main() {

    DatabaseManager()
    val phraseRepository = PhraseRepository()
    //val user = userRepository.addUser(1000, "Max Mustermann")
    //userRepository.removeUser(1000);

    /*transaction {
        // Setze Auto-Increment auf 1
        exec("ALTER TABLE topic AUTO_INCREMENT = 1")
    }
    topicRepository.addTopic("Essen", "Es geht um Essen", );
    print(topicRepository.getTopic(1))*/

    val topicId = 1


    /*val phrases = phraseRepository.getLimitedPhrasesByTopicName("Kleidung", Random.nextInt(10, 21))

    // Ausgabe der resultierenden Phrasen
    phrases.forEach { println(it) }
    println(phrases.size)*/


    /*val topic = TopicRepository().getTopicById(TopicRepository().getTopicIdByName("Essen"))
    val userProgressRepository = UserProgressRepository()
    userProgressRepository.addProgess(5, topic)

    val userProgress = userProgressRepository.getUserProgress(5, topic)

    userProgressRepository.changeMasteredState(userProgress);
    val repository = PhraseProgressHistoryRepository()

    repeat(10) {
        val randomCorrect = listOf(true, false).random()
        //repository.addPhraseProgress(userId = 1, phraseId = 13, correct = randomCorrect)
    }
    println(repository.calculateCorrectIndex(1, 13))*/

    val repository = PhraseProgressHistoryRepository()
    /*for (phraseId in 295..300) {

        // Generiere zufällige Werte für 'isMastered' (true oder false)
        repeat (Random.nextInt(0, 11)) {
            val isMastered = Random.nextBoolean()
            // Führe das Statement aus, um den Fortschritt hinzuzufügen
            repository.addPhraseProgress(userId = 1, phraseId = phraseId, isMastered)
        }
    }*/

    for (phraseId in 295..300) {
        println("$phraseId: " + repository.calculateCorrectIndex(1, phraseId))
    }

    println("/n")

    val phrases = phraseRepository.getPhrasesByTopicNameForUser("Kleidung", 1)
    phrases.forEach { println("${it.id} " + it.phrase) }

}

