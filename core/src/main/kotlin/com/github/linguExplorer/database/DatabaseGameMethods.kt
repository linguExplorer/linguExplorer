package com.github.linguExplorer.database

import com.github.linguExplorer.models.*
import com.github.linguExplorer.repositories.*
import org.jetbrains.exposed.sql.transactions.transaction

fun checkIfTopicAvailable(userId: Int, topicId: Int): Boolean {
    return transaction {
        TopicRepository().getTopicById(topicId) ?: return@transaction false

        if (topicId == TopicRepository().getFirstTopic().id) return@transaction true

        val currentProgress = UserProgressRepository().getUserProgress(userId, topicId)
        if (currentProgress != null) return@transaction true

        val previousTopic = TopicRepository().getTopicById(topicId - 1) ?: return@transaction false

        return@transaction false
    }
}
