package com.github.linguExplorer

import com.github.linguExplorer.database.DatabaseManager
import com.github.linguExplorer.repositories.UserRepository

fun main() {

    DatabaseManager()
    val userRepository = UserRepository()
    //val user = userRepository.addUser(1000, "Max Mustermann")
    //userRepository.removeUser(1000);

    val user = userRepository.getUser(1)
    print(user?.name)
}

