@file:JvmName("Lwjgl3Launcher")

package com.github.linguExplorer.lwjgl3

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.github.linguExplorer.linguExplorer

/** Launches the desktop (LWJGL3) application. */
fun main() {
    // This handles macOS support and helps on Windows.
    if (StartupHelper.startNewJvmIfRequired())
      return
    Lwjgl3Application(linguExplorer(), Lwjgl3ApplicationConfiguration().apply {
        setTitle("linguExplorer")
        //setWindowedMode(640, 480)
        setWindowedMode(1920, 1080)
        setWindowIcon(*(arrayOf(128, 64, 32, 16).map { "libgdx$it.png" }.toTypedArray()))
    })
}
