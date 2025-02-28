@file:JvmName("Lwjgl3Launcher")

package com.github.linguExplorer.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.github.linguExplorer.linguExplorer
/*import net.arikia.dev.drpc.DiscordEventHandlers
import net.arikia.dev.drpc.DiscordRPC
import net.arikia.dev.drpc.DiscordRichPresence*/

fun main() {
    // Initialize the application
    if (StartupHelper.startNewJvmIfRequired()) return

    // Initialize Discord RPC
    //initializeDiscordRPC()

    // Start the LibGDX application
    Lwjgl3Application(linguExplorer(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("linguExplorer")
        setWindowedMode(1920, 1080)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}

/*fun initializeDiscordRPC() {
    val clientId = "1344733731780431943" // Set your Discord Application Client ID

    // Set up the Discord event handlers
    val handlers = DiscordEventHandlers.Builder()
        .setReadyEventHandler { user ->
            println("Welcome ${user.username}#${user.discriminator}!")
        }
        .build()

    // Initialize Discord RPC
    DiscordRPC.discordInitialize(clientId, handlers, true)

    // Start updating the presence and running the callbacks in a loop
    startPresenceLoop()
}

fun startPresenceLoop() {
    // This should run continuously in the background to handle Discord callbacks and update presence
    Thread {
        while (true) {
            DiscordRPC.discordRunCallbacks()
            Thread.sleep(1000) // Update every 1 second
        }
    }.start()
}

fun updatePresence() {
    // Create the Discord Rich Presence object
    val presence = DiscordRichPresence.Builder("Currently playing linguExplorer!")
        .setDetails("Exploring new languages.")
        .build()

    // Update the Discord Presence
    DiscordRPC.discordUpdatePresence(presence)
}

fun shutdownDiscordRPC() {
    // Cleanly shut down the Discord RPC when the app closes
    DiscordRPC.discordShutdown()*
}*/
