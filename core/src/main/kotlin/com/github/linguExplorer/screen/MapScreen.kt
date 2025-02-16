package com.github.linguExplorer.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TmxMapLoader
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.linguExplorer.component.*
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
import com.badlogic.gdx.scenes.scene2d.ui.Image


class MapScreen(private val game: linguExplorer) : KtxScreen {

    private val stage :Stage = Stage(ExtendViewport(16f,9f))
    private val textureAtlas = TextureAtlas(/*"com/github/linguExplorer/assets/ */ "graphics/entities.atlas")
    private val playerTexture: Texture = Texture(/*"com/github/linguExplorer/assets/ */ "graphics/entities.png")
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
            add<MoveSystem>()
            add<PhysicSystem>()
            add<AnimationSystem>()
            add<CameraSystem>()
            add<RenderSystem>()
            add<DebugSystem>()
        }
    }

    override fun show() {
        log.debug { "Game Screen gets shown" }
        world.systems.forEach { system ->
            if (system is EventListener) {
                stage.addListener(system)
            }
        }

        // Karte laden
        currentMap = TmxMapLoader().load(/*"com/github/linguExplorer/assets/ */ "graphics/map/main-map.tmx")
        stage.fire(MapChangeEvent(currentMap!!))

        // fixe Bilder hinzufügen
        addUIImages()

        // Spieler-Eingabeverarbeitung
        //PlayerKeyboardInputProcessor(world, stage, world.mapper())

        val playerInputProcessor = PlayerKeyboardInputProcessor(world, stage, world.mapper())
        // InputMultiplexer um Spielfigur + UI zu verarbeiten
        val inputMultiplexer = InputMultiplexer()
        inputMultiplexer.addProcessor(playerInputProcessor)
        inputMultiplexer.addProcessor(stage)   // Spielfigur und Welt-Stage
        //inputMultiplexer.addProcessor(uiStage) // UI-Stage*/

        Gdx.input.inputProcessor = inputMultiplexer //Multiplexer als Input-Prozessor setzen
    }

    private val uiStage: Stage = Stage(ExtendViewport(16f, 9f)) // Eine Stage speziell für die UI

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
        val mapImage = com.badlogic.gdx.scenes.scene2d.ui.Image(mapTexture)
        val phrasingBookImage = com.badlogic.gdx.scenes.scene2d.ui.Image(phrasingBookTexture)
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
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {

       world.update(delta.coerceAtMost(0.25f))

        // Zeichne die Welt (z.B. Karte)
        stage.act(Math.min(delta, 1 / 30f)) // Update für die Haupt-Stage
        stage.draw()

        // Zeichne die UI-Stage
        uiStage.act(Math.min(delta, 1 / 30f)) // Update für die UI-Stage
        uiStage.draw() // UI immer über der Welt
    }

    override fun dispose() {
        stage.disposeSafely()
        uiStage.disposeSafely() // Dispose der UI-Stage
        playerTexture.disposeSafely()
        textureAtlas.disposeSafely()
        world.dispose()
        currentMap?.disposeSafely()
        phWorld.disposeSafely()
    }
    companion object : KtxScreen {
        private  val log = logger<MapScreen>()
    }
}
