package com.github.linguExplorer.test

import com.github.linguExplorer.database.DatabaseManager
import com.github.linguExplorer.models.NPC
import com.github.linguExplorer.repositories.NPCRepository
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class NPCRepositoryTest {

    private lateinit var npcRepository: NPCRepository
    private val id = 1;

    @BeforeEach
    fun setUp() {
        DatabaseManager();
        npcRepository = NPCRepository()
        transaction {
            // Setze Auto-Increment auf 1
            exec("ALTER TABLE npc AUTO_INCREMENT = 1")
        }
    }

    @AfterEach
    fun teardown() {
        transaction {
            NPC.deleteAll();
        }
    }



    @Test
    fun `test create npc`() {
        npcRepository.addNPC("Sophie", "arbeitslos", "../assets/blabla")

        val retrievedNPC = npcRepository.getNPC(id)

        assertNotNull(retrievedNPC)
        assertEquals(id, retrievedNPC?.id)
        assertEquals("Sophie", retrievedNPC?.name)
        assertEquals("arbeitslos", retrievedNPC?.role)
        assertEquals("../assets/blabla", retrievedNPC?.resource)
    }

    @Test
    fun `test remove user`() {
        npcRepository.removeNPC(id)

        val retrievedNPC = npcRepository.getNPC(id)

        assertNull(retrievedNPC)
    }
}
