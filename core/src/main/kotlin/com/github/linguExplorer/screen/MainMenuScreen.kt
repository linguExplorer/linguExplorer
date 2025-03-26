package com.github.linguExplorer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.TextField
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.linguExplorer.*
import com.github.linguExplorer.models.CheckpointEntity
import com.github.linguExplorer.models.PhraseProgressHistoryEntity
import com.github.linguExplorer.models.User
import com.github.linguExplorer.models.UserEntity
import com.github.linguExplorer.repositories.*
import ktx.actors.stage
import java.awt.Desktop
import java.net.URI
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.log.logger
import java.sql.Timestamp
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.max

class MainMenuScreen(private val game: linguExplorer) : KtxScreen {
    private var batch: SpriteBatch = SpriteBatch()
    private var stage: Stage = Stage(ExtendViewport(1920f, 1080f))
    private var shapeRenderer: ShapeRenderer = ShapeRenderer()
    private lateinit var font: BitmapFont
    private val viewport: Viewport = ExtendViewport(1920f, 1080f)
    private val glyphLayout = GlyphLayout()
    private var selectSound: Sound = Gdx.audio.newSound(Gdx.files.internal("Sounds/Soundeffekte/game_select.mp3"))

    private val skin = Skin(Gdx.files.internal("MainMenu/FieldSkin.json"))
    private var backgroundTexture: Texture = Texture("xx_map_assets/Map/ref.png")
    private var startNewGameTexture: Texture = Texture("xx_Images/Buttons/neuesSpiel_green.png")
    private var loadGameTexture: Texture = Texture("xx_Images/Buttons/spielstandLaden_green.png")
    private var settingsIconTexture: Texture = Texture("xx_Images/SettingsIcon.png")
    private var wordmarkTexture: Texture = Texture("xx_Images/wordmark/wordmark_scaled.png")
    private var popupTexture: Texture = Texture("MainMenu/box2.png")
    private var exitTexture: Texture = Texture("xx_Images/Buttons/red_X.png")
    private var usedSlotTexture: Texture = Texture("MainMenu/greenbox.png")
    private var emptySlotTexture: Texture = Texture("MainMenu/greybox.png")
    private var penTexture: Texture = Texture("MainMenu/penGrey.png")
    private var showUserExistsConfirmation = false
    private var showUserEditConfirmation = false
    private var userExistsBoxTexture: Texture = Texture("MainMenu/box3.png")
    private var checkTexture: Texture = Texture("MainMenu/check.png")
    private var showEnterNameDialog = false
    private var spielstandStartenButtonTexture: Texture = Texture("MainMenu/spielstandStarten.png")
    private var rectangleTexture: Texture = Texture("MainMenu/rectangle.png")
    private val executor: ExecutorService = Executors.newFixedThreadPool(1)
    private var showPopup = false
    private var showMenu = false
    private var newGamePopUp = false
    private var loadGamePopUp = false
    private var threadExecuted = false
    private var loadingTime = 0f
    private var userFound = false
    private var userAlreadyExists = false
    private var loadGame = false
    private var newGame = false
    private var soundPlayed = false
    private var introduction = false
    private lateinit var user: UserEntity
    private lateinit var currentCheckpoint: CheckpointEntity
    private var currentEditSlotIndex = -1
    private var clickProcessedThisFrame = false // Added variable to track clicks within a frame


    val saveSlots = mutableListOf<Triple<UserEntity?, CheckpointEntity?, String?>>()
    private lateinit var topicString: String
    private var loadingScreenRenderer = LoadingScreenRenderer()
    private val gameMenuRenderer = GameMenuRenderer()
    private var executePositionX = 0f
    private var executePositionY = 0f

    private val popUpSize = Vector2(1100f, 700f)
    private val exitSize = 90f
    private val editSize = 35f
    private val popUpPosition: Vector2
        get() = Vector2(
            (viewport.worldWidth / 2 - popUpSize.x / 2),
            (viewport.worldHeight / 2 - popUpSize.y / 2)
        )


    val boxWidth = 700f
    val boxHeight = 250f
    private val boxX = viewport.worldWidth / 2 - boxWidth / 2
    private val boxY = viewport.worldHeight / 2 - boxHeight / 2

    private val rectangleWidth = 550f
    private  val rectangleHeight = 46f


    private var textField = TextField("", skin).apply {
        setBounds((viewport.worldWidth / 2) - (rectangleWidth/2), viewport.worldHeight / 2 - (rectangleHeight/2), rectangleWidth, rectangleHeight)
        messageText = "Spielername eingeben"
    }

    private val textureAtlas = TextureAtlas("graphics/idle_animation.atlas")
    private val playerTexture: Texture = Texture("graphics/idle_animation.png")
    private lateinit var gifAnimation: Animation<TextureRegion>
    private var animationTime = 0f

    private var dotAnimationTime = 0f
    private var dotCount = 0

    private val overlayWidth = 1200f
    private val overlayHeight = 700f
    private val settingsIconSize = 80f
    private var backgroundOffsetX = 0f
    private var backgroundOffsetY = 0f
    private var speedX = 100f
    private var speedY = 50f

    private var isTransitioning = false
    private var transitionRadius = 0f
    private var transitionCenter = Vector2()
    private val maxRadius: Float
        get() = max(viewport.worldWidth, viewport.worldHeight) * 1.5f

    override fun show() {
        music = Gdx.audio.newMusic(Gdx.files.internal("Sounds/Hintergrundmusik/Hintergrundmusik_linguExplorer.mp3"))
        music.isLooping = true
        music.play()
        font = BitmapFont(Gdx.files.internal("fonts/vcr osd mono/vcr osd mono.fnt"))
        gifAnimation = Animation(0.1f, textureAtlas.regions, Animation.PlayMode.LOOP)
        checkTexture = Texture("MainMenu/check.png")
        spielstandStartenButtonTexture = Texture("MainMenu/spielstandStarten.png")
        rectangleTexture = Texture("MainMenu/rectangle.png")

        textField = TextField("Blob", skin).apply {
            setBounds((viewport.worldWidth / 2) - (rectangleWidth/2), viewport.worldHeight / 2 - (rectangleHeight/2), rectangleWidth, rectangleHeight)
            messageText = "Spielername eingeben"
        }



        stage.addActor(textField)
        Gdx.input.inputProcessor = stage


    }



    override fun render(delta: Float) {
        // Reset the flag at the beginning of each frame
        clickProcessedThisFrame = false

        viewport.apply()
        if (!isTransitioning) music.volume = 0.5f * musicVolume * masterVolume
        val screenWidth = viewport.worldWidth
        val screenHeight = viewport.worldHeight

        val mousePos = Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat()).also {
            viewport.unproject(it)
        }

        val backgroundWidth = backgroundTexture.width.toFloat()
        val backgroundHeight = backgroundTexture.height.toFloat()
        val scale = 1.5f
        val scaleX = screenWidth / backgroundWidth * scale
        val scaleY = screenHeight / backgroundHeight * scale
        val scaleFactor = Math.max(scaleX, scaleY)

        val scaledWidth = backgroundWidth * scaleFactor
        val scaledHeight = backgroundHeight * scaleFactor

        transitionCenter.set(viewport.worldWidth / 2f, viewport.worldHeight / 2f)

        backgroundOffsetX += speedX * delta
        backgroundOffsetY += speedY * delta

        if (backgroundOffsetX < -(scaledWidth - screenWidth)) {
            backgroundOffsetX = -(scaledWidth - screenWidth)
            speedX = -speedX
        } else if (backgroundOffsetX > 0) {
            backgroundOffsetX = 0f
            speedX = -speedX
        }

        if (backgroundOffsetY < -(scaledHeight - screenHeight)) {
            backgroundOffsetY = -(scaledHeight - screenHeight)
            speedY = -speedY
        } else if (backgroundOffsetY > 0) {
            backgroundOffsetY = 0f
            speedY = -speedY
        }

        batch.projectionMatrix = viewport.camera.combined
        shapeRenderer.projectionMatrix = viewport.camera.combined

        batch.begin()
        batch.draw(backgroundTexture, backgroundOffsetX, backgroundOffsetY, scaledWidth, scaledHeight)
        batch.end()

        val overlayColor = Color(0f, 0f, 0f, 0.65f)
        Gdx.gl.glEnable(GL20.GL_BLEND)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        shapeRenderer.color = overlayColor
        val overlayX = screenWidth / 2 - overlayWidth / 2
        val overlayY = screenHeight / 2 - overlayHeight / 2
        shapeRenderer.rect(overlayX, overlayY, overlayWidth, overlayHeight)
        shapeRenderer.end()
        Gdx.gl.glDisable(GL20.GL_BLEND)

        batch.begin()
        val wordmarkHeight = 220f
        val wordmarkAspectRatio = wordmarkTexture.width.toFloat() / wordmarkTexture.height.toFloat()
        val wordmarkWidth = wordmarkHeight * wordmarkAspectRatio
        val wordmarkX = screenWidth / 2 - 560f
        val wordmarkY = screenHeight / 2 + 20f
        batch.draw(wordmarkTexture, wordmarkX, wordmarkY, wordmarkWidth, wordmarkHeight)

        val buttonX = screenWidth / 2 - 500f
        val startNewGameButtonY = screenHeight / 2 - 90f
        val loadGameButtonY = screenHeight / 2 - 205f

        batch.draw(startNewGameTexture, buttonX, startNewGameButtonY)
        batch.draw(loadGameTexture, buttonX, loadGameButtonY)

        val settingsX = screenWidth - settingsIconSize - 10f
        val settingsY = screenHeight - settingsIconSize - 10f
        batch.draw(settingsIconTexture, settingsX, settingsY, settingsIconSize, settingsIconSize)

        animationTime += delta
        val currentFrame = gifAnimation.getKeyFrame(animationTime)
        batch.draw(currentFrame, screenWidth / 2 + 180f, screenHeight / 2 - 200f, 300f, 300f)
        batch.end()

        if (showPopup) {
            Gdx.gl.glEnable(GL20.GL_BLEND)
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            shapeRenderer.color = Color(0f, 0f, 0f, 0.5f)
            shapeRenderer.rect(0f, 0f, screenWidth, screenHeight)
            shapeRenderer.end()
            Gdx.gl.glDisable(GL20.GL_BLEND)
            batch.begin()
            batch.draw(popupTexture, popUpPosition.x, popUpPosition.y, popUpSize.x, popUpSize.y)
            batch.draw(exitTexture, popUpPosition.x + popUpSize.x - 110f, popUpPosition.y + popUpSize.y - 110f, exitSize, exitSize)
            font.color = Color.BLACK
            var gamePausedX: Float
            var gamePausedY: Float

            if (Gdx.input.justTouched() && !isTransitioning && !clickProcessedThisFrame) {
                if (isMouseInArea(mousePos.x, mousePos.y, popUpPosition.x + popUpSize.x - 120f, popUpPosition.y + popUpSize.y - 100f, exitSize, exitSize)) {
                    clickProcessedThisFrame = true // Set the flag
                    if (userAlreadyExists) {
                        userAlreadyExists = false
                    } else {
                        showPopup = false
                        loadGamePopUp = false
                        newGamePopUp = false
                        loadGame = false
                        newGame = false
                        showUserExistsConfirmation = false
                        showEnterNameDialog = false
                        showUserEditConfirmation = false
                    }
                }
            }

            if (threadExecuted) {
                if (loadGamePopUp && !userFound) {
                    font.data.setScale(0.4f, 0.4f)
                    glyphLayout.setText(font, "Kein Spielstand vorhanden!")
                    gamePausedX = (viewport.worldWidth - glyphLayout.width) / 2
                    gamePausedY = (viewport.worldHeight + glyphLayout.height) / 2
                    font.draw(batch, "Kein Spielstand vorhanden!", gamePausedX, gamePausedY)
                } else if (userFound || newGamePopUp) {
                    font.data.setScale(0.4f, 0.4f)
                    val slotHeight = 165f
                    val spacing = 20f
                    val startY = popUpPosition.y + popUpSize.y - 120f

                    font.color = Color.BLACK
                    glyphLayout.setText(font, "Wähle einen Spielstand")
                    font.draw(batch, "Wähle einen Spielstand",
                        popUpPosition.x + (popUpSize.x - glyphLayout.width) / 2,
                        startY + 65f)

                    for ((index, slot) in saveSlots.withIndex()) {
                        val (user, checkpoint, topic) = slot

                        val slotY = startY - (slotHeight + spacing) * index
                        val slotX = popUpPosition.x + 90f
                        val slotWidth = popUpSize.x - 180f

                        val slotIsActive = user != null

                        val slotTexture = if (slotIsActive) usedSlotTexture else emptySlotTexture
                        batch.draw(slotTexture, slotX, slotY - slotHeight, slotWidth, slotHeight)

                        font.color = Color.BLACK

                        if (!slotIsActive) {
                            font.data.setScale(0.3f, 0.3f)
                            glyphLayout.setText(font, "Leerer Spielstand")
                            font.draw(
                                batch, "Leerer Spielstand",
                                slotX + (slotWidth - glyphLayout.width) / 2,
                                slotY - slotHeight / 2 + glyphLayout.height / 2
                            )

                            val isBoxHovered = isMouseInArea(
                                mousePos.x,
                                mousePos.y,
                                slotX,
                                slotY - slotHeight,
                                slotWidth,
                                slotHeight
                            )


                            if (isBoxHovered && newGamePopUp && !showUserExistsConfirmation && !showEnterNameDialog) {
                                Gdx.gl.glLineWidth(3f)
                                Gdx.gl.glEnable(GL20.GL_BLEND)
                                batch.end()
                                shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
                                shapeRenderer.color = Color.YELLOW
                                shapeRenderer.rect(slotX, slotY - slotHeight, slotWidth, slotHeight)
                                shapeRenderer.end()
                                batch.begin()
                                Gdx.gl.glDisable(GL20.GL_BLEND)
                                Gdx.gl.glLineWidth(1f)
                            }

                            if (newGamePopUp && Gdx.input.justTouched() && isMouseInArea(
                                    mousePos.x,
                                    mousePos.y,
                                    slotX,
                                    slotY - slotHeight,
                                    slotWidth,
                                    slotHeight
                                ) && !showUserExistsConfirmation && !showEnterNameDialog && !showUserEditConfirmation && !clickProcessedThisFrame
                            ) {
                                clickProcessedThisFrame = true // Set the flag
                                showEnterNameDialog = true
                                currentEditSlotIndex = index
                            }


                        } else {
                            val penX = slotX + slotWidth - 65f
                            val penY = slotY - 65f
                            if (loadGamePopUp) {
                                batch.draw(penTexture, penX, penY, editSize, editSize)

                                val isPenHovered = isMouseInArea(
                                    mousePos.x,
                                    mousePos.y,
                                    penX,
                                    penY,
                                    editSize,
                                    editSize
                                )

                                if (isPenHovered && !showUserExistsConfirmation && !showEnterNameDialog && !showUserEditConfirmation) {
                                    Gdx.gl.glLineWidth(3f)
                                    Gdx.gl.glEnable(GL20.GL_BLEND)
                                    batch.end()
                                    shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
                                    shapeRenderer.color = Color.YELLOW
                                    shapeRenderer.rect(penX, penY, editSize, editSize)
                                    shapeRenderer.end()
                                    batch.begin()
                                    Gdx.gl.glDisable(GL20.GL_BLEND)
                                    Gdx.gl.glLineWidth(1f)
                                }
                            }

                            val isBoxHovered = isMouseInArea(
                                mousePos.x,
                                mousePos.y,
                                slotX,
                                slotY - slotHeight,
                                slotWidth,
                                slotHeight
                            )

                            val isPenHovered = isMouseInArea(
                                mousePos.x,
                                mousePos.y,
                                penX,
                                penY,
                                editSize,
                                editSize
                            )

                            if (isBoxHovered && !isPenHovered && !showUserExistsConfirmation && !showEnterNameDialog && !showUserEditConfirmation) {
                                Gdx.gl.glLineWidth(3f)
                                Gdx.gl.glEnable(GL20.GL_BLEND)
                                batch.end()
                                shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
                                shapeRenderer.color = Color.YELLOW
                                shapeRenderer.rect(slotX, slotY - slotHeight, slotWidth, slotHeight)
                                shapeRenderer.end()
                                batch.begin()
                                Gdx.gl.glDisable(GL20.GL_BLEND)
                                Gdx.gl.glLineWidth(1f)
                            }

                            font.data.setScale(0.23f, 0.23f)
                            val textStartY = slotY - 40f

                            glyphLayout.setText(font, "Name: ${user!!.name}")
                            font.draw(batch, "Name: ${user.name}", slotX + 40f, textStartY)

                            val formattedTime = checkpoint?.currentTime?.atZone(ZoneId.systemDefault())
                                ?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")) ?: "-"
                            glyphLayout.setText(font, "Speicherzeit: $formattedTime")
                            font.draw(batch, "Speicherzeit: $formattedTime", slotX + 40f, textStartY - 35f)

                            glyphLayout.setText(font, "Aktuelles Thema: $topic")
                            font.draw(batch, "Aktuelles Thema: $topic", slotX + 40f, textStartY - 70f)

                            if (Gdx.input.justTouched() && isPenHovered && !showUserExistsConfirmation && !showEnterNameDialog && !showUserEditConfirmation && loadGamePopUp && !clickProcessedThisFrame) {
                                clickProcessedThisFrame = true // Set the flag
                                showUserEditConfirmation = true
                                currentEditSlotIndex = index
                            }

                            if (Gdx.input.justTouched() && isBoxHovered && !isPenHovered && !showUserExistsConfirmation && !showEnterNameDialog && !showUserEditConfirmation && !clickProcessedThisFrame) {
                                clickProcessedThisFrame = true // Set the flag
                                if (newGamePopUp) {
                                    showUserExistsConfirmation = true
                                } else {
                                    if (!newGamePopUp) {
                                        this.user = user
                                        currentCheckpoint = checkpoint!!
                                        topicString = topic!!
                                        loadGame = true
                                    } else {
                                        saveNumber = index + 1
                                        newGame = true
                                    }
                                    isTransitioning = true
                                    loadingTime = 0f
                                }
                            }
                        }


                        if (showUserExistsConfirmation) {
                            val boxWidth = 700f
                            val boxHeight = 250f
                            val boxX = viewport.worldWidth / 2 - boxWidth / 2
                            val boxY = viewport.worldHeight / 2 - boxHeight / 2

                            batch.draw(userExistsBoxTexture, boxX, boxY, boxWidth, boxHeight)

                            val xButtonSize = 90f
                            val checkButtonSize = 90f
                            val xButtonX = boxX + 200f
                            val xButtonY = boxY + 40f
                            val checkButtonX = boxX + boxWidth - 200f - checkButtonSize
                            val checkButtonY = boxY + 40f

                            batch.draw(exitTexture, xButtonX, xButtonY, xButtonSize, xButtonSize)
                            batch.draw(checkTexture, checkButtonX, checkButtonY, checkButtonSize, checkButtonSize)

                            font.data.setScale(0.23f, 0.23f)
                            val text = "Bist du dir sicher, dass du den\nSpielstand überschreiben möchtest?"
                            glyphLayout.setText(font, text)
                            val textX = boxX + (boxWidth - glyphLayout.width) / 2
                            val textY = boxY + boxHeight - 60f
                            font.draw(batch, text, textX, textY)

                            if (Gdx.input.justTouched() && !clickProcessedThisFrame) {
                                if (isMouseInArea(
                                        mousePos.x,
                                        mousePos.y,
                                        xButtonX,
                                        xButtonY,
                                        xButtonSize,
                                        xButtonSize
                                    )
                                ) {
                                    clickProcessedThisFrame = true // Set the flag
                                    showUserExistsConfirmation = false
                                } else if (isMouseInArea(
                                        mousePos.x,
                                        mousePos.y,
                                        checkButtonX,
                                        checkButtonY,
                                        checkButtonSize,
                                        checkButtonSize
                                    )
                                ) {
                                    clickProcessedThisFrame = true // Set the flag
                                    showUserExistsConfirmation = false
                                    showEnterNameDialog = true
                                }
                            }
                        } else if (showUserEditConfirmation) {
                            val boxWidth = 700f
                            val boxHeight = 250f
                            val boxX = viewport.worldWidth / 2 - boxWidth / 2
                            val boxY = viewport.worldHeight / 2 - boxHeight / 2

                            batch.draw(userExistsBoxTexture, boxX, boxY, boxWidth, boxHeight)

                            val xButtonSize = 90f
                            val checkButtonSize = 90f
                            val xButtonX = boxX + 200f
                            val xButtonY = boxY + 40f
                            val checkButtonX = boxX + boxWidth - 200f - checkButtonSize
                            val checkButtonY = boxY + 40f

                            batch.draw(exitTexture, xButtonX, xButtonY, xButtonSize, xButtonSize)
                            batch.draw(checkTexture, checkButtonX, checkButtonY, checkButtonSize, checkButtonSize)

                            font.data.setScale(0.23f, 0.23f)
                            val text = "Bist du dir sicher, dass du diesen\nSpielstand löschen möchtest?"
                            glyphLayout.setText(font, text)
                            val textX = boxX + (boxWidth - glyphLayout.width) / 2
                            val textY = boxY + boxHeight - 60f
                            font.draw(batch, text, textX, textY)

                            if (Gdx.input.justTouched() && !clickProcessedThisFrame) {
                                if (isMouseInArea(
                                        mousePos.x,
                                        mousePos.y,
                                        xButtonX,
                                        xButtonY,
                                        xButtonSize,
                                        xButtonSize
                                    )
                                ) {
                                    clickProcessedThisFrame = true // Set the flag
                                    showUserEditConfirmation = false
                                    currentEditSlotIndex = -1
                                } else if (isMouseInArea(
                                        mousePos.x,
                                        mousePos.y,
                                        checkButtonX,
                                        checkButtonY,
                                        checkButtonSize,
                                        checkButtonSize
                                    )
                                ) {
                                    deleteCheckpoint(saveSlots[currentEditSlotIndex].first!!.id, saveSlots[currentEditSlotIndex].first!!.saveNumber)
                                }
                            }
                        } else if (showEnterNameDialog) {
                            val boxWidth = 700f
                            val boxHeight = 250f
                            val boxX = viewport.worldWidth / 2 - boxWidth / 2
                            val boxY = viewport.worldHeight / 2 - boxHeight / 2

                            batch.draw(userExistsBoxTexture, boxX, boxY, boxWidth, boxHeight)

                            font.data.setScale(0.23f, 0.23f)
                            val text = "Spielernamen eingeben:"
                            glyphLayout.setText(font, text)
                            val textX = boxX + (boxWidth - glyphLayout.width) / 2
                            val textY = boxY + boxHeight - 60f
                            font.draw(batch, text, textX, textY)

                            val rectangleWidth = 550f
                            val rectangleHeight = 46f
                            val rectangleX = boxX + (boxWidth - rectangleWidth) / 2
                            val rectangleY = boxY + boxHeight - 140f

                            batch.end()
                            stage.act(delta)
                            stage.draw()
                            batch.begin()

                            val buttonWidth = 170f
                            val buttonHeight = 50f
                            val buttonX = boxX + (boxWidth - buttonWidth) / 2
                            val buttonY = boxY + 40f
                            batch.draw(spielstandStartenButtonTexture, buttonX, buttonY, buttonWidth, buttonHeight)

                            if (Gdx.input.justTouched() && !clickProcessedThisFrame) {
                                if (isMouseInArea(
                                        mousePos.x,
                                        mousePos.y,
                                        buttonX,
                                        buttonY,
                                        buttonWidth,
                                        buttonHeight
                                    )
                                ) {
                                    clickProcessedThisFrame = true // Set the flag
                                    name = textField.text.toString()
                                    if (slotIsActive) {
                                        userAlreadyExists = true
                                        this.user = user!!
                                        saveNumber = user.saveNumber
                                        currentCheckpoint = checkpoint!!
                                    } else {
                                        saveNumber = currentEditSlotIndex + 1
                                        newGame = true
                                    }

                                    showEnterNameDialog = false
                                    showUserExistsConfirmation = false
                                    isTransitioning = true
                                    loadingTime = 0f
                                }
                            }
                        }
                    }
                }
            } else {
                loadingTime += delta

                font.data.setScale(0.4f, 0.4f)
                dotAnimationTime += delta
                if (dotAnimationTime >= 0.5f) {
                    dotAnimationTime = 0f
                    dotCount = (dotCount + 1) % 4
                }

                val animatedDots = ".".repeat(dotCount)
                val loadingText = when {
                    loadingTime >= 30f -> "Da ist was schiefgelaufen :(\nVersuch es nochmal"
                    loadingTime >= 18f -> "Fast geschafft$animatedDots"
                    loadingTime >= 9f -> "Hab noch Geduld$animatedDots"
                    else -> "Loading$animatedDots"
                }

                glyphLayout.setText(font, loadingText)
                gamePausedX = (viewport.worldWidth - glyphLayout.width) / 2
                gamePausedY = (viewport.worldHeight + glyphLayout.height) / 2
                font.draw(batch, loadingText, gamePausedX, gamePausedY)
            }

            batch.end()
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) && !isTransitioning ) {
            userAlreadyExists = false
            showPopup = false
            loadGamePopUp = false
            newGamePopUp = false
            showUserExistsConfirmation = false
            showEnterNameDialog = false
            showUserEditConfirmation = false
            showMenu = false
        }

        if (isTransitioning) {
            if (!soundPlayed) {
                selectSound.play(0.7f * masterVolume * soundEffectVolume)
                soundPlayed = true
            }
            transitionRadius += 1500 * delta
            if (transitionRadius >= maxRadius) {
                loadingTime += delta
                if (loadGamePopUp || newGamePopUp) {
                    loadCheckpoint()
                    loadGamePopUp = false
                    newGamePopUp = false
                }

                loadingScreenRenderer.renderAnimatedText(batch, font, glyphLayout, viewport,"Loading", delta, 1f, true)
                if (threadExecuted && loadingTime > 3f) {
                    Gdx.app.postRunnable {
                        isTransitioning = false
                        transitionRadius = 0f
                        loadingTime = 0f

                        if (introduction) {
                            game.addScreen(IntroductionScreen(game))
                            game.setScreen<IntroductionScreen>()
                            game.removeScreen<MainMenuScreen>()
                        } else {
                            if (game.containsScreen<MapScreen>()) {
                                game.removeScreen<MapScreen>()
                            }
                            game.addScreen(MapScreen(game, 30f, 30f))
                            game.setScreen<MapScreen>()
                            game.removeScreen<MainMenuScreen>()
                        }

                    }
                }
                return
            }

            if(music.volume > 0.005f) {
                music.volume -= (0.006f * masterVolume * musicVolume)
            } else if (music.volume <= 0.01f) {
                music.pause()
            }

            Gdx.gl.glEnable(GL20.GL_BLEND)
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            shapeRenderer.color = Color.BLACK
            shapeRenderer.circle(transitionCenter.x, transitionCenter.y, transitionRadius)
            shapeRenderer.end()
            Gdx.gl.glDisable(GL20.GL_BLEND)
        }

        if (showPopup) {
            return
        }

        // Setzt OnResumeClicked-Listener
        gameMenuRenderer.setOnResumeClicked {
            showMenu = false
        }

        if (showMenu) {
            gameMenuRenderer.renderGameMenu(batch, font, glyphLayout, viewport, shapeRenderer, true, game)
            return
        }

        //wenn der Benutzer auf einen Button klickt
        if (Gdx.input.justTouched()) {
            //wenn der Benutzer auf das Settings-Icon klickt...
            if (isMouseInArea(mousePos.x, mousePos.y, settingsX, settingsY, settingsIconSize, settingsIconSize)) {
                // Zeigt das Menü an
                showMenu = true
                return
            }

            // auf "Neues Spiel"-Button klicken
            if (isMouseInArea(mousePos.x, mousePos.y, buttonX, startNewGameButtonY, startNewGameTexture.width.toFloat(), startNewGameTexture.height.toFloat())) {
                // Zeigt Popup-Fenster an
                showPopup = true
                newGamePopUp = true
                // Überprüft Benutzer
                checkUser()
            }

            //auf "Spielstand laden" klicken
            if (isMouseInArea(mousePos.x, mousePos.y, buttonX, loadGameButtonY, loadGameTexture.width.toFloat(), loadGameTexture.height.toFloat())) {
                showPopup = true
                loadGamePopUp = true
                checkUser()
            }
        }
    }

    // Überprüft den Benutzer (asynchron)
    private fun checkUser() {
        threadExecuted = false
        executor.submit {
            if(!userFound) {
                val allUsers = UserRepository().getAllUsersById(userId)
                val allCheckpoints = CheckpointRepository().getAllCheckpointsForUser(userId) ?: emptyList()

                saveSlots.clear()

                println("User Id ist $userId")

                for (i in 0 until 3) {
                    saveSlots.add(Triple(null, null, null))
                }

                for (user in allUsers) {
                    val saveIndex = user.saveNumber - 1

                    if (saveIndex in 0..2) {
                        val checkpoint = allCheckpoints.find { it.saveNumber == user.saveNumber }

                        val latestProgress = UserProgressRepository().getLatestUserProgress(user.id, user.saveNumber)
                        val topic = latestProgress?.let { TopicRepository().getTopicById(it.topicId)?.name } ?: "-"

                        saveSlots[saveIndex] = Triple(user, checkpoint, topic)
                    }
                }

                userFound = saveSlots.any { it.first != null }
            }
            threadExecuted = true
            loadingTime = 0f
        }
    }

    private fun deleteCheckpoint (userId: Int, saveNumber: Int) {
        threadExecuted = false
        executor.submit {
            UserRepository().deleteUserWithDependencies(userId, saveNumber)
            userFound = false
            showUserEditConfirmation = false
            clickProcessedThisFrame = true
            currentEditSlotIndex = -1
            checkUser()
        }
        threadExecuted = true
    }


    // Lädt den Checkpoint (asynchron)
    private fun loadCheckpoint() {
        threadExecuted = false
        executor.submit {
            if(userAlreadyExists) {
                UserRepository().deleteUserWithDependencies(user.id, user.saveNumber)
                UserRepository().addUser(userId, saveNumber, name)
                CheckpointRepository().addCheckpoint(userId, saveNumber, 0, 30f, 30f, Timestamp(System.currentTimeMillis()))
                executePositionX = 30f
                executePositionY = 30f
            } else if(newGame) {
                UserRepository().addUser(userId, saveNumber, name)
                CheckpointRepository().addCheckpoint(userId, saveNumber, 0, 30f, 30f, Timestamp(System.currentTimeMillis()))
                executePositionX = 30f
                executePositionY = 30f
            } else {
                CheckpointRepository().updateCheckpoint(user.id, user.saveNumber, null, null, null, Timestamp(System.currentTimeMillis()))
                saveNumber = user.saveNumber
                println (topicString)
                executePositionX = currentCheckpoint.positionX
                executePositionY = currentCheckpoint.positionY
                println (executePositionX)
            }

            if (newGame || userAlreadyExists || (topicString == "-")) {
                println("ho")
                val topicId = TopicRepository().getTopicIdByName("Kleidung")
                currentTopic = TopicRepository().getTopicById(topicId)!!
                if (topicString != "-") introduction = true
            } else {
                println(topicString)
                val topicId = TopicRepository().getTopicIdByName(topicString)
                currentTopic = TopicRepository().getTopicById(topicId)!!
                updateUserInformation(PhraseProgressHistoryRepository().getAllEntriesForUser(userId, saveNumber), topicId!!)
            }
            println("HI")
            newGame = false
            loadGame = false
            userAlreadyExists = false
            println(saveNumber)
            threadExecuted = true
            println("Done")
        }
    }

    // ob sich Maus innerhalb eines Bereichs befindet
    private fun isMouseInArea(mouseX: Float, mouseY: Float, areaX: Float, areaY: Float, areaWidth: Float, areaHeight: Float): Boolean {
        return mouseX >= areaX && mouseX <= areaX + areaWidth &&
            mouseY >= areaY && mouseY <= areaY + areaHeight
    }

    fun updateUserInformation(userHistory: List<PhraseProgressHistoryEntity>, topicId: Int) {
        val historyRepo = PhraseProgressHistoryRepository()
        val phraseList = allPhrasesList.filter { it.topicId == topicId }

        var totalScore = 0.0
        var count = 0

        phraseList.forEach { phrase ->
            val correctIndex = historyRepo.calculateCorrectIndex(phrase.id, userHistory)
            var score = 0.0
            if (correctIndex >= phraseIndex) {
                score = 1.0
            } else if (correctIndex == -1.0) {
                score = 0.0
            } else {
                score = correctIndex / phraseIndex
            }

            totalScore += score
            count++
        }
        if (topicId == currentTopic.id) {
            topicProgress = if (count > 0) totalScore / count else 0.0
            println(topicProgress)
        }
    }

    //wenn die Fenstergröße geändert wird
    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    //wenn der Screen geschlossen
    override fun dispose() {
        playerTexture.disposeSafely()
        textureAtlas.disposeSafely()
        batch.dispose()
        shapeRenderer.dispose()
        backgroundTexture.dispose()
        startNewGameTexture.dispose()
        loadGameTexture.dispose()
        settingsIconTexture.dispose()
        wordmarkTexture.dispose()
        popupTexture.dispose()
        userExistsBoxTexture.disposeSafely()
        checkTexture.disposeSafely()
        rectangleTexture.disposeSafely()
        spielstandStartenButtonTexture.disposeSafely()
        skin.disposeSafely()
        stage.disposeSafely()
    }

    companion object {
        private val log = logger<MapScreen>()
    }
}
