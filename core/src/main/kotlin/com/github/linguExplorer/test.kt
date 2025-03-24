package com.github.linguExplorer

import com.github.linguExplorer.database.DatabaseManager
import com.github.linguExplorer.database.checkIfTopicAvailable
import com.github.linguExplorer.models.PhraseProgress
import com.github.linguExplorer.repositories.*


fun main() {
        DatabaseManager()
        val progressRepo = PhraseProgressRepository()
    val phraseList = allPhrasesList.filter { it.topicId == 2 }
    println(phraseList.map { it.id })
    val userProgress = progressRepo.getAllPhraseProgressForUser(userId, 2)
        .filter { it.phraseId in phraseList.map { it.id }
        }

    println(userProgress.size == phraseList.size)
    println(userProgress.size)
    println(phraseList.size)
    userProgress.forEach { println(it.isMastered) }
    println(userProgress.all { it.isMastered })
    }
