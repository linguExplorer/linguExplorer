package com.github.linguExplorer.database

import com.github.linguExplorer.repositories.NPCRepository
import com.github.linguExplorer.repositories.TopicRepository
import org.jetbrains.exposed.sql.transactions.transaction

class InsertFunctions {
    fun insertSampleTopics() = transaction {
        val topicRepository = TopicRepository()
        topicRepository.addTopic("Kleidung", "Vokabeln über Kleidungsteile", 2)
        topicRepository.addTopic("Essen", "Vokabeln über Gerichte und Zutaten", 3)
        topicRepository.addTopic("Familie", "Vokabeln für familiäre Beziehungen", 4)
        topicRepository.addTopic("Schule", "Schul- und Fächerbezogene Vokabeln")
    }

    fun insertSampleNPCs() = transaction {
        val npcRepository = NPCRepository()
        npcRepository.addNPC("Jürgen", "Bauer", "bling bling")
    }
}
