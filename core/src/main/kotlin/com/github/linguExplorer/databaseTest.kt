/*import com.github.linguExplorer.database.*
import com.github.linguExplorer.repositories.*
import java.util.Scanner
import kotlin.random.Random

fun startGame() {
    println("Das ist die Konsolenversion von linguExplorer")
    println("Gib 'start' ein, um zu beginnen!")

    val scanner = Scanner(System.`in`)
    val input = scanner.nextLine()

    if (input.lowercase() == "start") {
        DatabaseManager()
        UserRepository().deleteUserWithDependencies(123)
        createUser(scanner)
    } else {
        println("Fehler! Spiel zu Ende.")
    }
}

fun createUser(scanner: Scanner) {
    println("\nWie willst du heißen? Keine Angabe = Blob!")
    val username = scanner.nextLine()
    if (username.equals("")) {
        UserRepository().addUser(123, "Blob")
        println("Willkommen, Blob.");
    } else {
        UserRepository().addUser(123, username)
        println("Willkommen, $username");
    }

    showTopics(scanner)
}

fun showTopics(scanner: Scanner) {
    println("\nWähle dein Thema aus (Zahl)")
    val topicList = TopicRepository().getTopics()
    topicList.forEach { println("${it.id}: ${it.name}") }
    val topicId = Integer.parseInt(scanner.nextLine())
    if (checkIfTopicAvailable(123, topicId)) {
        println("Topic ${TopicRepository().getTopicById(topicId)!!.name} ausgewählt!")
        chooseNextOption(scanner, topicId)
    } else {
        println("Das darfst du nicht spielen")
        showTopics(scanner)
    }
}

fun chooseNextOption(scanner: Scanner, topicId: Int) {
    println("Wähle deine nächste Aktion aus\nStatistiken sehen (1)\nSpiel spielen (2)\nZurück (3)")
    val number = Integer.parseInt(scanner.nextLine())
    if (number == 1) {
        println("Dein aktueller Stand bei den Phrasen")
        PhraseProgressRepository().getAllPhraseProgressForUser(123).forEach {
            println("${PhraseRepository().getPhrase(it.phraseId)!!.phrase}\t${if(it.isMastered) "gemeistert" else "nicht gemeistert"}")
        }
        println("-------\nDeine Indexe")
        println("Id\tPhrase\tIndex")
        PhraseRepository().getPhrasesByTopicNameForUser(1, 123).forEach {
            println("${it.id}\t${it.phrase}\t${PhraseProgressHistoryRepository().calculateCorrectIndex(123, it.id)}")
        }

        chooseNextOption(scanner, topicId)
    } else if (number == 2) {
        game(scanner, topicId)
    } else if (number == 3) {
        showTopics(scanner)
    } else {
        UserRepository().deleteUserWithDependencies(123)
        println("Fehler! Spiel zu Ende.")
    }
}


fun game(scanner: Scanner, topicId: Int) {
    PhraseRepository().getLimitedPhrasesByTopicNameForUser(TopicRepository().
    getTopicById(topicId)!!.name, 123, Random.nextInt(10,16)).forEach {
        println("\nÜbersetze die folgende Phrase: '${it.phrase}'")
        print("Deine Übersetzung: ")
        val userTranslation = scanner.nextLine()

        if (userTranslation.equals(it.translation, ignoreCase = true)) {
            println("Richtig!")
            if (PhraseProgressRepository().getPhraseProgress(it.id, 123) == null) {
                PhraseProgressRepository().addPhraseProgress(it.id, 123)
            }
            PhraseProgressHistoryRepository().addPhraseProgress(123, it.id, true)
            if (PhraseProgressHistoryRepository().calculateCorrectIndex(123, it.id) > 0.75 &&
                !PhraseProgressRepository().getPhraseProgress(123, it.id)!!.isMastered) {
                PhraseProgressRepository().changeMasteredState(123, it.id)
                println("Du hast die Phrase gemeistert!")
            }
        } else {
            println("Falsch!")
            if (PhraseProgressRepository().getPhraseProgress(it.id, 123) == null) {
                PhraseProgressRepository().addPhraseProgress(it.id, 123)
            }
            PhraseProgressHistoryRepository().addPhraseProgress(123, it.id, false)
        }
    }
    println("Spiel zu Ende!")
    if (UserProgressRepository().checkIfMastered(TopicRepository().getTopicById(topicId)!!.name, 123)) {
        println("Geschafft!")
    }
    chooseNextOption(scanner, topicId)
}


fun main() {
    startGame()
}
*/
