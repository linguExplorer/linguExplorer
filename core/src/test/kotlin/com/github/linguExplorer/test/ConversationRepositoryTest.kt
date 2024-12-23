package com.github.linguExplorer.test

import com.github.linguExplorer.database.DatabaseManager
import com.github.linguExplorer.models.Conversation
import com.github.linguExplorer.repositories.ConversationRepository
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class ConversationRepositoryTest {

    private lateinit var conversationRepository: ConversationRepository
    private val id = 1

    @BeforeEach
    fun setUp() {
        DatabaseManager()
        conversationRepository = ConversationRepository()
        transaction {
            // Setze Auto-Increment auf 1
            exec("ALTER TABLE conversation AUTO_INCREMENT = 1")
        }
    }

    @AfterEach
    fun teardown() {
        transaction {
            Conversation.deleteAll()
        }
    }

    @Test
    fun `test create conversation`() {
        conversationRepository.addConversation("Introduction", 1)

        val retrievedConversation = conversationRepository.getConversation(id)

        assertNotNull(retrievedConversation)
        assertEquals(id, retrievedConversation?.id)
        assertEquals("Introduction", retrievedConversation?.name)
        assertEquals(1, retrievedConversation?.npcId)
    }

    @Test
    fun `test remove conversation`() {
        conversationRepository.addConversation("Introduction", 1)

        val deleteResult = conversationRepository.removeConversation(id)
        val retrievedConversation = conversationRepository.getConversation(id)

        assertTrue(deleteResult)
        assertNull(retrievedConversation)
    }

    @Test
    fun `test get conversation by id`() {
        conversationRepository.addConversation("Introduction", 1)

        val retrievedConversation = conversationRepository.getConversationById(id)

        assertNotNull(retrievedConversation)
        assertEquals(id, retrievedConversation?.id)
        assertEquals("Introduction", retrievedConversation?.name)
        assertEquals(1, retrievedConversation?.npcId)
    }

    @Test
    fun `test get conversation by name`() {
        conversationRepository.addConversation("Introduction", 1)

        val retrievedConversation = conversationRepository.getConversationByName("Introduction")

        assertNotNull(retrievedConversation)
        assertEquals(id, retrievedConversation?.id)
        assertEquals("Introduction", retrievedConversation?.name)
        assertEquals(1, retrievedConversation?.npcId)
    }

}
