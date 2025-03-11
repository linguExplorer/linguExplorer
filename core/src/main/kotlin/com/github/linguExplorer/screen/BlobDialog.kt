package com.github.linguExplorer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.linguExplorer.linguExplorer
import ktx.app.KtxScreen

class BlobDialog(private val game: linguExplorer) : KtxScreen {

    private var batch: SpriteBatch = SpriteBatch()
    private lateinit var dialogTexture: Texture
    private lateinit var dialogImage: Image
    private val viewport: Viewport = ExtendViewport(1920f, 1080f)
    private val uiStage = Stage(viewport)

    // GIF-Animation
    private lateinit var gifAnimation: Animation<TextureRegion>
    private var animationTime = 0f

    override fun show() {
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

        // Texturen für die GIF-Animation aus dem TextureAtlas
        val textureAtlas = TextureAtlas(Gdx.files.internal("graphics/idle_animation.atlas"))
        gifAnimation = Animation(0.1f, textureAtlas.regions, Animation.PlayMode.LOOP) // 0.1f ist die Frame-Dauer

    }

    override fun render(delta: Float) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        uiStage.act(delta)

        // zuerst die UI-Stage zeichnen
        uiStage.draw()

        batch.projectionMatrix = uiStage.camera.combined //Hier mit stage kombinieren

        batch.begin()

        animationTime += delta
        val currentFrame = gifAnimation.getKeyFrame(animationTime, true) // true für Loop-Modus
        batch.draw(currentFrame, 1320f, 200f, 300f, 300f) // Position + Größe von Blob

        batch.end()
    }

    override fun resize(width: Int, height: Int) {
        uiStage.viewport.update(width, height, true)
    }

    override fun dispose() {
        batch.dispose()
        dialogTexture.dispose()
        uiStage.dispose()
    }
}
