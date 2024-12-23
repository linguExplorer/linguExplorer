package com.github.linguExplorer.test

import com.github.linguExplorer.database.DatabaseManager
import com.github.linguExplorer.database.InsertFunctions
import com.github.linguExplorer.models.Topic
import com.github.linguExplorer.repositories.TopicRepository
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class TopicRepositoryTest {

    private lateinit var topicRepository: TopicRepository
    private lateinit var insertFunction: InsertFunctions
    private val id = 1;

    @BeforeEach
    fun setUp() {
        DatabaseManager();
        topicRepository = TopicRepository()
        transaction {
            transaction {
                Topic.deleteAll();
            }
            exec("ALTER TABLE topic AUTO_INCREMENT = 1")
        }
    }

    @AfterEach
    fun teardown() {
        println("ENDE!")
        insertFunction = InsertFunctions()
        transaction {
            Topic.deleteAll();
            exec("ALTER TABLE topic AUTO_INCREMENT = 1")
            insertFunction.insertSampleTopics()
        }
    }

    @Test
    fun `test create topic`() {
        topicRepository.addTopic("Essen", "Es geht um Essen", 2);

        val retrievedTopic = topicRepository.getTopicById(id)

        assertNotNull(retrievedTopic)
        assertEquals(1, retrievedTopic?.id)
        assertEquals("Essen", retrievedTopic?.name)
        assertEquals("Es geht um Essen", retrievedTopic?.description)
        assertEquals(2, retrievedTopic?.next)
    }

    @Test
    fun `test remove user`() {
        topicRepository.removeTopic(id)

        val retrievedTopic = topicRepository.getTopicById(id)

        assertNull(retrievedTopic)
    }

    @Test
    fun `test next topic null`() {
        topicRepository.addTopic("Kleidung", "Kleidung")
        val retrievedTopic = topicRepository.getTopicById(1)

        assertNotNull(retrievedTopic?.name)
        assertNull(retrievedTopic?.next)
    }

    @Test
    fun `test get topic by name`() {
        topicRepository.addTopic("Kleidung", "Kleidung")
        val retrievedTopic = topicRepository.getTopicIdByName("Kleidung")

        assertNotNull(retrievedTopic)
        assertEquals(1, retrievedTopic)
    }
}
