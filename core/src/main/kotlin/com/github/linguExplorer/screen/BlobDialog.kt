package com.github.linguExplorer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.linguExplorer.linguExplorer

class BlobDialog(private val game: linguExplorer, private val viewport: Viewport) {
    private var dialogTexture: Texture
    private var dialogImage: Image
    val uiStage = Stage(viewport)
    private lateinit var gifAnimation: Animation<TextureRegion>
    private var animationTime = 0f
    private var smallFont: BitmapFont = BitmapFont(Gdx.files.internal("fonts/pixelsplitter/pixelsplitter.fnt"))

    init {
        dialogTexture = Texture(Gdx.files.internal("xx_Images/Dialogfenster/RS_window.png"))
        dialogImage = Image(dialogTexture)

        // Größe und Position im 16x9-Viewport
        val imageWidth = 1400f // Breite
        val imageHeight = 400f
        dialogImage.setSize(imageWidth, imageHeight)
        dialogImage.setPosition((16f - imageWidth) / 2, 100f) // Zentriert, 1 Einheit vom Boden

        uiStage.addActor(dialogImage)
        Gdx.input.inputProcessor = uiStage // ODER InputMultiplexer

        // GIF-Animation
        val textureAtlas = TextureAtlas(Gdx.files.internal("graphics/idle_animation.atlas"))
        gifAnimation = Animation(0.1f, textureAtlas.regions, Animation.PlayMode.LOOP)
    }

    fun render(delta: Float) {

        uiStage.act(delta)
        uiStage.draw()

        // Animation und Text mit dem Batch der uiStage
        uiStage.batch.begin()
        animationTime += delta
        val currentFrame = gifAnimation.getKeyFrame(animationTime, true)
        uiStage.batch.draw(currentFrame, 4f, 2f, 2f, 2f) // Position im Viewport
        smallFont.draw(uiStage.batch, "Hallo, Test!", viewport.worldWidth/2f, 300f)
        smallFont.color = Color.BLACK
        smallFont.data.setScale(0.2f)

        uiStage.batch.end()
    }

    fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        uiStage.viewport.update(width, height, true)
    }

    fun dispose() {
        dialogTexture.dispose()
        uiStage.dispose()
        smallFont.dispose()
    }
}
