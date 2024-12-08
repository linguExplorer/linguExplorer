package com.github.linguExplorer.screen

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Gdx

class GameScreen : Screen {

    private val font = BitmapFont()
    private val batch = SpriteBatch()  // SpriteBatch erstellen

    override fun show() {
        // Initialisierung des Spiels (falls notwendig)
    }

    override fun render(delta: Float) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT) // Bildschirm löschen

        batch.begin()  // Beginnt das Zeichnen
        font.color = Color.WHITE
        font.draw(batch, "Hallo, LibGDX!", 100f, 200f)  // Text zeichnen
        batch.end()  // Beendet das Zeichnen
    }

    override fun resize(width: Int, height: Int) {}

    override fun hide() {}

    override fun pause() {}

    override fun resume() {}

    override fun dispose() {
        batch.dispose()  // SpriteBatch und andere Ressourcen freigeben
        font.dispose()
    }
}
