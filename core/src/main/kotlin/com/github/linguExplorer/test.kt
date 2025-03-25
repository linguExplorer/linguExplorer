package com.github.linguExplorer

import com.github.linguExplorer.database.DatabaseManager
import com.github.linguExplorer.database.checkIfTopicAvailable
import com.github.linguExplorer.models.PhraseProgress
import com.github.linguExplorer.repositories.*


fun main() {
        DatabaseManager()
        println(checkIfTopicAvailable(1, 1, 4))
    }
