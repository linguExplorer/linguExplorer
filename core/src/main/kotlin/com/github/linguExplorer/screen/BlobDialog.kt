package com.github.linguExplorer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.JsonReader
import com.badlogic.gdx.utils.JsonValue
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.linguExplorer.linguExplorer
import com.github.linguExplorer.system.DialogManager
import com.github.quillraven.fleks.Entity
import com.sun.jna.StringArray

class BlobDialog(
    private val game: linguExplorer,
    private val viewport: Viewport,

) {
    private val dialogManager: DialogManager = DialogManager()
    private var dialogTexture: Texture
    private var dialogImage: Image
    private var uiStage = Stage(viewport)
    private lateinit var gifAnimation: Animation<TextureRegion>
    private var animationTime = 0f
    private var smallFont: BitmapFont = BitmapFont(Gdx.files.internal("fonts/pixelsplitter/pixelsplitter.fnt"))
    private val skin = Skin(Gdx.files.internal("xx_Images/Dialogfenster/DialogButton/button.json"))
    private val buttons = Array(4) { Button(skin) }


    private val buttonTexts = Array(4) { "" }
    private val buttonPositions = Array(4) { Pair(0f, 0f) }

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

    private var autoCloseTimer = 0f

    private var  currentDialog : String = ""
    private var currentOptions: JsonValue = JsonReader().parse("""{"options":[]}""")
    private var currentNext : String = ""
    private var currentNode = JsonReader().parse("""{"options":[]}""")



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
//Node Testd
        dialogManager.loadAllDialogs()
        val dialog = dialogManager.getDialogFor(name) ?: return
        currentNode = dialog.get("nodes").first {
            it.getString("id") == dialog.getString("startNode")
        }

        val optionsArray: JsonValue = currentNode.get("options") // Bleibt JsonValue

        currentDialog = currentNode.getString("text")

        currentOptions = currentNode.get("options")

        for (i in 0 until optionsArray.size) {
            val option = optionsArray.get(i)
            val text = option.getString("text")
            currentNext = option.getString("next")

        }

        println(currentNode)
        println(currentDialog)
        println(currentOptions)
        println(currentNext)


        println(currentNode)

//Node Test End
        currentCharacter = "Entity-${entity.id}"
        this.name = name
        isVisible = true
        uiStage.root.isVisible = true
        updatePositions() // Positionierung bei jedem Aufruf aktualisieren
        updateButtons(this.name)
        updateDialogState(this.name)

    }

    fun hide() {
        isVisible = false
        uiStage.root.isVisible = false
        autoCloseTimer = 0f
    }

    fun render(delta: Float) {
        if (!isVisible) return

        if (autoCloseTimer > 0) {
            autoCloseTimer -= delta
            if (autoCloseTimer <= 0) {
                hide()

            }
        }

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
        val fontLayout = GlyphLayout() // Neu: Für Textdimensionen

        smallFont.draw(uiStage.batch, currentDialog, (viewport.worldWidth / 2f) - 700f, 300f)
        smallFont.color = Color.BLACK
        smallFont.data.setScale(0.2f)
        buttons.forEachIndexed { index, button ->
            if (button.isVisible && index < buttonTexts.size) {
                val text = buttonTexts[index]
                if (text.isNotEmpty()) {
                    // Button-Position aus der Stage holen (statt aus buttonPositions)
                    val x = button.x
                    val y = button.y

                    // Textdimensionen berechnen
                    fontLayout.setText(smallFont, text)
                    val textWidth = fontLayout.width
                    val textHeight = fontLayout.height

                    // Text zentriert über dem Button rendern
                    smallFont.draw(
                        uiStage.batch,
                        text,
                        x + (button.width - textWidth) / 2,  // Horizontal zentrieren
                        y + button.height - 10  // Etwas über dem Button
                    )
                }
            }
        }




        uiStage.batch.end()


        // 2. UI-Stage rendern (Buttons)

    }
    private fun updateDialogState(name: String) {
        // Text aktualisieren
        currentDialog = currentNode?.getString("text", "") ?: ""

        // Options aktualisieren
        currentOptions = currentNode?.get("options") ?: JsonValue(JsonValue.ValueType.array)


        updateButtons(name)

        if (currentNode?.getBoolean("isEnd", false) == true) {
            autoCloseTimer = 3f
        } else {
            autoCloseTimer = 0f
        }


    }

    private fun updateButtons(name: String) {
        // Zurücksetzen
        buttonTexts.fill("")
        buttons.forEach {
            it.isVisible = false
            it.clearListeners()
        }

        // Optionen verarbeiten
        for (i in 0 until currentOptions.size) {
            if (i >= buttons.size) break

            val option = currentOptions.get(i)
            buttonTexts[i] = option.getString("text", "")
            buttons[i].isVisible = true

            buttons[i].addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    if (option.has("next")) {
                        val dialog = dialogManager.getDialogFor(name) ?: return
                        currentNode = dialog.get("nodes").firstOrNull {
                            it.getString("id") == option.getString("next")
                        }
                        updateDialogState(name)
                    } else {
                        hide()
                    }
                }
            })
        }
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
