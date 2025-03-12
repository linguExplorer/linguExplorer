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
    private var currentMap: TiledMap? = null;
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
    private val batch: SpriteBatch = SpriteBatch()
    private val boxTexture: Texture = Texture(Gdx.files.internal("xx_Images/GameMenü/box.png"))
    private var boxX: Float = 0f
    private var boxY: Float = 0f
    private var font: BitmapFont = BitmapFont(Gdx.files.internal("fonts/pixelsplitter/pixelsplitter.fnt"))

    private var inputMultiplexer = InputMultiplexer()

    //UI Elemente
    private lateinit var backpackImage: Image
    private lateinit var mapImage: Image
    private lateinit var phrasingBookImage: Image
    private lateinit var moneyBagImage: Image

    //Größe der UI Elemente
    private val imageSize = 220f
    private val padding = 20f



    override fun show() {

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

    private val uiStage: Stage = Stage(ExtendViewport(1920f, 1080f).apply {
        setWorldSize(1920f, 1080f)
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
        backpackImage = Image(backpackTexture)
        backpackImage.touchable = Touchable.enabled

        mapImage = Image(mapTexture)
        phrasingBookImage = Image(phrasingBookTexture)

        moneyBagImage = Image(moneyBagTexture)


        uiStage.addActor(mapImage)
        uiStage.addActor(phrasingBookImage)


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
        batch.projectionMatrix = viewport.camera.combined
        shapeRenderer.projectionMatrix = viewport.camera.combined

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {

            if (menuSet) {

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


        // Hier die Positionen basierend auf der aktuellen Viewport-Größe berechnen
        mapImage.setPosition(
            padding,
            uiStage.viewport.worldHeight - imageSize - padding
        )

        phrasingBookImage.setPosition(
            uiStage.viewport.worldWidth - imageSize - padding,
            padding
        )
        mapImage.setSize(imageSize, imageSize)
        phrasingBookImage.setSize(imageSize, imageSize)


        // Zeichne die UI-Stage
        uiStage.act(Math.min(delta, 1 / 30f)) // Update für die UI-Stage
        uiStage.draw() // UI immer über der Welt


        //Game Menü


        if (menuSet) {
            gameMenuRenderer.renderGameMenu(batch, font, glyphLayout, viewport, shapeRenderer)
            return
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
        shapeRenderer.dispose()
    }

    companion object : KtxScreen {
        private  val log = logger<MapScreen>()
    }
}
