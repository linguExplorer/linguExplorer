@file:JvmName("TeaVMLauncher")

package com.github.linguExplorer.teavm

import com.github.xpenatan.gdx.backends.teavm.TeaApplicationConfiguration
import com.github.xpenatan.gdx.backends.teavm.TeaApplication
import com.github.linguExplorer.linguExplorer

/** Launches the TeaVM/HTML application. */
fun main() {
    val config = TeaApplicationConfiguration("canvas").apply {
        width = 0
        height = 0
    }
    TeaApplication(linguExplorer(), config)
}
