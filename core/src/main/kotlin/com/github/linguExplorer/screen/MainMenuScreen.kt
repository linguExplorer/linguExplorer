package com.github.linguExplorer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.*
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.github.linguExplorer.linguExplorer
import com.github.linguExplorer.models.CheckpointEntity
import com.github.linguExplorer.models.TopicEntity
import com.github.linguExplorer.models.UserEntity
import com.github.linguExplorer.repositories.CheckpointRepository
import com.github.linguExplorer.repositories.TopicRepository
import com.github.linguExplorer.repositories.UserProgressRepository
import com.github.linguExplorer.repositories.UserRepository
import com.github.linguExplorer.userId
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
    private var shapeRenderer: ShapeRenderer = ShapeRenderer()
    private lateinit var font: BitmapFont
    private val viewport: Viewport = ExtendViewport(1920f, 1080f)
    private val glyphLayout = GlyphLayout()

    private var backgroundTexture: Texture = Texture("xx_map_assets/Map/ref.png")
    private var startNewGameTexture: Texture = Texture("xx_Images/Buttons/neuesSpiel_green.png")
    private var loadGameTexture: Texture = Texture("xx_Images/Buttons/spielstandLaden_green.png")
    private var settingsIconTexture: Texture = Texture("xx_Images/SettingsIcon.png")
    private var wordmarkTexture: Texture = Texture("xx_Images/wordmark/wordmark_scaled.png")
    private var popupTexture: Texture = Texture("box.png")
    private var exitTexture: Texture = Texture("xx_Images/Buttons/red_X.png")
    private val executor: ExecutorService = Executors.newFixedThreadPool(1)
    private var showPopup = false
    private var newGame = false
    private var loadGame = false
    private var threadExecuted = false
    private var loadingTime = 0f
    private var userFound = false
    private lateinit var user: UserEntity
    private lateinit var currentTopic: String
    private lateinit var currentCheckpoint: CheckpointEntity
    private var loadingScreenRenderer = LoadingScreenRenderer()

    private val popUpSize = Vector2(1000f, 600f)
    private val exitSize = 90f
    private val popUpPosition: Vector2
        get() = Vector2(
            (viewport.worldWidth / 2 - popUpSize.x / 2),
            (viewport.worldHeight / 2 - popUpSize.y / 2)
        )

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
        val music: Music = Gdx.audio.newMusic(Gdx.files.internal("Sounds/Hintergrundmusik/Hintergrundmusik_linguExplorer.mp3"))
        music.isLooping = true
        music.volume = 0.5f
        music.play()
        font = BitmapFont(Gdx.files.internal("fonts/vcr osd mono/vcr osd mono.fnt"))
        gifAnimation = Animation(0.1f, textureAtlas.regions, Animation.PlayMode.LOOP)
    }

    override fun render(delta: Float) {
        viewport.apply()
        val screenWidth = viewport.worldWidth
        val screenHeight = viewport.worldHeight

        // Mauskoordinaten in Weltkoordinaten umrechnen
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

        transitionCenter.set(viewport.screenWidth / 2f, viewport.screenHeight / 2f)

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
            batch.draw(exitTexture, popUpPosition.x + popUpSize.x - 120f, popUpPosition.y + popUpSize.y - 100f, exitSize, exitSize)
            font.color = Color.BLACK
            var gamePausedX: Float
            var gamePausedY: Float

            if (threadExecuted) {
                if (newGame) {
                    if (!userFound) {
                        font.data.setScale(0.4f, 0.4f)
                        glyphLayout.setText(font, "Game start...")
                        gamePausedX = (viewport.worldWidth - glyphLayout.width) / 2
                        gamePausedY = (viewport.worldHeight + glyphLayout.height) / 2
                        font.draw(batch, "Game start...", gamePausedX, gamePausedY)
                        isTransitioning = true
                    } else {
                        font.data.setScale(0.4f, 0.4f)
                        glyphLayout.setText(font, "Spielstand bereits vorhanden!")
                        gamePausedX = (viewport.worldWidth - glyphLayout.width) / 2
                        gamePausedY = (viewport.worldHeight + glyphLayout.height) / 2
                        font.draw(batch, "Spielstand bereits vorhanden!", gamePausedX, gamePausedY)
                    }
                } else if (loadGame) {
                    if (!userFound) {
                        font.data.setScale(0.4f, 0.4f)
                        glyphLayout.setText(font, "Kein Spielstand vorhanden!")
                        gamePausedX = (viewport.worldWidth - glyphLayout.width) / 2
                        gamePausedY = (viewport.worldHeight + glyphLayout.height) / 2
                        font.draw(batch, "Kein Spielstand vorhanden!", gamePausedX, gamePausedY)
                    } else {
                        batch.draw(
                            currentFrame,
                            popUpPosition.x + popUpSize.x - 250f,
                            viewport.worldHeight / 2 - 250f,
                            200f,
                            200f
                        )
                        font.data.setScale(0.25f, 0.25f)
                        glyphLayout.setText(font, "Name: ${user.name}")
                        gamePausedX = popUpPosition.x + 80f
                        gamePausedY = (viewport.worldHeight + glyphLayout.height) / 2 + 180f
                        val firstGamePausedY = gamePausedY
                        font.draw(batch, "Name: ${user.name}", gamePausedX, gamePausedY)

                        val formattedTime = currentCheckpoint.currentTime.atZone(ZoneId.systemDefault())
                            .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
                        glyphLayout.setText(font, "Zuletzt gespeichert: $formattedTime")
                        gamePausedY -= glyphLayout.height + 20f
                        font.draw(batch, "Speicherzeit: $formattedTime", gamePausedX, gamePausedY)

                        glyphLayout.setText(font, "Aktuelles Thema: $currentTopic")
                        gamePausedY -= glyphLayout.height + 20f
                        font.draw(batch, "Aktuelles Thema: $currentTopic", gamePausedX, gamePausedY)

                        batch.end()

                        val rectX = gamePausedX - 20f
                        val rectY = firstGamePausedY + 20f
                        val rectWidth = popUpSize.x - 160f
                        val rectHeight = -(gamePausedY - gamePausedX + 40f)

                        val isMouseOver = isMouseInArea(mousePos.x, mousePos.y, rectX, rectY + rectHeight, rectWidth, -rectHeight)

                        var isClicked = false
                        if (isMouseOver && Gdx.input.isTouched) {
                            isClicked = true
                        }

                        Gdx.gl.glLineWidth(3f)
                        Gdx.gl.glEnable(GL20.GL_BLEND)
                        shapeRenderer.begin(ShapeRenderer.ShapeType.Line)

                        when {
                            isClicked -> {
                                shapeRenderer.color = Color.GREEN
                                isTransitioning = true
                            }
                            isMouseOver -> shapeRenderer.color = Color.YELLOW
                            else -> shapeRenderer.color = Color(0f, 0f, 0f, 0.5f)
                        }
                        shapeRenderer.rect(rectX, rectY, rectWidth, rectHeight)
                        shapeRenderer.end()
                        Gdx.gl.glDisable(GL20.GL_BLEND)
                        Gdx.gl.glLineWidth(1f)

                        batch.begin()
                    }
                }
            } else {
                loadingTime += delta // Zeit aktualisieren

                font.data.setScale(0.4f, 0.4f)
                dotAnimationTime += delta
                if (dotAnimationTime >= 0.5f) { // Alle 0,5 Sekunden ändern
                    dotAnimationTime = 0f
                    dotCount = (dotCount + 1) % 4 // 0, 1, 2, 3 Punkte
                }

                val animatedDots = ".".repeat(dotCount)
                val loadingText = when {
                    loadingTime >= 30f -> ":("
                    loadingTime >= 20f -> "Only a few more seconds$animatedDots"
                    loadingTime >= 10f -> "Lean back and wait$animatedDots"
                    else -> "Loading$animatedDots"
                }

                glyphLayout.setText(font, loadingText)
                gamePausedX = (viewport.worldWidth - glyphLayout.width) / 2
                gamePausedY = (viewport.worldHeight + glyphLayout.height) / 2
                font.draw(batch, loadingText, gamePausedX, gamePausedY)
            }

            batch.end()
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            showPopup = false
            loadGame = false
            newGame = false
        }

        if (isTransitioning) {
            transitionRadius += 1500 * delta
            if (transitionRadius >= maxRadius) {
                if (loadGame) {
                    loadCheckpoint()
                    loadGame = false
                }

                if (!threadExecuted || threadExecuted) {
                    loadingScreenRenderer.renderAnimatedText(batch, font, glyphLayout, viewport,"Loading...", delta, 1f, true)
                } else {
                    game.addScreen(MapScreen(game, currentCheckpoint.positionX, currentCheckpoint.positionY))
                    game.setScreen<MapScreen>()
                    isTransitioning = false
                    transitionRadius = 0f
                }
                return
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

        if (Gdx.input.justTouched()) {
            if (isMouseInArea(mousePos.x, mousePos.y, buttonX, startNewGameButtonY, startNewGameTexture.width.toFloat(), startNewGameTexture.height.toFloat())) {
                showPopup = true
                newGame = true
                checkUser()
            }

            if (isMouseInArea(mousePos.x, mousePos.y, buttonX, loadGameButtonY, loadGameTexture.width.toFloat(), loadGameTexture.height.toFloat())) {
                showPopup = true
                loadGame = true
                checkUser()
            }

            if (isMouseInArea(mousePos.x, mousePos.y, settingsX, settingsY, settingsIconSize, settingsIconSize)) {
                openWebpage("https://www.example.com")
            }
        }
    }

    private fun checkUser() {
        threadExecuted = false
        executor.submit {
            if (!userFound) {
                    userFound = UserRepository().getUser(userId) != null
                if (userFound) {
                    user = UserRepository().getUser(userId)!!
                    currentCheckpoint = CheckpointRepository().getCheckpoint(user.id)!!
                }
                val latestProgress = UserProgressRepository().getLatestUserProgress(userId)
                currentTopic = latestProgress?.let {
                    TopicRepository().getTopicById(it.topicId)!!.name
                } ?: "-"
            }
            threadExecuted = true
        }
    }

    private fun loadCheckpoint() {
        threadExecuted = false
        executor.submit {
            CheckpointRepository().upsertCheckpoint(user.id, null, null, null, Timestamp(System.currentTimeMillis()))
            threadExecuted = true
        }
    }

    private fun isMouseInArea(mouseX: Float, mouseY: Float, areaX: Float, areaY: Float, areaWidth: Float, areaHeight: Float): Boolean {
        return mouseX >= areaX && mouseX <= areaX + areaWidth &&
            mouseY >= areaY && mouseY <= areaY + areaHeight
    }

    private fun openWebpage(url: String) {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().browse(URI(url))
        } else {
            println("Desktop-Modus nicht unterstützt.")
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

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
    }

    companion object {
        private val log = logger<MapScreen>()
    }
}
