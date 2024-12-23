package com.github.linguExplorer.test

import com.github.linguExplorer.database.DatabaseManager
import com.github.linguExplorer.repositories.TopicRepository
import com.github.linguExplorer.repositories.UserProgressRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

class UserProgessRepositoryTest {

    private lateinit var userProgressRepository: UserProgressRepository
    private val id = 5;

    @BeforeEach
    fun setUp() {
        DatabaseManager();
        userProgressRepository = UserProgressRepository()  // Initialisiere das Repository
    }


    @Test
    fun `test create progress`() {
        val topic = TopicRepository().getTopicById(TopicRepository().getTopicIdByName("Essen"))
        userProgressRepository.addProgess(1, topic)

        /*val retrievedProgress = userProgressRepository.getUserProgress(1, topic.getId())
        println(retrievedProgress?.isMastered)

        assertNotNull(retrievedProgress)
        assertEquals(1, retrievedProgress?.userId)
        assertEquals(topic!!.id, retrievedProgress?.topicId)
        assertEquals(false, retrievedProgress?.isMastered)*/
    }
}
