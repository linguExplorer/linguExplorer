package com.github.linguExplorer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.GlyphLayout
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.badlogic.gdx.utils.viewport.Viewport
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
    private var currentMap: TiledMap? = null
    private val phWorld = createWorld(gravity = vec2()).apply {
        autoClearForces = false
    }
    private val gameMenuRenderer = GameMenuRenderer()
    private var shapeRenderer: ShapeRenderer = ShapeRenderer()
    private val viewport: Viewport = ExtendViewport(1920f, 1080f)
    private val glyphLayout = GlyphLayout()

    private val world: World= world {
        injectables {
            add(stage)
            add(textureAtlas)
            add(phWorld)
            add(game)
            add("tempX", tempX)
            add("tempY", tempX)
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
    private val batch: SpriteBatch = SpriteBatch()
    private val boxTexture: Texture = Texture(Gdx.files.internal("xx_Images/GameMenü/box.png"))
    private var font: BitmapFont = BitmapFont(Gdx.files.internal("fonts/pixelsplitter/pixelsplitter.fnt"))

    private var inputMultiplexer = InputMultiplexer()

    //UI Elemente
    private lateinit var backpackImage: Image
    private lateinit var mapImage: Image
    private lateinit var phrasingBookImage: Image
    private lateinit var moneyBagImage: Image
    private lateinit var upperBarImage: Image
    private lateinit var boxInImage: Image
    private lateinit var settingsIconImage: Image
    private lateinit var progressBarImage: Image

    //Größe der UI Elemente
    private val imageSize = 220f
    private val padding = 20f

    //Variablen für die Größenanpassung
    private var upperBarWidth: Float = 400f
    private var settingsIconWidth: Float = 60f
    private var progressBarWidth: Float = 200f

    private var settingsIconXOffset: Float = 20f
    private var progressBarXOffset: Float = 5f

    //Texture Toggle
    private lateinit var boxInTexture: Texture
    private lateinit var boxOutTexture: Texture
    private var isBoxIn = true

    // Fixe Höhe für BoxIn und BoxOut
    private val fixedBoxHeight = 500f

    //Neue Variablen für das Phrasenheft und die Karte
    private var isBoxOutVisible = false

    // UI Stage (wird initialisiert, sobald benötigt)
    private val uiStage: Stage by lazy {
        Stage(ScreenViewport())
    }

    // Initialisierung der UI-Elemente (lazy), um die Abhängigkeit von uiStage zu gewährleisten
    private val uiElements by lazy {
        UIElements(uiStage)
    }

    // Hier die Größen und Positionen definieren
    private var upperBarHeight: Float = 0f
    private var settingsIconHeight: Float = 0f
    private var progressBarHeight: Float = 0f

    // Hilfsklasse, um die UI-Elemente zu verwalten
    inner class UIElements(val uiStage: Stage) {
        // Bilder laden
        val backpackTexture = Texture("graphics/map-objects/Rucksack/v2/Backpack2.png")
        val mapTexture = Texture("graphics/map-objects/Map/v2/Map2.png")
        val phrasingBookTexture = Texture("graphics/map-objects/Phrasenheft/v2/Phrasenheft2-1.png.png")
        val moneyBagTexture = Texture("graphics/map-objects/Coinbag/v2/MoneyBag2-2.png.png")
        val upperBarTexture = Texture(Gdx.files.internal("graphics/map-objects/upperbar 2.png"))
        val boxInTexture = Texture(Gdx.files.internal("graphics/map-objects/boxIn 1.png"))
        val boxOutTexture = Texture(Gdx.files.internal("graphics/map-objects/boxOut 2.png"))
        val settingsIconTexture = Texture(Gdx.files.internal("graphics/map-objects/SettingsIcon 1.png"))
        val progressBarTexture = Texture(Gdx.files.internal("graphics/map-objects/Prozentleiste2-1.png 1.png"))

        // Images für jedes Bild
        val backpackImage = Image(backpackTexture).apply { touchable = Touchable.enabled }
        val mapImage = Image(mapTexture).apply {
            isVisible = false
            setSize(imageSize / 2, imageSize / 2) // Setze die Größe hier
        }
        val phrasingBookImage = Image(phrasingBookTexture).apply {
            isVisible = false
            setSize(imageSize / 2, imageSize / 2) // Setze die Größe hier
        }
        val moneyBagImage = Image(moneyBagTexture)
        val upperBarImage = Image(upperBarTexture)
        val boxInImage = Image(boxInTexture)
        val settingsIconImage = Image(settingsIconTexture)
        val progressBarImage = Image(progressBarTexture)

        init {
            // Images hinzufügen
            uiStage.addActor(upperBarImage)
            uiStage.addActor(boxInImage)
            uiStage.addActor(settingsIconImage)
            uiStage.addActor(progressBarImage)

            //Listener
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

            boxInImage.addListener(object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    // Toggle zwischen boxIn und boxOut
                    isBoxIn = !isBoxIn
                    val newTexture = if (isBoxIn) boxInTexture else boxOutTexture
                    boxInImage.drawable = Image(newTexture).drawable

                    // Aktualisiere die Größe
                    updateBoxImageSize()

                    //Aktualisiere die Sichtbarkeit des Phrasenhefts und der Karte
                    isBoxOutVisible = !isBoxIn
                    mapImage.isVisible = isBoxOutVisible
                    phrasingBookImage.isVisible = isBoxOutVisible

                    // Füge mapImage und phrasingBookImage nur zum Stage hinzu, wenn sie sichtbar sein sollen
                    if (isBoxOutVisible) {
                        uiStage.addActor(mapImage)
                        uiStage.addActor(phrasingBookImage)
                    } else {
                        // Entferne sie vom Stage, wenn sie nicht sichtbar sein sollen
                        mapImage.remove()
                        phrasingBookImage.remove()
                    }
                }
            })
        }

        fun updateBoxImageSize() {
            val texture = if (isBoxIn) boxInTexture else boxOutTexture
            val aspectRatio = texture.width.toFloat() / texture.height.toFloat()
            val newWidth = fixedBoxHeight * aspectRatio
            boxInImage.setSize(newWidth, fixedBoxHeight)
            recalculateMapAndPhrasebookPositions() // Neu berechnen!
        }

        private fun recalculateMapAndPhrasebookPositions() {
            //Positioniere Phrasenheft und Karte untereinander in der Box
            mapImage.setPosition(
                boxInImage.x + (boxInImage.width - mapImage.width) / 2 - 45f, //zentriert horizontal
                boxInImage.y + boxInImage.height - mapImage.height - 45f //oberer Rand der Box - 10f Abstand
            )

            phrasingBookImage.setPosition(
                boxInImage.x + (boxInImage.width - phrasingBookImage.width) / 2 - 45f, //zentriert horizontal
                boxInImage.y + 45f //unterer Rand der Box + 10f Abstand
            )
        }
    }

    override fun show() {
        log.debug { "Game Screen gets shown" }
        world.systems.forEach { system ->
            if (system is EventListener) {
                stage.addListener(system)
            }
        }

        currentMap = TmxMapLoader().load("graphics/map/main-map.tmx")
        stage.fire(MapChangeEvent(currentMap!!))

        // InputMultiplexer um Spielfigur + UI zu verarbeiten
        inputMultiplexer = InputMultiplexer()
        val playerInputProcessor = PlayerKeyboardInputProcessor(world, stage, world.mapper(), world.mapper(), stage, pathSystem)
        inputMultiplexer.addProcessor(uiStage)
        inputMultiplexer.addProcessor(stage)
        inputMultiplexer.addProcessor(playerInputProcessor)
        Gdx.input.inputProcessor = inputMultiplexer //Multiplexer als Input-Prozessor setzen

        calculateUIElementSizesAndPositions() // Initial UI Berechnung
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
        uiStage.viewport.update(width, height, true)
        calculateUIElementSizesAndPositions() // UI Elemente bei Resize neu berechnen
    }

    private fun calculateUIElementSizesAndPositions() {
        // Höhe basierend auf dem Seitenverhältnis und der Breite berechnen
        upperBarHeight = upperBarWidth / (uiElements.upperBarImage.width.toFloat() / uiElements.upperBarImage.height.toFloat())
        settingsIconHeight = settingsIconWidth / (uiElements.settingsIconImage.width.toFloat() / uiElements.settingsIconImage.height.toFloat())
        progressBarHeight = progressBarWidth / (uiElements.progressBarImage.width.toFloat() / uiElements.progressBarImage.height.toFloat())

        // Größe anpassen
        uiElements.upperBarImage.setSize(upperBarWidth, upperBarHeight)
        uiElements.settingsIconImage.setSize(settingsIconWidth, settingsIconHeight)
        uiElements.progressBarImage.setSize(progressBarWidth, progressBarHeight)

        // Positionierung der neuen Bilder (nach der Größenänderung!)
        uiElements.upperBarImage.setPosition(0f, uiStage.height - upperBarHeight) // Links oben

        // Positionierung der Icons innerhalb der upperBar
        uiElements.settingsIconImage.setPosition(settingsIconXOffset, uiStage.height - upperBarHeight + (upperBarHeight - settingsIconHeight) / 2 + 10f) // Zentriert vertikal in der upperBar
        uiElements.progressBarImage.setPosition(settingsIconXOffset + settingsIconWidth + progressBarXOffset, uiStage.height - upperBarHeight + (upperBarHeight - progressBarHeight) / 2 + 10f) // Zentriert vertikal in der upperBar, rechts neben dem Settings-Icon

        //Berechnung von BoxIn
        uiElements.updateBoxImageSize()
        uiElements.boxInImage.setPosition(0f, (uiStage.height - uiElements.boxInImage.height) / 2) // Links mittig
    }

    override fun render(delta: Float) {
        batch.projectionMatrix = viewport.camera.combined
        shapeRenderer.projectionMatrix = viewport.camera.combined

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            menuSet = !menuSet // Einfacher Toggle
            Gdx.input.inputProcessor = if (menuSet) null else inputMultiplexer
            update = if (menuSet) 0f else 0.25f
        }

        world.update(delta.coerceAtMost(update))

        // Zeichne die UI-Stage
        uiStage.act(Math.min(delta, 1 / 30f)) // Update für die UI-Stage
        uiStage.draw() // UI immer über der Welt

        //Game Menü
        if (menuSet) {
            gameMenuRenderer.renderGameMenu(batch, font, glyphLayout, viewport, shapeRenderer)
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
        shapeRenderer.disposeSafely()
        uiStage.disposeSafely()
        uiElements.backpackTexture.disposeSafely()
        uiElements.mapTexture.disposeSafely()
        uiElements.phrasingBookTexture.disposeSafely()
        uiElements.moneyBagTexture.disposeSafely()
        uiElements.upperBarTexture.disposeSafely()
        uiElements.boxInTexture.disposeSafely()
        uiElements.boxOutTexture.disposeSafely()
        uiElements.settingsIconTexture.disposeSafely()
        uiElements.progressBarTexture.disposeSafely()
    }

    companion object : KtxScreen {
        private  val log = logger<MapScreen>()
    }
}
