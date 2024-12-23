package com.github.linguExplorer.test

import com.github.linguExplorer.database.DatabaseManager
import com.github.linguExplorer.models.Phrase
import com.github.linguExplorer.repositories.PhraseRepository
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class PhraseRepositoryTest {

    private lateinit var phraseRepository: PhraseRepository
    private val id = 1;

    @BeforeEach
    fun setUp() {
        DatabaseManager();
        phraseRepository = PhraseRepository()
        transaction {
            // Setze Auto-Increment auf 1
            Phrase.deleteAll()
            exec("ALTER TABLE phrase AUTO_INCREMENT = 1")
        }
    }

    @AfterEach
    fun teardown() {
        transaction {
            Phrase.deleteAll();
        }
        transaction {
            // Setze Auto-Increment auf 1
            exec("ALTER TABLE phrase AUTO_INCREMENT = 1")
        }
    }

    @Test
    fun `test create phrase`() {
        phraseRepository.addPhrase("tuba", "tuba", "kleines Instrument", 1)

        val retrievedPhrase = phraseRepository.getPhrase(id)

        assertNotNull(retrievedPhrase)
        assertEquals(1, retrievedPhrase?.id)
        assertEquals("tuba", retrievedPhrase?.phrase)
        assertEquals("tuba", retrievedPhrase?.translation)
        assertEquals("kleines Instrument", retrievedPhrase?.definition)
        assertEquals(1, retrievedPhrase?.topicId)
    }

    @Test
    fun `test remove user`() {
        phraseRepository.addPhrase("tuba", "tuba", "kleines Instrument", 1)
        var retrievedPhrase = phraseRepository.getPhrase(id)
        assertNotNull(retrievedPhrase)

        phraseRepository.removePhrase(id)
        retrievedPhrase = phraseRepository.getPhrase(id)
        assertNull(retrievedPhrase)
    }

    @Test
    fun `test next phrase null`() {
        phraseRepository.addPhrase("tuba", "tuba", null, 1);
        val retrievedPhrase = phraseRepository.getPhrase(1)

        assertNotNull(retrievedPhrase?.phrase)
        assertNull(retrievedPhrase?.definition)
    }

    @Test
    fun `get phrases for topic by id`() {
        phraseRepository.addPhrase("apple", "apfel", null, 1);
        phraseRepository.addPhrase("banana", "banana", null, 1);
        phraseRepository.addPhrase("testt", "test", null, 1);
        phraseRepository.addPhrase("test", "testtest", null, 1);
        phraseRepository.addPhrase("no", "ja", null, 2);
        phraseRepository.addPhrase("what", "was", null, 1);

        val retrievedPhrases = phraseRepository.getPhrasesByTopicId(1);

        assertEquals(5, retrievedPhrases.size)

        val expectedPhrases = listOf("apple", "banana", "testt", "test", "what")
        val actualPhrases = retrievedPhrases.map { it.phrase }

        assertEquals(expectedPhrases, actualPhrases)
    }

    @Test
    fun `get phrases for topic by name`() {
        phraseRepository.addPhrase("apple", "apfel", null, 1);
        phraseRepository.addPhrase("banana", "banana", null, 1);
        phraseRepository.addPhrase("testt", "test", null, 1);
        phraseRepository.addPhrase("test", "testtest", null, 1);
        phraseRepository.addPhrase("no", "ja", null, 2);
        phraseRepository.addPhrase("what", "was", null, 1);

        val retrievedPhrases = phraseRepository.getPhrasesByTopicName("Kleidung");

        assertEquals(5, retrievedPhrases.size)

        val expectedPhrases = listOf("apple", "banana", "testt", "test", "what")
        val actualPhrases = retrievedPhrases.map { it.phrase }

        assertEquals(expectedPhrases, actualPhrases)
    }
}
