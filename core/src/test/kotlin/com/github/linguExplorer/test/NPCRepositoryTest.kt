package com.github.linguExplorer.test

import com.github.linguExplorer.database.DatabaseManager
import com.github.linguExplorer.repositories.NPCRepository
import com.github.linguExplorer.repositories.UserRepository
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
    }


    @Test
    fun `test create npc`() {
        val npc = npcRepository.addNPC(id, "Sophie", "arbeitslos", "../assets/blabla")

        val retrievedNPC = npcRepository.getNPC(id)

        assertNotNull(retrievedNPC)
        assertEquals(1, retrievedNPC?.id)
        assertEquals("Sophie", retrievedNPC?.name)
        assertEquals("arbeitslos", retrievedNPC?.role)
        assertEquals("../assets/blabla", retrievedNPC?.resource)
    }

    @Test
    fun `test remove user`() {
        val user = npcRepository.removeNPC(id)

        val retrievedNPC = npcRepository.getNPC(id)

        assertNull(retrievedNPC)
    }
}
