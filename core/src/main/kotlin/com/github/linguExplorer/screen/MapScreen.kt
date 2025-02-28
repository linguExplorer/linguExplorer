package com.github.linguExplorer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.linguExplorer.component.*
import com.github.linguExplorer.event.GamePause
import com.github.linguExplorer.event.MapChangeEvent
import com.github.linguExplorer.event.fire
import com.github.linguExplorer.input.PlayerKeyboardInputProcessor
import com.github.linguExplorer.linguExplorer
import com.github.linguExplorer.system.*
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.world
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.box2d.createWorld
import ktx.log.logger
import ktx.math.vec2

class MapScreen(private val game: linguExplorer, private val tempX: Float, private val tempY : Float) : KtxScreen {

    private val stage :Stage = Stage(ExtendViewport(16f,9f))
    private val textureAtlas = TextureAtlas("graphics/entities.atlas")
    private val playerTexture: Texture = Texture("graphics/entities.png")
    private var currentMap: TiledMap? = null;
    private val phWorld = createWorld(gravity = vec2()).apply {
        autoClearForces = false
    }




    private val world: World= world {

        injectables {
            add(stage)
            add(textureAtlas)
            add(phWorld)
            add(game)
            add("tempX", tempX)
            add("tempY", tempY)
        }

        components {
            add<ImageComponent.Companion.ImageComponentListener>()
            add<PhysicComponent.Companion.PhysicComponentListener>()
        }

        systems {
            add<EntitySpawnSystem>()
            add<CollisionSpawnSystem>()
            add<CollisionDespawnSystem>()
            add<MapChangeSystem>()
            add<PathSystem>()
            add<MoveSystem>()
            add<PhysicSystem>()
            add<AnimationSystem>()
            add<CameraSystem>()
            add<RenderSystem>()
            add<DebugSystem>()
        }
    }


    private val pathSystem = world.system<PathSystem>()



    //Game Menü
    private var update = 0.25f
    private var menuSet = false
    private lateinit var batch: SpriteBatch
    private lateinit var boxTexture: Texture
    private var boxX: Float = 0f
    private var boxY: Float = 0f
    private lateinit var font: BitmapFont
    private lateinit var resumeTexture: Texture
    private lateinit var soundSettingsTexture: Texture
    private lateinit var quitGameTexture: Texture
    private val buttonSize = Vector2(300f, 90f)
    private val buttonSpacing = 40f
    private lateinit var wordmarkTexture: Texture
    private val wordmarkScale = 0.2f
    private lateinit var applyTexture: Texture
    private val applyButtonWidth = 230f  // Breite für den Apply-Button
    private var showSoundSettings = false
    private lateinit var redXTexture: Texture
    private val redXSize = Vector2(60f, 60f) //rotes X-Symbols
    private lateinit var barnoneTexture: Texture
    private lateinit var barfullTexture: Texture // Neue Textur für den farbigen Balken
    private val barnoneWidth = 480f  // Breite des Balkens
    private val barnoneHeight = 25f // Höhe des Balkens
    private val headingLeftPadding = 50f // Abstand der Überschriften vom linken Rand
    private lateinit var circleTexture: Texture
    private val circleSize = Vector2(50f, 50f) // Größe des Kreises
    private var masterCircleX: Float = 0f
    private var soundeffectsCircleX: Float = 0f
    private var musicCircleX: Float = 0f
    private var isDraggingMaster: Boolean = false
    private var isDraggingSoundeffects: Boolean = false
    private var isDraggingMusic: Boolean = false

    // NEU: Offset-Variablen für die Kreispositionen
    private var masterCircleXOffset: Float = 0f // Relativ zum Balkenstart
    private var soundeffectsCircleXOffset: Float = 0f
    private var musicCircleXOffset: Float = 0f

    private var masterBarnoneY: Float = 0f
    private var soundeffectsBarnoneY: Float = 0f
    private var musicBarnoneY: Float = 0f
    private var inputMultiplexer = InputMultiplexer()



    override fun show() {

        batch = SpriteBatch()
        boxTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/box.png"))
        font = BitmapFont(Gdx.files.internal("fonts/pixelsplitter/pixelsplitter.fnt"))
        resumeTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/resume.png"))
        soundSettingsTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/soundsettings.png"))
        quitGameTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/quitgame.png"))
        wordmarkTexture = Texture(Gdx.files.internal("xx_Images/wordmark/wordmark_scaled.png"))
        applyTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/apply.png"))
        redXTexture = Texture(Gdx.files.internal("xx_Images/Buttons/red_X.png")) // Lade die Textur für das rote X
        barnoneTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/barnone.png")) // Lade die Textur für den Balken
        barfullTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/barfull.png")) // Lade die Textur für den farbigen Balken
        circleTexture = Texture(Gdx.files.internal("xx_Images/GameMenü/circle.png")) // Lade die Textur für den Kreis


        log.debug { "Game Screen gets shown" }
        world.systems.forEach { system ->
            if (system is EventListener) {
                stage.addListener(system)
            }
        }


        currentMap = TmxMapLoader().load("graphics/map/main-map.tmx")
        stage.fire(MapChangeEvent(currentMap!!))


        // fixe Bilder hinzufügen
        addUIImages()

            PlayerKeyboardInputProcessor(world, stage, world.mapper(), world.mapper(), stage, pathSystem )

            val playerInputProcessor = PlayerKeyboardInputProcessor(world, stage, world.mapper(), world.mapper(), stage, pathSystem)
            // InputMultiplexer um Spielfigur + UI zu verarbeiten
            inputMultiplexer = InputMultiplexer()
            inputMultiplexer.addProcessor(uiStage)
            inputMultiplexer.addProcessor(stage)
            inputMultiplexer.addProcessor(playerInputProcessor)
            // Spielfigur und Welt-Stage
            inputMultiplexer.addProcessor(uiStage) // UI-Stage*/

            Gdx.input.inputProcessor = inputMultiplexer //Multiplexer als Input-Prozessor setzen


    }

    private val uiStage: Stage = Stage(ExtendViewport(16f, 9f).apply {
        setWorldSize(16f, 9f)
    })
    //fixe Bilder Methode
    private fun addUIImages() {
        // Bilder laden
        val backpackTexture = Texture("graphics/map-objects/Rucksack/v2/Backpack2.png")
        val mapTexture = Texture("graphics/map-objects/Map/v2/Map2.png")
        val phrasingBookTexture = Texture("graphics/map-objects/Phrasenheft/v2/Phrasenheft2-1.png.png")
        //val progressBarTexture = Texture("graphics/map-objects/Prozentleiste/v2/Prozentleiste2-1.png.png")
        val moneyBagTexture = Texture("graphics/map-objects/Coinbag/v2/MoneyBag2-2.png.png")

        // Images für jedes Bild
        val backpackImage = com.badlogic.gdx.scenes.scene2d.ui.Image(backpackTexture)
        backpackImage.touchable = Touchable.enabled

        val mapImage = com.badlogic.gdx.scenes.scene2d.ui.Image(mapTexture)
        val phrasingBookImage = com.badlogic.gdx.scenes.scene2d.ui.Image(phrasingBookTexture)
        backpackImage.touchable = Touchable.enabled

        //val progressBarImage = com.badlogic.gdx.scenes.scene2d.ui.Image(progressBarTexture)
        val moneyBagImage = com.badlogic.gdx.scenes.scene2d.ui.Image(moneyBagTexture)

        // Größe
        val imageSize = 2f //Definiere die größe für die Bilder
        backpackImage.setSize(imageSize, imageSize)
        mapImage.setSize(imageSize, imageSize)
        phrasingBookImage.setSize(imageSize, imageSize)
        //progressBarImage.setSize(imageSize, imageSize-1f)
        moneyBagImage.setSize(imageSize, imageSize)

        // Position
        backpackImage.setPosition(12.8f, 7.2f)  // rechts oben
        mapImage.setPosition(0f, 6.5f)     // links oben
        phrasingBookImage.setPosition(14.5f, 0.5f) // rechts unten
        //progressBarImage.setPosition(0f, 8f) // Beispiel
        moneyBagImage.setPosition(14.5f, 7f)   // rechts oben

        // Bilder zur UI-Stage hinzufügen
        uiStage.addActor(backpackImage)
        uiStage.addActor(mapImage)
        uiStage.addActor(phrasingBookImage)
        //uiStage.addActor(progressBarImage)
        uiStage.addActor(moneyBagImage)



        backpackImage.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                println("Backpack clicked")

            }
        })

        phrasingBookImage.addListener(object : ClickListener() {
            override fun clicked(event: InputEvent?, x: Float, y: Float) {
                println("Phrasebook clicked")


                if (!game.containsScreen<PhrasenheftScreen>()) {
                    game.addScreen(PhrasenheftScreen(game))
                }
                game.setScreen<PhrasenheftScreen>()




            }
        })


    }








    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
        uiStage.viewport.update(width, height, true)

    }

    override fun render(delta: Float) {

  
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {

                if(menuSet) {

                    menuSet = false
                    Gdx.input.inputProcessor = inputMultiplexer
                    update = 0.25f

                } else {
                    menuSet = true
                    Gdx.input.inputProcessor = null
                    update = 0f
                }

                println("is clicked")

        }


        world.update(delta.coerceAtMost(update))


        // Zeichne die UI-Stage
        uiStage.act(Math.min(delta, 1 / 30f)) // Update für die UI-Stage
        uiStage.draw() // UI immer über der Welt


        //Game Menü



if(menuSet) {



    val screenWidth = Gdx.graphics.width.toFloat()
    val screenHeight = Gdx.graphics.height.toFloat()

    val boxWidth = screenWidth * 0.30f
    val boxHeight = screenHeight * 0.6f

    boxX = (screenWidth - boxWidth) / 2f
    boxY = (screenHeight - boxHeight) / 2f - screenHeight * 0.10f


    batch.begin()

    // Wordmark zeichnen
    val wordmarkWidth = wordmarkTexture.width.toFloat() * wordmarkScale
    val wordmarkHeight = wordmarkTexture.height.toFloat() * wordmarkScale
    val wordmarkX = (screenWidth - wordmarkWidth) / 2f
    val wordmarkY = boxY + boxHeight + (screenHeight - (boxY + boxHeight)) / 2 - wordmarkHeight / 2

    batch.draw(wordmarkTexture, wordmarkX, wordmarkY, wordmarkWidth, wordmarkHeight)

    batch.draw(boxTexture, boxX, boxY, boxWidth, boxHeight)

    font.color = Color.BLACK
    // Überschrift zeichnen
    font.data.setScale(0.4f)
    val headlineText = if (showSoundSettings) "SOUND" else "OPTIONS"
    val headlineLayout = GlyphLayout(font, headlineText)
    val headlineTextX = boxX + (boxWidth - headlineLayout.width) / 2
    val headlineTextY = boxY + boxHeight - 80
    font.draw(batch, headlineText, headlineTextX, headlineTextY)

    // Button-Positionierung (einmalig berechnen)
    val resumeButtonX = boxX + (boxWidth - buttonSize.x) / 2
    val resumeButtonY = headlineTextY - 120f - buttonSize.y

    val soundSettingsButtonX = boxX + (boxWidth - buttonSize.x) / 2
    val soundSettingsButtonY = resumeButtonY - buttonSize.y - buttonSpacing

    val quitGameButtonX = boxX + (boxWidth - buttonSize.x) / 2
    val quitGameButtonY = soundSettingsButtonY - buttonSize.y - buttonSpacing

    // apply button
    val applyButtonX = boxX + (boxWidth - applyButtonWidth) / 2  // Zentriere den schmaleren Button
    val applyButtonY = boxY - buttonSize.y / 2

    // Position des roten X-Symbols
    val redXButtonX = boxX + boxWidth - redXSize.x - 20 // 20 Pixel Abstand vom rechten Rand
    val redXButtonY = boxY + boxHeight - redXSize.y - 20 // 20 Pixel Abstand vom oberen Rand

    if (!showSoundSettings) {
        // Buttons zeichnen
        batch.draw(resumeTexture, resumeButtonX, resumeButtonY, buttonSize.x, buttonSize.y)
        batch.draw(soundSettingsTexture, soundSettingsButtonX, soundSettingsButtonY, buttonSize.x, buttonSize.y)
        batch.draw(quitGameTexture, quitGameButtonX, quitGameButtonY, buttonSize.x, buttonSize.y)
    } else {
        // Zeichne das rote X-Symbol, wenn Soundeinstellungen angezeigt werden
        batch.draw(redXTexture, redXButtonX, redXButtonY, redXSize.x, redXSize.y)

        //Startposition für die Sound-Einstellungen-Inhalte
        var currentY = headlineTextY - 100f // Abstand zur Überschrift

        // Schriftgröße für die Sound-Einstellungen-Überschriften
        font.data.setScale(0.2f)

        val headingSpacing = 50f // Abstand zwischen Überschrift und Balken
        val sectionSpacing = 45f // Abstand zwischen den Abschnitten (Überschrift + Balken)

        //Master Volume
        val masterText = "MASTER"
        val masterLayout = GlyphLayout(font, masterText)
        val masterTextX = boxX + headingLeftPadding // Linksbündig mit Abstand
        font.draw(batch, masterText, masterTextX, currentY)
        currentY -= masterLayout.height + headingSpacing // Abstand zum Balken
        masterBarnoneY = currentY // Speichere die Y-Position für später

        //Zeichne zuerst die volle bar none textur
        batch.draw(barnoneTexture, boxX + headingLeftPadding, currentY, barnoneWidth, barnoneHeight)

        // Definiere eine minimale Breite für die farbige Bar
        val minColoredBarWidth = barnoneWidth * 0.1f //Beispielwert: 10% der gesamten Balkenlänge

        // Berechne die gewünschte Breite basierend auf der Kugelposition
        var masterColoredBarWidth = masterCircleX - (boxX + headingLeftPadding) + circleSize.x / 3

        // Stelle sicher, dass die Breite nicht kleiner als die minimale Breite ist
        masterColoredBarWidth = Math.max(masterColoredBarWidth, minColoredBarWidth)

        val scaleX = masterColoredBarWidth / barfullTexture.width

        batch.draw(
            barfullTexture,
            boxX + headingLeftPadding,
            currentY,
            masterColoredBarWidth, //Die tatsächliche Breite des gezeichneten Bereichs
            barnoneHeight,
            0,
            0,
            barfullTexture.width, //Die ursprüngliche Breite der Textur
            barfullTexture.height,
            false,
            false
        )

        // Berechne die Kreisposition basierend auf dem Offset
        masterCircleX = boxX + headingLeftPadding + masterCircleXOffset
        // Stelle sicher, dass der Kreis innerhalb der Grenzen bleibt
        masterCircleX = MathUtils.clamp(
            masterCircleX,
            boxX + headingLeftPadding,
            boxX + headingLeftPadding + barnoneWidth - circleSize.x
        )
        // Zeichne den Kreis
        batch.draw(
            circleTexture,
            masterCircleX,
            masterBarnoneY + barnoneHeight / 2f - circleSize.y / 2f,
            circleSize.x,
            circleSize.y
        )

        currentY -= barnoneHeight + sectionSpacing // Abstand zur nächsten Überschrift

        // Soundeffects Volume
        val soundeffectsText = "SOUNDEFFECTS"
        val soundeffectsLayout = GlyphLayout(font, soundeffectsText)
        val soundeffectsTextX = boxX + headingLeftPadding // Linksbündig mit Abstand
        font.draw(batch, soundeffectsText, soundeffectsTextX, currentY)
        currentY -= soundeffectsLayout.height + headingSpacing // Abstand zum Balken
        soundeffectsBarnoneY = currentY // Speichere die Y-Position für später

        //Zeichne zuerst die volle bar none textur
        batch.draw(barnoneTexture, boxX + headingLeftPadding, currentY, barnoneWidth, barnoneHeight)

        // Definiere eine minimale Breite für die farbige Bar
        val minSoundeffectsColoredBarWidth = barnoneWidth * 0.1f //Beispielwert: 10% der gesamten Balkenlänge

        // Berechne die gewünschte Breite basierend auf der Kugelposition
        var soundeffectsColoredBarWidth = soundeffectsCircleX - (boxX + headingLeftPadding) + circleSize.x / 3

        // Stelle sicher, dass die Breite nicht kleiner als die minimale Breite ist
        soundeffectsColoredBarWidth = Math.max(soundeffectsColoredBarWidth, minSoundeffectsColoredBarWidth)

        val soundeffectsScaleX = soundeffectsColoredBarWidth / barfullTexture.width

        batch.draw(
            barfullTexture,
            boxX + headingLeftPadding,
            currentY,
            soundeffectsColoredBarWidth, //Die tatsächliche Breite des gezeichneten Bereichs
            barnoneHeight,
            0,
            0,
            barfullTexture.width, //Die ursprüngliche Breite der Textur
            barfullTexture.height,
            false,
            false
        )

        // Berechne die Kreisposition basierend auf dem Offset
        soundeffectsCircleX = boxX + headingLeftPadding + soundeffectsCircleXOffset
        // Stelle sicher, dass der Kreis innerhalb der Grenzen bleibt
        soundeffectsCircleX = MathUtils.clamp(
            soundeffectsCircleX,
            boxX + headingLeftPadding,
            boxX + headingLeftPadding + barnoneWidth - circleSize.x
        )
        // Zeichne den Kreis
        batch.draw(
            circleTexture,
            soundeffectsCircleX,
            soundeffectsBarnoneY + barnoneHeight / 2f - circleSize.y / 2f,
            circleSize.x,
            circleSize.y
        )

        currentY -= barnoneHeight + sectionSpacing // Abstand zur nächsten Überschrift

        //Music Volume
        val musicText = "MUSIC"
        val musicLayout = GlyphLayout(font, musicText)
        val musicTextX = boxX + headingLeftPadding // Linksbündig mit Abstand
        font.draw(batch, musicText, musicTextX, currentY)
        currentY -= musicLayout.height + headingSpacing // Abstand zum Balken
        musicBarnoneY = currentY // Speichere die Y-Position für später

        //Zeichne zuerst die volle bar none textur
        batch.draw(barnoneTexture, boxX + headingLeftPadding, currentY, barnoneWidth, barnoneHeight)

        // Definiere eine minimale Breite für die farbige Bar
        val minMusicColoredBarWidth = barnoneWidth * 0.1f //Beispielwert: 10% der gesamten Balkenlänge

        // Berechne die gewünschte Breite basierend auf der Kugelposition
        var musicColoredBarWidth = musicCircleX - (boxX + headingLeftPadding) + circleSize.x / 3

        // Stelle sicher, dass die Breite nicht kleiner als die minimale Breite ist
        musicColoredBarWidth = Math.max(musicColoredBarWidth, minMusicColoredBarWidth)

        val musicScaleX = musicColoredBarWidth / barfullTexture.width

        batch.draw(
            barfullTexture,
            boxX + headingLeftPadding,
            currentY,
            musicColoredBarWidth, //Die tatsächliche Breite des gezeichneten Bereichs
            barnoneHeight,
            0,
            0,
            barfullTexture.width, //Die ursprüngliche Breite der Textur
            barfullTexture.height,
            false,
            false
        )

        // Berechne die Kreisposition basierend auf dem Offset
        musicCircleX = boxX + headingLeftPadding + musicCircleXOffset
        // Stelle sicher, dass der Kreis innerhalb der Grenzen bleibt
        musicCircleX = MathUtils.clamp(
            musicCircleX,
            boxX + headingLeftPadding,
            boxX + headingLeftPadding + barnoneWidth - circleSize.x
        )
        // Zeichne den Kreis
        batch.draw(
            circleTexture,
            musicCircleX,
            musicBarnoneY + barnoneHeight / 2f - circleSize.y / 2f,
            circleSize.x,
            circleSize.y
        )

        currentY -= barnoneHeight + 50f // Abstand zum Apply Button

        // apply button
        batch.draw(applyTexture, applyButtonX, applyButtonY, applyButtonWidth, buttonSize.y)
    }

    batch.end()

    // Eingabe verarbeiten (Positionen werden hier übergeben)
    handleInput(
        resumeButtonX,
        resumeButtonY,
        soundSettingsButtonX,
        soundSettingsButtonY,
        quitGameButtonX,
        quitGameButtonY,
        applyButtonX,
        applyButtonY,
        applyButtonWidth,
        redXButtonX,
        redXButtonY
    )

}
        //Game Menü Ende


    }


    private fun handleInput(resumeButtonX: Float, resumeButtonY: Float, soundSettingsButtonX: Float, soundSettingsButtonY: Float, quitGameButtonX: Float, quitGameButtonY: Float, applyButtonX: Float, applyButtonY: Float, applyButtonWidth: Float, redXButtonX: Float, redXButtonY: Float) {
        val mouseX = Gdx.input.x.toFloat()
        val mouseY = Gdx.graphics.height - Gdx.input.y.toFloat()

        //Funktion zum vereinfachen der Überprüfung auf Button gedrückt
        fun isButtonClicked(buttonX: Float, buttonY: Float, buttonWidth: Float, buttonHeight: Float): Boolean {
            return mouseX >= buttonX && mouseX <= buttonX + buttonWidth && mouseY >= buttonY && mouseY <= buttonY + buttonHeight
        }




            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {

            if (!showSoundSettings && isButtonClicked(soundSettingsButtonX, soundSettingsButtonY, buttonSize.x, buttonSize.y)) {
                Gdx.app.log("DEBUG", "SoundSettings Button clicked!")
                stage.fire(GamePause(game))
                showSoundSettings = true
            }

            // Überprüfen, ob der Apply-Button angeklickt wurde
            if (showSoundSettings && isButtonClicked(applyButtonX, applyButtonY, applyButtonWidth, buttonSize.y)) {
                Gdx.app.log("DEBUG", "Apply Button clicked!")
                showSoundSettings = false

                update = 0.25f
                Gdx.input.inputProcessor = inputMultiplexer
                menuSet = false
            }

            // Überprüfen, ob das rote X-Symbol angeklickt wurde
            if (showSoundSettings && isButtonClicked(redXButtonX, redXButtonY, redXSize.x, redXSize.y)) {
                Gdx.app.log("DEBUG", "Red X Button clicked!")

                showSoundSettings = false // Zurück zum Hauptmenü
            }
        }

        //Dragging Funktionalität einbauen
        if (showSoundSettings) {
            // Beim Drücken: Überprüfe, ob ein Kreis angeklickt wurde
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                val circleTouchArea = 20f
                val masterCircleY = masterBarnoneY + barnoneHeight / 2f - circleSize.y / 2f
                if (mouseX >= masterCircleX - circleTouchArea && mouseX <= masterCircleX + circleSize.x + circleTouchArea
                    && mouseY >= masterCircleY - circleTouchArea && mouseY <= masterCircleY + circleSize.y + circleTouchArea) {
                    isDraggingMaster = true
                }
                val soundeffectsCircleY = soundeffectsBarnoneY + barnoneHeight / 2f - circleSize.y / 2f
                if (mouseX >= soundeffectsCircleX - circleTouchArea && mouseX <= soundeffectsCircleX + circleSize.x + circleTouchArea
                    && mouseY >= soundeffectsCircleY - circleTouchArea && mouseY <= soundeffectsCircleY + circleSize.y + circleTouchArea) {
                    isDraggingSoundeffects = true
                }
                val musicCircleY = musicBarnoneY + barnoneHeight / 2f - circleSize.y / 2f
                if (mouseX >= musicCircleX - circleTouchArea && mouseX <= musicCircleX + circleSize.x + circleTouchArea
                    && mouseY >= musicCircleY - circleTouchArea && mouseY <= musicCircleY + circleSize.y + circleTouchArea) {
                    isDraggingMusic = true
                }
            }

            // Beim Loslassen: Setze isDragging zurück
            if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                isDraggingMaster = false
                isDraggingSoundeffects = false
                isDraggingMusic = false
            }

            // Wenn ein Kreis gezogen wird: Aktualisiere die Position
            if (isDraggingMaster) {
                masterCircleXOffset = mouseX - (boxX + headingLeftPadding) - circleSize.x / 2f
                masterCircleXOffset = MathUtils.clamp(masterCircleXOffset, 0f, barnoneWidth - circleSize.x)
            }
            if (isDraggingSoundeffects) {
                soundeffectsCircleXOffset = mouseX - (boxX + headingLeftPadding) - circleSize.x / 2f
                soundeffectsCircleXOffset = MathUtils.clamp(soundeffectsCircleXOffset, 0f, barnoneWidth - circleSize.x)
            }
            if (isDraggingMusic) {
                musicCircleXOffset = mouseX - (boxX + headingLeftPadding) - circleSize.x / 2f
                musicCircleXOffset = MathUtils.clamp(musicCircleXOffset, 0f, barnoneWidth - circleSize.x)
            }
        }
    }

    override fun dispose() {
        stage.disposeSafely()
        playerTexture.disposeSafely()
        textureAtlas.disposeSafely()
        world.dispose()
        currentMap?.disposeSafely()
        phWorld.disposeSafely()
        batch.dispose()
        boxTexture.dispose()
        font.dispose()
        resumeTexture.dispose()
        soundSettingsTexture.dispose()
        quitGameTexture.dispose()
        wordmarkTexture.dispose()
        applyTexture.dispose()
        redXTexture.dispose()
        barnoneTexture.dispose()
        barfullTexture.dispose()
        circleTexture.dispose()

    }
    companion object : KtxScreen {
        private  val log = logger<MapScreen>()
    }
}
