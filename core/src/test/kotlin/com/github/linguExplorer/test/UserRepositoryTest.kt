package com.github.linguExplorer.test

import com.github.linguExplorer.database.DatabaseManager
import com.github.linguExplorer.repositories.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class UserRepositoryTest {

    private lateinit var userRepository: UserRepository
    private val id = 5;

    @BeforeEach
    fun setUp() {
        DatabaseManager();
        userRepository = UserRepository()  // Initialisiere das Repository
    }


    @Test
    fun `test create user`() {
        val user = userRepository.addUser(id, "Max Mustermann")

        val retrievedUser = userRepository.getUser(id)

        assertNotNull(retrievedUser)
        assertEquals(5, retrievedUser?.id)
        assertEquals("Max Mustermann", retrievedUser?.name)
    }

    @Test
    fun `test remove user`() {
        val user = userRepository.removeUser(id)

        val retrievedUser = userRepository.getUser(id)

        assertNull(retrievedUser)
    }
}
