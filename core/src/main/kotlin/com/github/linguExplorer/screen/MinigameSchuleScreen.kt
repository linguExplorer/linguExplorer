package com.github.linguExplorer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.Viewport
import com.badlogic.gdx.math.Vector2
import ktx.app.KtxScreen
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.utils.Align

class MinigameSchuleScreen : KtxScreen {

    private val batch = SpriteBatch()
    private lateinit var font: BitmapFont
    private val viewport: Viewport = ExtendViewport(800f, 600f)
    private val shapeRenderer = ShapeRenderer()

    // Texturen (Uhr und Play/Pause, Continue, Stundenplan)
    private val timeTexture = Texture(Gdx.files.internal("Minigames/time.png"))
    private var pauseTexture = Texture(Gdx.files.internal("Minigames/pausebutton.png"))
    private var playTexture = Texture(Gdx.files.internal("Minigames/playbutton.png"))
    private val continueTexture = Texture(Gdx.files.internal("Minigames/btn_continue.png"))
    private val stundenplanTexture = Texture(Gdx.files.internal("Minigames/school/timetable/stundenplan_final.png")) // Stundenplan Textur laden

    // Positionen und Größen (Uhr, Play/Pause, Continue, Stundenplan)
    private val timeBasePosition = Vector2(20f, 530f)
    private val timeSize = Vector2(150f, 50f)
    private val pauseBasePosition = Vector2(180f, 530f)
    private val pauseSize = Vector2(50f, 50f)
    private val continueButtonBasePosition = Vector2(300f, 200f) // Beispielposition
    private val buttonSize = Vector2(200f, 70f) //Beispielgröße

    // Stundenplan Position und Größe
    private val stundenplanWidth = 500f
    private val stundenplanHeight = 410f
    private val stundenplanPositionX = 270f // Rechts
    private val stundenplanPositionY = 160f // Oben

    private var pauseButtonScale = 1f
    private var pauseButtonTargetScale = 1f
    private var continueButtonScale = 1f
    private var continueButtonTargetScale = 1f
    private val scaleSpeed = 5f

    // Zeit
    private var timeLeft = 10 // Startzeit in Sekunden
    private var elapsedTime = 0f

    private var isPaused = false
    private var gameStarted = false
    private var gameOver = false // Flag für Game Over

    // Getter für die dynamischen Positionen (Uhr, Play/Pause, Continue)
    private val timePosition: Vector2
        get() = Vector2(
            timeBasePosition.x,
            timeBasePosition.y * (viewport.worldHeight / 600f)
        )

    private val pausePosition: Vector2
        get() = Vector2(
            pauseBasePosition.x,
            pauseBasePosition.y * (viewport.worldHeight / 600f)
        )

    private val continueButtonPosition: Vector2
        get() = Vector2(
            continueButtonBasePosition.x * (viewport.worldWidth / 800f),
            continueButtonBasePosition.y * (viewport.worldHeight / 600f)
        )

    //Getter für den Stundenplan
    private val stundenplanPosition: Vector2
        get() = Vector2(
            stundenplanPositionX * (viewport.worldWidth / 800f),
            stundenplanPositionY * (viewport.worldHeight / 600f)
        )

    override fun show() {
        // NICHT auf null setzen! Sonst empfängst du keine Inputs.
        // Gdx.input.inputProcessor = null
    }

    override fun render(delta: Float) {
        handleInput(delta)

        if (!isPaused && gameStarted && !gameOver) {
            updateTime(delta)
        }

        Gdx.gl.glClearColor(0.85490196f, 0.80784315f, 0.87058824f, 1f) // #dacede als RGB
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        font = BitmapFont(Gdx.files.internal("fonts/vcr osd mono/vcr osd mono.fnt"))

        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        font.color = Color.BLACK

        pauseButtonScale += (pauseButtonTargetScale - pauseButtonScale) * scaleSpeed * delta
        continueButtonScale += (continueButtonTargetScale - continueButtonScale) * scaleSpeed * delta

        batch.begin()

        // Zeit zeichnen
        batch.draw(timeTexture, timePosition.x, timePosition.y, timeSize.x, timeSize.y)
        font.data.setScale(0.3f, 0.3f)
        font.draw(batch, formatTime(timeLeft), timePosition.x + 20f, timePosition.y + timeSize.y / 1.4f)

        // Pause- / Play-Button zeichnen
        val texture: Texture = if (isPaused) playTexture else pauseTexture
        batch.draw(
            texture,
            pausePosition.x - (pauseSize.x * (pauseButtonScale - 1f) / 2),
            pausePosition.y - (pauseSize.y * (pauseButtonScale - 1f) / 2),
            pauseSize.x * pauseButtonScale,
            pauseSize.y * pauseButtonScale
        )

        // Stundenplan anzeigen (nur wenn das Spiel läuft und nicht vorbei ist)
        if (gameStarted && !gameOver) {
            batch.draw(stundenplanTexture, stundenplanPosition.x, stundenplanPosition.y, stundenplanWidth * (viewport.worldWidth/800f), stundenplanHeight * (viewport.worldHeight/600f))
        }

        // Game Over Anzeige
        if (gameOver) {
            batch.end()
            Gdx.gl.glEnable(GL20.GL_BLEND)
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            shapeRenderer.color = Color(0f, 0f, 0f, 0.5f)
            shapeRenderer.rect(0f, 0f, viewport.screenWidth.toFloat(), viewport.screenHeight.toFloat())
            shapeRenderer.end()
            Gdx.gl.glDisable(GL20.GL_BLEND)
            batch.begin()

            font.color = Color.WHITE
            val glyphLayout = GlyphLayout()
            font = BitmapFont(Gdx.files.internal("fonts/pixelsplitter/pixelsplitter.fnt"))
            font.data.setScale(0.7f, 0.7f)

            glyphLayout.setText(font, "GAME OVER")
            val gameOverX = (viewport.worldWidth - glyphLayout.width) / 2
            val gameOverY = (viewport.worldHeight) / 2 + glyphLayout.height + 10f
            font.draw(batch, "GAME OVER", gameOverX, gameOverY)
        }
        // Startbildschirm
        else if (!gameStarted && !gameOver) { // Hinzugefügte Bedingung: !gameOver
            batch.end()
            Gdx.gl.glEnable(GL20.GL_BLEND)
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            shapeRenderer.color = Color(0f, 0f, 0f, 0.65f)
            shapeRenderer.rect(0f, 0f, viewport.screenWidth.toFloat(), viewport.screenHeight.toFloat())
            shapeRenderer.end()
            Gdx.gl.glDisable(GL20.GL_BLEND)
            batch.begin()

            font.color = Color.WHITE
            val glyphLayout = GlyphLayout()
            font.data.setScale(0.4f, 0.4f)

            // Breiteres Rechteck für die Zentrierung
            val textWidth = viewport.worldWidth * 0.75f // 75% der Bildschirmbreite
            glyphLayout.setText(font, "Erklärung", Color.WHITE, textWidth, Align.center, true)

            val textX = (viewport.worldWidth - glyphLayout.width) / 2 // Horizontale Mitte
            val textY = (viewport.worldHeight / 2) + glyphLayout.height // Vertikale Mitte + Höhe für bessere Positionierung

            font.draw(batch, "Erklärung", textX, textY)

            // Continue-Button anzeigen
            batch.draw(
                continueTexture,
                continueButtonPosition.x - (buttonSize.x * (continueButtonScale - 1f) / 2),
                continueButtonPosition.y - (buttonSize.y * (continueButtonScale - 1f) / 2),
                buttonSize.x * continueButtonScale,
                buttonSize.y * continueButtonScale
            )
        }
        // Game Paused Screen
        else if (isPaused && gameStarted && !gameOver) {
            batch.end()
            Gdx.gl.glEnable(GL20.GL_BLEND)
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
            shapeRenderer.color = Color(0f, 0f, 0f, 0.65f) // Abdunkelnder Hintergrund
            shapeRenderer.rect(0f, 0f, viewport.screenWidth.toFloat(), viewport.screenHeight.toFloat())
            shapeRenderer.end()
            Gdx.gl.glDisable(GL20.GL_BLEND)
            batch.begin()

            font.color = Color.WHITE
            val glyphLayout = GlyphLayout()
            font.data.setScale(0.7f, 0.7f)
            glyphLayout.setText(font, "GAME PAUSED")

            // Zentriere den Text "GAME PAUSED"
            val gamePausedX = (viewport.worldWidth - glyphLayout.width) / 2
            val gamePausedY = (viewport.worldHeight / 2) + glyphLayout.height + 10f
            font.draw(batch, "GAME PAUSED", gamePausedX, gamePausedY)

            // Continue-Button zentrieren
            val continueButtonX = (viewport.worldWidth - buttonSize.x) / 2 // Horizontale Mitte
            val continueButtonY = continueButtonBasePosition.y * (viewport.worldHeight / 600f) // Beibehaltung der vertikalen Position

            batch.draw(
                continueTexture,
                continueButtonX - (buttonSize.x * (continueButtonScale - 1f) / 2), // Berücksichtige Skalierung
                continueButtonY - (buttonSize.y * (continueButtonScale - 1f) / 2),  // Berücksichtige Skalierung
                buttonSize.x * continueButtonScale,
                buttonSize.y * continueButtonScale
            )

        }
        else {
            // Spielcode
            // Hier kommt dein eigentlicher Spielcode hin, der während des Spiels ausgeführt wird.
        }

        batch.end()
    }

    private fun handleInput(delta: Float) {
        val mouseX = Gdx.input.x.toFloat() * viewport.worldWidth / Gdx.graphics.width
        val mouseY = (Gdx.graphics.height - Gdx.input.y.toFloat()) * viewport.worldHeight / Gdx.graphics.height

        pauseButtonTargetScale = if (mouseX in pausePosition.x..(pausePosition.x + pauseSize.x) &&
            mouseY in pausePosition.y..(pausePosition.y + pauseSize.y)) {
            1.1f
        } else {
            1f
        }

        continueButtonTargetScale = if (mouseX in continueButtonPosition.x..(continueButtonPosition.x + buttonSize.x) &&
            mouseY in continueButtonPosition.y..(continueButtonPosition.y + buttonSize.y)) {
            1.1f
        } else {
            1f
        }


        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (!gameStarted) { // Wenn das Spiel noch nicht gestartet wurde
                if (mouseX in continueButtonPosition.x..(continueButtonPosition.x + buttonSize.x) &&
                    mouseY in continueButtonPosition.y..(continueButtonPosition.y + buttonSize.y)) {
                    gameStarted = true // Starte das Spiel
                }
            }  // Wenn das Spiel bereits läuft
            else if (mouseX in continueButtonPosition.x..(continueButtonPosition.x + buttonSize.x) && mouseY in continueButtonPosition.y..(continueButtonPosition.y + buttonSize.y) && isPaused) {
                isPaused = !isPaused
            }
            else if (gameStarted && mouseX in pausePosition.x..(pausePosition.x + pauseSize.x) &&
                mouseY in pausePosition.y..(pausePosition.y + pauseSize.y)) {
                isPaused = true
            }

        } else if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) && gameStarted) {
            isPaused = true
        }
    }

    private fun updateTime(delta: Float) {
        if (!isPaused && gameStarted && !gameOver) {
            elapsedTime += delta
            if (elapsedTime >= 1f) {
                timeLeft--
                elapsedTime -= 1f
            }

            if (timeLeft <= 0 && !gameOver) {
                gameOver = true
                gameStarted = false
            }
        }
    }

    private fun formatTime(time: Int): String {
        val minutes = time / 60
        val seconds = time % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun dispose() {
        batch.dispose()
        font.dispose()
        timeTexture.dispose()
        pauseTexture.dispose()
        playTexture.dispose()
        continueTexture.dispose()
        stundenplanTexture.dispose()
    }
}
