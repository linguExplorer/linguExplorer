package com.github.linguExplorer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.linguExplorer.linguExplorer
import com.github.quillraven.fleks.Entity

class BlobDialog(
    private val game: linguExplorer,
    private val viewport: Viewport
) {
    private var dialogTexture: Texture
    private var dialogImage: Image
    private var uiStage = Stage(viewport)
    private lateinit var gifAnimation: Animation<TextureRegion>
    private var animationTime = 0f
    private var smallFont: BitmapFont = BitmapFont(Gdx.files.internal("fonts/pixelsplitter/pixelsplitter.fnt"))
    private val skin = Skin(Gdx.files.internal("xx_Images/Dialogfenster/DialogButton/button.json"))
    private val buttons = arrayOf(
        Button(skin),
        Button(skin),
        Button(skin),
        Button(skin)
    )

    private val imageWidth = 1400f
    private val imageHeight = 400f
    private var dialogX = 0f
    private var dialogY = 0f
    private val buttonWidth = 200f
    private val buttonHeight = 70f
    private val padding = 20f
    private var currentCharacter: String = ""
    private var name: String = ""
    private var isVisible = false






    init {

        val bdialogTexture = Texture(Gdx.files.internal("xx_Images/Dialogfenster/RS_window.png"))
        val revdialogTexture = Texture(Gdx.files.internal("xx_Images/Dialogfenster/LS_window.png"))



        dialogTexture = bdialogTexture


        dialogImage = Image(dialogTexture).apply {
            isVisible = true
        }
        uiStage.addActor(dialogImage)

        val textureAtlas = TextureAtlas(Gdx.files.internal("graphics/idle_animation.atlas"))
        gifAnimation = Animation(0.1f, textureAtlas.regions, Animation.PlayMode.LOOP)

        updatePositions() // Initiale Positionierung
        setupButtons()
    }

    private fun updatePositions() {
        dialogX = (viewport.worldWidth / 2f - imageWidth / 2)
        dialogY = 100f
        dialogImage.setSize(imageWidth, imageHeight)
        dialogImage.setPosition(dialogX, dialogY)
    }

    private fun setupButtons() {
        val leftColumnX = dialogX + imageWidth * 0.55f
        val leftColumnStartY = dialogY + imageHeight * 0.4f
        val rightColumnX = leftColumnX - buttonWidth - padding

        buttons.forEachIndexed { index, button ->
            val (columnX, columnY) = if (index < 2) {
                Pair(leftColumnX, leftColumnStartY - (buttonHeight + padding) * index)
            } else {
                val adjustedIndex = index - 2
                Pair(rightColumnX, leftColumnStartY - (buttonHeight + padding) * adjustedIndex)
            }

            with(button) {
                setPosition(columnX, columnY)
                setSize(buttonWidth, buttonHeight)
                addListener(object : ClickListener() {
                    override fun clicked(event: InputEvent?, x: Float, y: Float) {
                        if (index == 0) hide()
                        println("Button ${index + 1} action for $currentCharacter")
                    }
                })
            }
            uiStage.addActor(button)
        }
    }

    fun show(entity: Entity, name: String) {
        currentCharacter = "Entity-${entity.id}"
        this.name = name
        isVisible = true
        uiStage.root.isVisible = true
        updatePositions() // Positionierung bei jedem Aufruf aktualisieren
    }

    fun hide() {
        isVisible = false
        uiStage.root.isVisible = false
    }

    fun render(delta: Float) {
        if (!isVisible) return
        uiStage.act(delta)
        uiStage.draw()

        uiStage.batch.begin()
        animationTime += delta
        val currentFrame = gifAnimation.getKeyFrame(animationTime, true)
        uiStage.batch.draw(
            currentFrame,
            dialogX + imageWidth * 0.74f,
            dialogY + 100f,
            300f,
            300f
        )

        smallFont.draw(uiStage.batch, "Hallo, $name", (viewport.worldWidth / 2f) - 500f, 300f)
        smallFont.color = Color.BLACK
        smallFont.data.setScale(0.2f)
        uiStage.batch.end()

        // 2. UI-Stage rendern (Buttons)

    }

    fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
        uiStage.viewport.update(width, height, true)
        updatePositions() // Positionierung bei Resize aktualisieren
        setupButtons() // Buttons neu positionieren
    }

    fun getStage(): Stage = uiStage

    fun dispose() {
        dialogTexture.dispose()
        uiStage.dispose()
        smallFont.dispose()
    }
}
