package com.github.linguExplorer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.linguExplorer.linguExplorer

class BlobDialog(private val game: linguExplorer, private val viewport: Viewport
) {
    private var dialogTexture: Texture
    private var dialogImage: Image
    val uiStage = Stage(viewport)
    private lateinit var gifAnimation: Animation<TextureRegion>
    private var animationTime = 0f
    private var font: BitmapFont = BitmapFont(Gdx.files.internal("fonts/pixelsplitter/pixelsplitter.fnt"))

    init {
        dialogTexture = Texture(Gdx.files.internal("xx_Images/Dialogfenster/RS_window.png"))
        dialogImage = Image(dialogTexture)

        val imageWidth = 1400f // Breite
        val imageHeight = 400f // Höhe
        dialogImage.setSize(imageWidth, imageHeight)

        val bottomMarginPercentage = 0.1f // 10% vom unteren Rand
        val bottomMargin = uiStage.height * bottomMarginPercentage
        val xPosition = (uiStage.width - imageWidth) / 2

        dialogImage.setPosition(xPosition, bottomMargin)
        uiStage.addActor(dialogImage)
        Gdx.input.inputProcessor = uiStage

        // GIF-Animation
        val textureAtlas = TextureAtlas(Gdx.files.internal("graphics/idle_animation.atlas"))
        gifAnimation = Animation(0.1f, textureAtlas.regions, Animation.PlayMode.LOOP) // 0.1f = Frame-Dauer
    }

    fun render(delta: Float, batch: SpriteBatch) { // SpriteBatch übergeben
        uiStage.act(delta)
        uiStage.draw()

        batch.projectionMatrix = uiStage.camera.combined

        batch.begin()

        animationTime += delta
        val currentFrame = gifAnimation.getKeyFrame(animationTime, true)
        batch.draw(currentFrame, 1320f, 200f, 300f, 300f)

        batch.end()
    }

    fun dispose() {
        dialogTexture.dispose()
        uiStage.dispose()
    }
}
