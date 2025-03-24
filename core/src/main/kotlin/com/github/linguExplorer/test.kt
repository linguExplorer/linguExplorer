package com.github.linguExplorer

import com.github.linguExplorer.database.DatabaseManager
import com.github.linguExplorer.database.checkIfTopicAvailable
import com.github.linguExplorer.models.PhraseProgress
import com.github.linguExplorer.repositories.*


fun main() {
        DatabaseManager()
        var test = TopicRepository().getTopicById(4)
        println(checkIfTopicAvailable(userId, 2, test!!.id))
    }
