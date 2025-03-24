package com.github.linguExplorer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.linguExplorer.linguExplorer

class CharacterDialog(private val game: linguExplorer, private val viewport: Viewport
) {
    private var dialogTexture: Texture
    private var dialogImage: Image
    val uiStage = Stage(viewport)
    private var font: BitmapFont = BitmapFont(Gdx.files.internal("fonts/pixelsplitter/pixelsplitter.fnt")) //Font instanziert

    init {
        dialogTexture = Texture(Gdx.files.internal("xx_Images/Dialogfenster/LS_window.png"))
        dialogImage = Image(dialogTexture)

        val imageWidth = 1400f // Breite
        val imageHeight = 400f // Höhe
        dialogImage.setSize(imageWidth, imageHeight)

        val bottomMarginPercentage = 0.1f // 10% vom unteren Rand
        val bottomMargin = uiStage.height * bottomMarginPercentage
        //X-Position, um das Bild horizontal zu zentrieren
        val xPosition = (uiStage.width - imageWidth) / 2

        dialogImage.setPosition(xPosition, bottomMargin)

        uiStage.addActor(dialogImage)
    }

    fun render(delta: Float, batch: SpriteBatch) { // SpriteBatch übergeben!
        uiStage.act(delta)
        uiStage.draw()
    }

    fun dispose() {
        dialogTexture.dispose()
        uiStage.dispose()
        font.dispose() //Font auch disposed
    }
}
