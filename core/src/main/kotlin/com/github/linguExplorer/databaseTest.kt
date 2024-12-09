package com.github.linguExplorer

import com.github.linguExplorer.database.DatabaseManager
import com.github.linguExplorer.repositories.PhraseRepository
import com.github.linguExplorer.repositories.TopicRepository
import org.jetbrains.exposed.sql.transactions.transaction
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

    val topicId = 1  // Beispiel für eine Topic-ID


    val phrases = phraseRepository.getLimitedPhrasesByTopicName("Kleidung", Random.nextInt(10, 21))

    // Ausgabe der resultierenden Phrasen
    phrases.forEach { println(it) }
}

