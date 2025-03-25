package com.github.linguExplorer.database

import com.github.linguExplorer.models.*
import com.github.linguExplorer.repositories.*
import com.github.linguExplorer.saveNumber
import org.jetbrains.exposed.sql.transactions.transaction

fun checkIfTopicAvailable(userId: Int, saveNumber: Int, topicId: Int): Boolean {
    return transaction {
        TopicRepository().getTopicById(topicId) ?: return@transaction false

        if (topicId == TopicRepository().getFirstTopic().id) return@transaction true

        var currentProgress = UserProgressRepository().getUserProgress(userId, saveNumber, topicId)
        if (currentProgress != null) return@transaction true

        currentProgress = UserProgressRepository().getUserProgress(userId, saveNumber, topicId - 1)
        if (currentProgress != null && currentProgress.isMastered) return@transaction true


        return@transaction false
    }
}
