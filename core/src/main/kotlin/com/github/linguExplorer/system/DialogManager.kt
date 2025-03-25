package com.github.linguExplorer.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue

class DialogManager {
    private val loadedDialogs = mutableMapOf<String, JsonValue>()

    fun loadAllDialogs() {
        val file = Gdx.files.internal("dialogs/dialogs.json")
        val root = JsonReader().parse(file)

        root.get("dialogues").forEach { dialog ->
            loadedDialogs[dialog.getString("entityId")] = dialog
        }
    }

    fun getDialogFor(entityId: String): JsonValue? {
        return loadedDialogs[entityId] ?: run {
            Gdx.app.error("Dialog", "No dialog for $entityId")
            null
        }
    }
}
