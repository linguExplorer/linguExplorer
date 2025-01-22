package com.github.linguExplorer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.linguExplorer.component.ImageComponent
import com.github.linguExplorer.linguExplorer
import com.github.linguExplorer.system.*
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.world

import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.log.logger

class MainMenuScreen(private val game: linguExplorer) : KtxScreen {

    private var batch: SpriteBatch = SpriteBatch()
    private var shapeRenderer: ShapeRenderer = ShapeRenderer()

    // Texturen für den Hintergrund und Buttons
    private var backgroundTexture: Texture = Texture("xx_map_assets/Map/ref.png")
    private var startNewGameTexture: Texture = Texture("xx_Images/Buttons/neuesSpiel_green.png")
    private var loadGameTexture: Texture = Texture("xx_Images/Buttons/spielstandLaden_green.png")
    private var settingsIconTexture: Texture = Texture("xx_Images/SettingsIcon.png")
    private var wordmarkTexture: Texture = Texture("xx_Images/wordmark/wordmark_scaled.png")

//Spieler Textur
    private val textureAtlas = TextureAtlas("assets/graphics/idle_animation.atlas")
    private val playerTexture: Texture = Texture("assets/graphics/idle_animation.png")
    private lateinit var gifAnimation: Animation<TextureRegion>
    private var animationTime = 0f

    // Rechtecke für die Buttons
    private val startNewGameButton = Rectangle()
    private val loadGameButton = Rectangle()
    private val settingsButton = Rectangle()

    // Weitere Einstellungen
    private val overlayWidth = 1200f
    private val overlayHeight = 700f
    private val settingsIconSize = 60f
    private var backgroundOffsetX = 0f
    private var backgroundOffsetY = 0f
    private var speedX = 100f
    private var speedY = 50f




    override fun show() {
        // Bildschirmgröße abrufen
        val screenWidth = Gdx.graphics.width.toFloat()
        val screenHeight = Gdx.graphics.height.toFloat()


        gifAnimation = Animation(0.1f, textureAtlas.regions, Animation.PlayMode.LOOP)
        // Position und Größe der Buttons definieren
        startNewGameButton.set(100f, screenHeight / 2 - 200f, 200f, 50f)
        loadGameButton.set(100f, screenHeight / 2 - 100f, 200f, 50f)
        settingsButton.set(screenWidth - 70f, screenHeight - 70f, 60f, 60f)


    }

    override fun render(delta: Float) {
        val screenWidth = Gdx.graphics.width.toFloat()
        val screenHeight = Gdx.graphics.height.toFloat()

        // Hintergrund berechnen und zeichnen
        val backgroundWidth = backgroundTexture.width.toFloat()
        val backgroundHeight = backgroundTexture.height.toFloat()
        val scale = 1.5f
        val scaleX = screenWidth / backgroundWidth * scale
        val scaleY = screenHeight / backgroundHeight * scale
        val scaleFactor = Math.max(scaleX, scaleY)

        val scaledWidth = backgroundWidth * scaleFactor
        val scaledHeight = backgroundHeight * scaleFactor

        backgroundOffsetX += speedX * delta
        backgroundOffsetY += speedY * delta

        // Kollisionsprüfungen und Hintergrund bewegen
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


        batch.begin()
        batch.draw(backgroundTexture, backgroundOffsetX, backgroundOffsetY, scaledWidth, scaledHeight)
        batch.end()

        // Overlay zeichnen
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
        // Wordmark und Buttons zeichnen
        val wordmarkHeight = 220f
        val wordmarkAspectRatio = wordmarkTexture.width.toFloat() / wordmarkTexture.height.toFloat()
        val wordmarkWidth = wordmarkHeight * wordmarkAspectRatio
        val wordmarkX = screenWidth / 2 - 560f
        val wordmarkY = screenHeight / 2 + 20f
        batch.draw(wordmarkTexture, wordmarkX, wordmarkY, wordmarkWidth, wordmarkHeight)

        val buttonX = screenWidth / 2 - 500f
        val startNewGameButtonY = screenHeight / 2 - 90f
        val loadGameButtonY = screenHeight / 2 - 205f

        // Buttons zeichnen
        batch.draw(startNewGameTexture, buttonX, startNewGameButtonY)
        batch.draw(loadGameTexture, buttonX, loadGameButtonY)

        // Settings Button
        val settingsX = screenWidth - settingsIconSize - 10f
        val settingsY = screenHeight - settingsIconSize - 10f
        batch.draw(settingsIconTexture, settingsX, settingsY, settingsIconSize, settingsIconSize)


        //Animierten Spieler zeichnen
        animationTime += delta
        val currentFrame = gifAnimation.getKeyFrame(animationTime)
        batch.draw(currentFrame, screenWidth / 2 + 180f, screenHeight / 2 - 200f, 300f,300f)
        batch.end()

        // Prüfen, ob auf den Start-Button geklickt wurde
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            val screenX = Gdx.input.x.toFloat()
            val screenY = Gdx.input.y.toFloat()

            println(screenY)
            println(startNewGameButtonY)
            println(screenX)
            println(buttonX + startNewGameTexture.width)
            println(startNewGameButtonY + startNewGameTexture.height)

            //TODO: Wieso ist das Feld in dem es richtig ist eine Etage über dem Button lmaooo
            if (screenX >= buttonX && screenX <= buttonX + startNewGameTexture.width &&
                screenY >= startNewGameButtonY + startNewGameTexture.height && screenY <= startNewGameButtonY + 2* startNewGameTexture.height) {
                println("klappt")
                game.addScreen(LoadingScreen(game))

                game.addScreen(MapScreen(game))
                game.setScreen<MapScreen>()
            }

        }
    }

    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun resume() {}
    override fun hide() {}
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
    }

    companion object {
        private val log = logger<MapScreen>()
    }
}
